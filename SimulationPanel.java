import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

public class SimulationPanel extends JPanel {
    private GUIController controller;

    private JLabel complexNameLabel;
    private JLabel monthLabel;
    private JLabel progressPercentLabel;
    private JProgressBar progressBar;

    private JLabel residentsMetricLabel;
    private JLabel occupancyMetricLabel;
    private JLabel rentMetricLabel;
    private JLabel aasMetricLabel;

    private JTable monthlyTable;
    private DefaultTableModel monthlyTableModel;

    private ArrayList<JLabel> amenityStatusLabels;
    private ArrayList<JLabel> amenityIndicatorLabels;

    private JLabel aqsLabel;
    private JLabel operatingCostLabel;
    private JLabel movedOutLabel;

    public SimulationPanel(GUIController controller) {
        this.controller = controller;
        buildLayout();
    }

    public void fillApartmentName(String complexName) {
        complexNameLabel.setText(complexName);
    }

    private void buildLayout() {
        createSimulationComponents();
        removeAll();
        setLayout(new BorderLayout(0, 12));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));

        JPanel top = new JPanel(new BorderLayout(0, 12));
        top.setOpaque(false);
        top.add(createProgressHeader(), BorderLayout.NORTH);
        top.add(createMetricCards(), BorderLayout.SOUTH);

        add(top, BorderLayout.NORTH);
        add(createSimulationBody(), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void createSimulationComponents() {
        complexNameLabel = simulationLabel("Apartment Complex", 12, false);
        monthLabel = simulationLabel("Month 0 of 12", 14, true);
        progressPercentLabel = simulationLabel("0%", 12, false);
        progressPercentLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        progressPercentLabel.setPreferredSize(new Dimension(38, 20));
        monthLabel.setPreferredSize(new Dimension(112, 20));
        progressBar = new JProgressBar(0, 12);
        progressBar.setStringPainted(false);
        progressBar.setBorderPainted(false);
        progressBar.setPreferredSize(new Dimension(0, 10));
        progressBar.putClientProperty("FlatLaf.style",
                "arc: 10; background: #e0e4ea; foreground: #315ff4");
        residentsMetricLabel = simulationLabel("- / -", 22, true);
        occupancyMetricLabel = simulationLabel("-%", 22, true);
        rentMetricLabel = simulationLabel("$-", 22, true);
        aasMetricLabel = simulationLabel("-", 22, true);
        aqsLabel = simulationLabel("-", 14, true);
        operatingCostLabel = simulationLabel("$-", 14, true);
        movedOutLabel = simulationLabel("-", 14, true);
        createAmenityLabels();
        createMonthlyTable();
    }

    private void createAmenityLabels() {
        amenityStatusLabels = new ArrayList<>();
        amenityIndicatorLabels = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            JLabel status = simulationLabel("-", 14, true);
            status.setHorizontalAlignment(SwingConstants.RIGHT);
            amenityStatusLabels.add(status);

            JLabel dot = simulationLabel("●", 12, false);
            dot.setForeground(new Color(0xA2A9B3));
            amenityIndicatorLabels.add(dot);
        }
    }

    private void createMonthlyTable() {
        String[] columns = {
                "MONTH", "AAS", "RESIDENTS", "OCCUPIED", "MOVED OUT", "UTILITY"
        };
        monthlyTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        monthlyTable = new JTable(monthlyTableModel) {
            @Override
            public boolean getScrollableTracksViewportWidth() {
                return getParent() != null
                        && getPreferredSize().width <= getParent().getWidth();
            }
        };
        styleMonthlyTable();
        styleMonthlyTableHeader();
        configureMonthlyColumns();
    }

    private void styleMonthlyTable() {
        monthlyTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        monthlyTable.setBackground(Color.WHITE);
        monthlyTable.setForeground(new Color(0x20242A));
        monthlyTable.setRowHeight(40);
        monthlyTable.setShowHorizontalLines(true);
        monthlyTable.setShowVerticalLines(false);
        monthlyTable.setGridColor(new Color(0xEDF0F2));
        monthlyTable.setIntercellSpacing(new Dimension(0, 1));
        monthlyTable.setFillsViewportHeight(true);
        monthlyTable.setRowSelectionAllowed(false);
        monthlyTable.setColumnSelectionAllowed(false);
        monthlyTable.setFocusable(false);
        monthlyTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        monthlyTable.putClientProperty("FlatLaf.style",
                "background: #ffffff; foreground: #20242a; cellMargins: 0,12,0,12; " +
                        "showHorizontalLines: true; showVerticalLines: false");
    }

    private void styleMonthlyTableHeader() {
        JTableHeader header = monthlyTable.getTableHeader();
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(0, 34));
        header.putClientProperty("FlatLaf.style",
                "background: #fafbfc; foreground: #6e747d; " +
                        "separatorColor: #fafbfc; bottomSeparatorColor: #edf0f2");

        header.setDefaultRenderer((table, value, selected, focused, row, column) -> {
            JLabel label = simulationLabel(value == null ? "" : value.toString(), 11, true);
            label.setOpaque(true);
            label.setBackground(new Color(0xFAFBFC));
            label.setForeground(new Color(0x6E747D));
            label.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xEDF0F2)),
                    BorderFactory.createEmptyBorder(0, 12, 0, 12)));
            return label;
        });
    }

    private void configureMonthlyColumns() {
        int[] widths = {72, 70, 104, 104, 114, 160};

        for (int i = 0; i < widths.length; i++) {
            TableColumn column = monthlyTable.getColumnModel().getColumn(i);
            column.setPreferredWidth(widths[i]);
            column.setMinWidth(widths[i]);
        }
    }

    private JPanel createProgressHeader() {
        JPanel card = simulationCard(16);
        JPanel titles = new JPanel(new BorderLayout(0, 4));
        titles.setOpaque(false);
        titles.add(simulationLabel("Simulation in Progress", 20, true), BorderLayout.NORTH);
        titles.add(complexNameLabel, BorderLayout.CENTER);

        JPanel progress = new JPanel(new BorderLayout(12, 0));
        progress.setOpaque(false);
        progress.add(monthLabel, BorderLayout.WEST);
        progress.add(createProgressTrack(), BorderLayout.CENTER);
        progress.add(progressPercentLabel, BorderLayout.EAST);

        card.add(titles, BorderLayout.NORTH);
        card.add(progress, BorderLayout.SOUTH);
        return card;
    }

    private JPanel createProgressTrack() {
        JPanel track = new JPanel(new GridBagLayout());
        track.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;

        track.add(progressBar, c);
        return track;
    }

    private JPanel createMetricCards() {
        JPanel metrics = new JPanel(new GridLayout(1, 4, 10, 0));
        metrics.setOpaque(false);

        metrics.add(createMetricCard("Residents", residentsMetricLabel, "capacity"));
        metrics.add(createMetricCard("Occupancy", occupancyMetricLabel, ""));
        metrics.add(createMetricCard("Monthly Rent", rentMetricLabel, "per resident"));
        metrics.add(createMetricCard("Current Month AAS", aasMetricLabel, ""));

        return metrics;
    }

    private JPanel createMetricCard(String title, JLabel value, String suffix) {
        JPanel card = simulationCard(14);
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT, 0, 0);
        layout.setAlignOnBaseline(true);

        JPanel values = new JPanel(layout);
        values.setOpaque(false);
        values.add(value);

        if (!suffix.isEmpty()) {
            JLabel detail = simulationLabel(suffix, 11, false);
            detail.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
            values.add(detail);
        }

        card.add(simulationLabel(title, 12, false), BorderLayout.NORTH);
        card.add(values, BorderLayout.CENTER);
        return card;
    }

    private JPanel createSimulationBody() {
        JPanel body = new JPanel(new GridBagLayout());
        body.setOpaque(false);
        JPanel history = createHistoryCard();
        JPanel status = createStatusCard();
        history.setPreferredSize(new Dimension(0, 440));
        status.setPreferredSize(new Dimension(0, 440));
        history.setMinimumSize(new Dimension(0, 0));
        status.setMinimumSize(new Dimension(0, 0));

        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;
        c.gridx = 0;
        c.weightx = 0.64;
        c.insets = new Insets(0, 0, 0, 6);
        body.add(history, c);

        c.gridx = 1;
        c.weightx = 0.36;
        c.insets = new Insets(0, 6, 0, 0);
        body.add(status, c);
        return body;
    }

    private JPanel createHistoryCard() {
        JPanel card = createRoundedHistoryPanel();
        card.putClientProperty("FlatLaf.style",
                "background: #ffffff; arc: 28; border: 1,1,1,1,#edf0f2,1,28");

        JPanel title = new JPanel(new BorderLayout());
        title.setBackground(new Color(0xF5F6F8));
        title.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xE1E5EA)),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)));
        title.add(simulationLabel("Monthly Breakdown", 16, true), BorderLayout.CENTER);

        JScrollPane scroll = new JScrollPane(monthlyTable);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setOpaque(false);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        scroll.getVerticalScrollBar().setUnitIncrement(40);

        card.add(title, BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    private JPanel createRoundedHistoryPanel() {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            public void paint(Graphics graphics) {
                Graphics2D copy = (Graphics2D) graphics.create();
                copy.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                copy.clip(new RoundRectangle2D.Double(
                        0, 0, getWidth(), getHeight(), 28, 28));
                super.paint(copy);
                copy.dispose();
            }
        };
        card.setOpaque(false);
        return card;
    }

    private JPanel createStatusCard() {
        JPanel content = new JPanel(new BorderLayout(0, 18));
        content.setOpaque(false);
        content.add(createAmenitySection(), BorderLayout.NORTH);
        content.add(createMonthSection(), BorderLayout.CENTER);

        JPanel aligned = new JPanel(new BorderLayout());
        aligned.setOpaque(false);
        aligned.add(content, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(aligned);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        JPanel card = simulationCard(18);
        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    private JPanel createAmenitySection() {
        JPanel section = new JPanel(new BorderLayout(0, 12));
        section.setOpaque(false);
        section.add(simulationLabel("Current Amenity Status", 18, true), BorderLayout.NORTH);

        JPanel rows = new JPanel(new GridLayout(4, 1));
        rows.setOpaque(false);
        String[] names = {"Laundry Machines", "Pool", "Gym", "Security"};

        for (int i = 0; i < names.length; i++) {
            rows.add(createAmenityRow(names[i], i));
        }

        section.add(rows, BorderLayout.CENTER);
        return section;
    }

    private JPanel createAmenityRow(String name, int index) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xE1E5EA)),
                BorderFactory.createEmptyBorder(10, 0, 10, 0)));

        JPanel description = new JPanel(new BorderLayout(7, 0));
        description.setOpaque(false);
        description.add(amenityIndicatorLabels.get(index), BorderLayout.WEST);

        JLabel nameLabel = simulationLabel(name, 14, false);
        nameLabel.setForeground(new Color(0x20242A));
        description.add(nameLabel, BorderLayout.CENTER);

        row.add(description, BorderLayout.CENTER);
        row.add(amenityStatusLabels.get(index), BorderLayout.EAST);
        return row;
    }

    private JPanel createMonthSection() {
        JPanel section = new JPanel(new BorderLayout(0, 10));
        section.setOpaque(false);
        section.add(simulationLabel("This Month", 18, true), BorderLayout.NORTH);

        JPanel values = new JPanel(new GridLayout(3, 1));
        values.setOpaque(false);
        values.add(createMonthItem("Apartment Quality Score", aqsLabel));
        values.add(createMonthItem("Operating Cost", operatingCostLabel));
        values.add(createMonthItem("Residents Moved Out", movedOutLabel));

        section.add(values, BorderLayout.CENTER);
        return section;
    }

    private JPanel createMonthItem(String title, JLabel value) {
        JPanel item = new JPanel(new BorderLayout(0, 4));
        item.setOpaque(false);
        item.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xE1E5EA)),
                BorderFactory.createEmptyBorder(8, 0, 12, 0)));

        item.add(simulationLabel(title, 12, false), BorderLayout.NORTH);
        item.add(value, BorderLayout.CENTER);
        return item;
    }

    private JPanel simulationCard(int padding) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.putClientProperty("FlatLaf.style", "background: #f5f6f8; arc: 28");
        card.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
        return card;
    }

    private JLabel simulationLabel(String text, int size, boolean bold) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, size));
        label.setForeground(new Color(bold ? 0x20242A : 0x6E747D));
        return label;
    }

    public void updateProgress(int monthNumber, int totalMonths) {

    }

    public void updateMetrics(int residents, int capacity, double rent, double aas) {

    }

    public void updateAmenityStatuses(String[] names, String[] statuses) {

    }

    public void updateMonthlySummary(double aqs, double operatingCost, int movedOut) {

    }

    public void addMonthRow(MonthlyRecord record, String[] utilityNames) {

    }
}