import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ReviewPanel extends JPanel {
    private GUIController controller;
    private JButton editButton;
    private JButton startButton;

    private boolean showInitialOccupancy = false;

    private JLabel complexNameValueLabel;
    private JLabel apartmentTypeValueLabel;
    private JLabel floorsValueLabel;
    private JLabel roomsPerFloorValueLabel;
    private JLabel totalApartmentsValueLabel;
    private JLabel capacityValueLabel;
    private JLabel initialMetricTitle;
    private JLabel initialMetricValueLabel;

    private ArrayList<JPanel> amenityPills;
    private ArrayList<JLabel> amenityLabels;
    private ArrayList<JLabel> distributionValueLabels;

    public ReviewPanel(GUIController controller) {
        this.controller = controller;
        buildLayout();
        setActions();
    }

    public void displayConfiguration(String[] values) {
        complexNameValueLabel.setText(values[0]);
        floorsValueLabel.setText(values[1]);
        roomsPerFloorValueLabel.setText(values[2]);
        apartmentTypeValueLabel.setText(values[3].equals("1") ? "Singles" : "Randomized Singles and Doubles");
        if (controller.getSelectedSetupMode().equals("import")) {
            initialMetricValueLabel.setText(values[5]);
            setInitialMetricMode(false);
        }
        else {
            initialMetricValueLabel.setText(values[4] + "%");
            setInitialMetricMode(true);
        }
        for (int i = 0; i < 4; i++) {
            distributionValueLabels.get(i).setText(values[6+i]);
        }
        totalApartmentsValueLabel.setText(values[10]);
        capacityValueLabel.setText(values[11]);
        for (int i = 0; i < 4; i++) amenityPills.get(i).setVisible(false);
        for (int i = 0; i < (values.length - 12) / 2; i++) {
            amenityPills.get(i).setVisible(true);
            String[] amenityStatusArr = values[13+2*i].toLowerCase().split("_");
            for (int j = 0; j < amenityStatusArr.length; j++) amenityStatusArr[j] = amenityStatusArr[j].substring(0, 1).toUpperCase() + amenityStatusArr[j].substring(1);
            String amenityStatus = String.join(" ", amenityStatusArr);
            amenityLabels.get(i).setText(values[12+2*i] + ": " + amenityStatus);
        }
        repaint();
    }

    private void buildLayout() {
        if (complexNameValueLabel == null) createReviewComponents();
        removeAll();
        setLayout(new BorderLayout(0, 18));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));
        add(createReviewHeader(), BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(createReviewColumns());
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
        add(createReviewActions(), BorderLayout.SOUTH);
        setInitialMetricMode(showInitialOccupancy);
        revalidate();
        repaint();
    }

    private void setActions() {
        editButton.addActionListener(e -> controller.handleReviewEdit());
        startButton.addActionListener(e -> controller.handleStartSimulation());
    }

    private void createReviewComponents() {
        complexNameValueLabel = reviewLabel("-", 16, true);
        apartmentTypeValueLabel = reviewLabel("-", 16, true);
        floorsValueLabel = reviewLabel("-", 16, true);
        roomsPerFloorValueLabel = reviewLabel("-", 16, true);
        totalApartmentsValueLabel = reviewLabel("-", 16, true);
        capacityValueLabel = reviewLabel("-", 16, true);
        initialMetricTitle = reviewLabel("Initial Residents", 13, false);
        initialMetricValueLabel = reviewLabel("-", 16, true);
        amenityLabels = new ArrayList<>();
        distributionValueLabels = new ArrayList<>();
        String[] names = {"Laundry", "Pool", "Gym", "Security"};
        for (String name : names) {
            amenityLabels.add(reviewLabel(name + ": -", 13, false));
            distributionValueLabels.add(reviewLabel("-", 16, true));
        }
        if (editButton == null) editButton = new JButton("Edit Configuration");
        if (startButton == null) startButton = new JButton("Start Simulation");
    }

    private JPanel createReviewHeader() {
        JPanel header = new JPanel(new BorderLayout(0, 7));
        header.setOpaque(false);
        header.add(reviewLabel(
                "Review Active Configuration", 26, true), BorderLayout.NORTH);
        header.add(reviewLabel(
                "Confirm the exact values that will be used before the simulation begins.",
                15, false), BorderLayout.CENTER);
        return header;
    }

    private JPanel createReviewColumns() {
        JPanel columns = new JPanel(new GridLayout(1, 2, 16, 0));
        columns.setOpaque(false);
        columns.setPreferredSize(new Dimension(1040, 430));
        columns.add(createComplexReview());
        columns.add(createPreferencesReview());
        return columns;
    }

    private JPanel createComplexReview() {
        JPanel grid = new JPanel(new GridLayout(4, 2, 18, 10));
        grid.setOpaque(false);
        grid.add(reviewItem("Complex Name", complexNameValueLabel));
        grid.add(reviewItem("Apartment Type", apartmentTypeValueLabel));
        grid.add(reviewItem("Floors", floorsValueLabel));
        grid.add(reviewItem("Rooms per Floor", roomsPerFloorValueLabel));
        grid.add(reviewItem("Total Apartments", totalApartmentsValueLabel));
        grid.add(reviewItem("Resident Capacity", capacityValueLabel));
        grid.add(reviewItem(initialMetricTitle, initialMetricValueLabel));
        JPanel empty = new JPanel();
        empty.setOpaque(false);
        grid.add(empty);
        return reviewCard("Apartment Complex", grid);
    }

    private JPanel createPreferencesReview() {
        JPanel pills = new JPanel(new GridLayout(2, 2, 8, 8));
        pills.setOpaque(false);
        amenityPills = new ArrayList<>();
        for (JLabel label : amenityLabels) {
            JPanel pill = createAmenityPill(label);
            pills.add(pill);
            amenityPills.add(pill);
        }
        JPanel grid = new JPanel(new GridLayout(2, 2, 18, 10));
        grid.setOpaque(false);
        String[] types = {"No-Maintenance", "Low-Maintenance",
                "Standard-Maintenance", "High-Maintenance"};
        for (int i = 0; i < types.length; i++) {
            grid.add(reviewItem(types[i], distributionValueLabels.get(i)));
        }
        JPanel distribution = new JPanel(new BorderLayout(0, 16));
        distribution.setOpaque(false);
        distribution.add(reviewLabel(
                "Resident Distribution", 20, true), BorderLayout.NORTH);
        distribution.add(grid, BorderLayout.CENTER);
        JPanel content = new JPanel(new BorderLayout(0, 26));
        content.setOpaque(false);
        content.add(pills, BorderLayout.NORTH);
        content.add(distribution, BorderLayout.CENTER);
        return reviewCard("Offered Amenities", content);
    }

    private JPanel createAmenityPill(JLabel label) {
        label.setForeground(new Color(0x157347));
        JPanel pill = new JPanel(new BorderLayout());
        pill.putClientProperty("FlatLaf.style",
                "background: #e9f7ef; arc: 999; border: 8,12,8,12,#c7e9d4,1,999");
        pill.add(label, BorderLayout.CENTER);
        JPanel aligned = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        aligned.setOpaque(false);
        aligned.add(pill);
        return aligned;
    }

    private JPanel reviewCard(String title, JPanel content) {
        JPanel card = new JPanel(new BorderLayout(0, 18));
        card.putClientProperty("FlatLaf.style", "background: #f5f6f8; arc: 28");
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.add(reviewLabel(title, 20, true), BorderLayout.NORTH);
        card.add(content, BorderLayout.CENTER);
        return card;
    }

    private JPanel reviewItem(String title, JLabel value) {
        return reviewItem(reviewLabel(title, 13, false), value);
    }

    private JPanel reviewItem(JLabel title, JLabel value) {
        JPanel item = new JPanel(new BorderLayout(0, 6));
        item.setOpaque(false);
        item.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xE1E5EA)),
                BorderFactory.createEmptyBorder(8, 0, 12, 0)));
        value.setVerticalAlignment(SwingConstants.TOP);
        item.add(title, BorderLayout.NORTH);
        item.add(value, BorderLayout.CENTER);
        return item;
    }

    private JLabel reviewLabel(String text, int size, boolean bold) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, size));
        label.setForeground(new Color(bold ? 0x20242A : 0x6E747D));
        return label;
    }

    private JPanel createReviewActions() {
        JPanel footer = new JPanel(new BorderLayout(18, 0));
        footer.putClientProperty("FlatLaf.style",
                "background: #ffffff; arc: 28; border: 16,18,16,18,#e1e5ea,1,28");
        JPanel text = new JPanel(new BorderLayout(0, 4));
        text.setOpaque(false);
        text.add(reviewLabel("Automatic 12-month run", 17, true), BorderLayout.NORTH);
        text.add(reviewLabel(
                "No input is needed while the simulation is running.",
                13, false), BorderLayout.CENTER);
        styleReviewButton(editButton, false);
        styleReviewButton(startButton, true);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setOpaque(false);
        buttons.add(editButton);
        buttons.add(startButton);
        footer.add(text, BorderLayout.CENTER);
        footer.add(buttons, BorderLayout.EAST);
        return footer;
    }

    private void styleReviewButton(JButton button, boolean primary) {
        String fill = primary ? "#315ff4" : "#ffffff";
        button.setFont(new Font("Segoe UI", primary ? Font.BOLD : Font.PLAIN, 15));
        button.setMargin(new Insets(12, 18, 12, 18));
        button.putClientProperty("JComponent.minimumHeight", 48);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.putClientProperty("FlatLaf.style",
                "arc: 20; focusWidth: 0; innerFocusWidth: 0; background: " + fill + "; " +
                        "foreground: " + (primary ? "#ffffff" : "#20242a") + "; " +
                        "borderColor: " + (primary ? "#315ff4" : "#dce0e5") + "; " +
                        "focusedBorderColor: #000000; focusedBackground: " + fill + "; " +
                        "hoverBackground: " + fill + "; pressedBackground: " + fill);
    }

    public void setInitialMetricMode(boolean usePercentage) {
        showInitialOccupancy = usePercentage;
        initialMetricTitle.setText(showInitialOccupancy
                ? "Initial Occupancy Percentage" : "Initial Residents");
        revalidate();
        repaint();
    }
}
