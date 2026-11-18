import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;

public class SetupPanel extends JPanel {
    private GUIController controller;
    private JTextField complexNameField;
    private JTextField floorsField;
    private JTextField roomsPerFloorField;
    private JComboBox<String> apartmentTypeBox;
    private JTextField initialResidentsField;
    private JLabel capacityLabel;
    private ArrayList<JCheckBox> amenityChecks;
    private ArrayList<JComboBox<String>> amenityStatusBoxes;
    private ArrayList<JTextField> distributionFields;
    private JLabel distributionTotalLabel;
    private JButton useDefaultButtons;
    private JButton submitButton;
    private JButton cancelButton;
    private ArrayList<JLabel> errorLabels;

    public SetupPanel(GUIController controller) {
        this.controller = controller;
        buildLayout();
        setListeners();
    }

    private void setListeners() {
        floorsField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                updateCapacityDisplay(calcCapacity());
            }
        });
        roomsPerFloorField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                updateCapacityDisplay(calcCapacity());
            }
        });
        apartmentTypeBox.addActionListener(e -> {
            updateCapacityDisplay(calcCapacity());
        });
        for (JTextField field : distributionFields) {
            field.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    updateDistributionTotal(calcDistributionTotal());
                }
            });
        }
    }

    private int calcCapacity() {
        try {
            int floors = Integer.parseInt(floorsField.getText());
            int rooms = Integer.parseInt(roomsPerFloorField.getText());
            return apartmentTypeBox.getSelectedIndex() == 0 ? floors * rooms : (int) (floors * (rooms * 1.5));
        } catch (NullPointerException | NumberFormatException e) {
            return -1;
        }
    }

    private int calcDistributionTotal() {
        int total = 0;
        for (JTextField field : distributionFields) {
            try {
                String fieldContent = field.getText();
                if (fieldContent.isEmpty()) throw new NullPointerException();
                total += Integer.parseInt(fieldContent);
            } catch (NullPointerException e) {
                total += 0;
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return total;
    }

    public String[] getFormValues() {
        return null;
    }

    public void setFormValues(String[] values) {

    }

    public void showFieldErrors(String settingName, String message) {

    }

    public void clearFieldErrors() {

    }

    public void updateCapacityDisplay(int capacity) {
        capacityLabel.setText("Capacity: " + (capacity != -1 ? capacity : "-"));
    }

    public void updateDistributionTotal(int distribution) {
        String distributionTotalPercentage = distribution != -1 ? (distribution + "%") : "-%";
        distributionTotalLabel.setText("Distribution total: " + distributionTotalPercentage);
    }

    private void buildLayout() {
        if (complexNameField == null) createSetupComponents();
        removeAll();
        setLayout(new BorderLayout(0, 18));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));
        add(createSetupHeader(), BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(createSetupColumns());
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
        add(createSetupActions(), BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    private void createSetupComponents() {
        complexNameField = setupTextField("Apartment Complex");
        floorsField = setupTextField("2-8");
        roomsPerFloorField = setupTextField("4-12");
        initialResidentsField = setupTextField("1 to calculated capacity");
        apartmentTypeBox = new JComboBox<>(new String[]{"1 - Singles only", "2 - Randomized Singles & Doubles"});
        styleSetupInput(apartmentTypeBox);
        capacityLabel = setupLabel("Capacity: -", 12, false);
        amenityChecks = new ArrayList<>();
        amenityStatusBoxes = new ArrayList<>();
        distributionFields = new ArrayList<>();
        errorLabels = new ArrayList<>();
        String[][] amenityStatuses = {{"Full Operation", "Half Operation", "Closed"}, {"Full Operation", "Closed"}, {"Full Operation", "Reduced Machines", "Closed"}, {"Full Operation", "Reduced Force", "Not Functioning"}};
        for (int i = 0; i < 4; i++) {
            amenityChecks.add(new JCheckBox());
            JComboBox<String> status = new JComboBox<>(amenityStatuses[i]);
            styleSetupInput(status);
            amenityStatusBoxes.add(status);
            distributionFields.add(setupTextField("0-100"));
        }
        createSetupErrorLabels();
        distributionTotalLabel = setupLabel("Distribution total: -%", 12, false);
        useDefaultButtons = new JButton("Use Defaults");
        submitButton = new JButton("Validate & Continue");
        cancelButton = new JButton("Cancel");
    }

    // Error indices:
// 0=name, 1=floors, 2=rooms, 3=apartment type, 4=initial residents
// 5–8=amenities, 9–12=resident distribution
    private void createSetupErrorLabels() {
        for (int i = 0; i < 13; i++) {
            JLabel error = setupLabel(" ", 12, false);
            error.setForeground(new Color(0xD43B45));
            error.setVisible(false);
            errorLabels.add(error);
        }
    }

    private JPanel createSetupHeader() {
        JPanel header = new JPanel(new BorderLayout(12, 7));
        header.setOpaque(false);
        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);
        titleRow.add(setupLabel("Manual Setup", 24, true), BorderLayout.CENTER);
        JPanel badge = new JPanel(new BorderLayout());
        badge.putClientProperty("FlatLaf.style", "background: #f5f6f8; arc: 12");
        badge.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        badge.add(setupLabel("Step 1 of 3", 12, false));
        titleRow.add(badge, BorderLayout.EAST);
        header.add(titleRow, BorderLayout.NORTH);
        header.add(setupLabel("Enter every value below. Capacity depends on the building layout.", 14, false), BorderLayout.CENTER);
        return header;
    }

    private JPanel createSetupColumns() {
        JPanel columns = setupWidthPanel(new GridBagLayout(), 1040);
        columns.setOpaque(false);
        JPanel[] panels = {createBuildingPanel(), createAmenitiesPanel(), createDistributionPanel()};
        double[] weights = {0.32, 0.38, 0.30};
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        for (int i = 0; i < panels.length; i++) {
            panels[i].setMinimumSize(new Dimension(0, 0));
            c.gridx = i;
            c.weightx = weights[i];
            c.insets = new Insets(0, i == 0 ? 0 : 7, 0, i == 2 ? 0 : 7);
            columns.add(panels[i], c);
        }
        return columns;
    }

    private JPanel createBuildingPanel() {
        JPanel fields = new JPanel(new GridLayout(5, 1, 0, 14));
        fields.setOpaque(false);
        fields.add(setupField("Apartment Complex Name", complexNameField, setupLabel("1-40 characters", 12, false), 0));
        fields.add(setupField("Number of Floors", floorsField, setupLabel("2-8", 12, false), 1));
        fields.add(setupField("Rooms per Floor", roomsPerFloorField, setupLabel("4-12", 12, false), 2));
        fields.add(setupField("Apartment Type", apartmentTypeBox, null, 3));
        fields.add(setupField("Initial Residents", initialResidentsField, capacityLabel, 4));
        return setupCard("Building & Residents", fields);
    }

    private JPanel createAmenitiesPanel() {
        JPanel content = new JPanel(new BorderLayout(0, 12));
        content.setOpaque(false);
        content.add(setupLabel("Choose offered amenities and initial statuses.", 12, false), BorderLayout.NORTH);
        JPanel rows = new JPanel(new GridLayout(4, 1, 0, 10));
        rows.setOpaque(false);
        String[] names = {"Laundry Machines", "Pool", "Gym", "Security"};
        for (int i = 0; i < 4; i++) {
            JCheckBox check = amenityChecks.get(i);
            check.setText(names[i]);
            check.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            check.setOpaque(false);
            JPanel row = new JPanel(new BorderLayout(8, 4));
            row.putClientProperty("FlatLaf.style", "background: #ffffff; arc: 14");
            row.setBorder(BorderFactory.createEmptyBorder(7, 8, 7, 8));
            row.add(check, BorderLayout.WEST);
            row.add(amenityStatusBoxes.get(i), BorderLayout.CENTER);
            row.add(errorLabels.get(5 + i), BorderLayout.SOUTH);
            rows.add(row);
        }
        content.add(rows, BorderLayout.CENTER);
        return setupCard("Amenities Offered", content);
    }

    private JPanel createDistributionPanel() {
        JPanel content = new JPanel(new BorderLayout(0, 14));
        content.setOpaque(false);
        content.add(setupLabel("Whole-number percentages must total 100%.", 12, false), BorderLayout.NORTH);
        JPanel fields = new JPanel(new GridLayout(4, 1, 0, 14));
        fields.setOpaque(false);
        String[] types = {"No-Maintenance", "Low-Maintenance", "Standard-Maintenance", "High-Maintenance"};
        for (int i = 0; i < 4; i++) {
            JPanel input = new JPanel(new BorderLayout(6, 0));
            input.setOpaque(false);
            input.add(distributionFields.get(i), BorderLayout.CENTER);
            input.add(setupLabel("%", 14, false), BorderLayout.EAST);
            fields.add(setupField(types[i], input, null, 9 + i));
        }
        content.add(fields, BorderLayout.CENTER);
        content.add(distributionTotalLabel, BorderLayout.SOUTH);
        return setupCard("Resident Distribution", content);
    }

    private JPanel setupCard(String title, JPanel body) {
        JPanel card = setupWidthPanel(new BorderLayout(), 0);
        card.putClientProperty("FlatLaf.style", "background: #f5f6f8; arc: 26");
        card.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        JPanel top = new JPanel(new BorderLayout(0, 14));
        top.setOpaque(false);
        top.add(setupLabel(title, 18, true), BorderLayout.NORTH);
        top.add(body, BorderLayout.CENTER);
        card.add(top, BorderLayout.NORTH);
        return card;
    }

    private JPanel setupField(String title, JComponent input, JLabel hint, int errorIndex) {
        JPanel heading = new JPanel(new BorderLayout(6, 0));
        heading.setOpaque(false);
        heading.add(setupLabel(title, 13, false), BorderLayout.CENTER);
        if (hint != null) heading.add(hint, BorderLayout.EAST);
        JPanel field = new JPanel(new BorderLayout(0, 6));
        field.setOpaque(false);
        field.add(heading, BorderLayout.NORTH);
        field.add(input, BorderLayout.CENTER);
        field.add(errorLabels.get(errorIndex), BorderLayout.SOUTH);
        return field;
    }

    // Keep the requested width while allowing height to grow for error messages.
    private JPanel setupWidthPanel(LayoutManager layout, int width) {
        return new JPanel(layout) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(width, super.getPreferredSize().height);
            }
        };
    }

    private JLabel setupLabel(String text, int size, boolean bold) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, size));
        label.setForeground(new Color(bold ? 0x20242A : 0x6E747D));
        return label;
    }

    private JTextField setupTextField(String placeholder) {
        JTextField field = new JTextField();
        styleSetupInput(field);
        field.putClientProperty("JTextField.placeholderText", placeholder);
        return field;
    }

    private void styleSetupInput(JComponent component) {
        component.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        component.setPreferredSize(new Dimension(0, 40));
        component.setMinimumSize(new Dimension(0, 40));
        component.putClientProperty("FlatLaf.style", "arc: 12; background: #ffffff; borderColor: #dce0e5; " + "focusedBorderColor: #000000; focusWidth: 0");
    }

    private JPanel createSetupActions() {
        styleSetupButton(cancelButton, false);
        styleSetupButton(useDefaultButtons, false);
        styleSetupButton(submitButton, true);
        cancelButton.putClientProperty("JButton.buttonType", "borderless");
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);
        actions.add(cancelButton);
        actions.add(useDefaultButtons);
        actions.add(submitButton);
        return actions;
    }

    private void styleSetupButton(JButton button, boolean primary) {
        String fill = primary ? "#315ff4" : "#ffffff";
        button.setFont(new Font("Segoe UI", primary ? Font.BOLD : Font.PLAIN, 14));
        button.setMargin(new Insets(10, 18, 10, 18));
        button.putClientProperty("JComponent.minimumHeight", 44);
        button.putClientProperty("FlatLaf.style", "arc: 16; focusWidth: 0; innerFocusWidth: 0; background: " + fill + "; " + "foreground: " + (primary ? "#ffffff" : "#20242a") + "; " + "borderColor: " + (primary ? "#315ff4" : "#dce0e5") + "; " + "focusedBorderColor: #000000; focusedBackground: " + fill + "; " + "hoverBackground: " + fill + "; pressedBackground: " + fill);
    }
}
