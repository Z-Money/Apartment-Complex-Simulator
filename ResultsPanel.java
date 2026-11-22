import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

public class ResultsPanel extends JPanel {
    private GUIController controller;

    private JLabel completionMessageLabel;
    private ArrayList<JLabel> summaryMetricLabels;

    private JTable monthlyTable;
    private DefaultTableModel monthlyTableModel;

    private JLabel highestTypeLabel;
    private JLabel lowestTypeLabel;

    private JPanel resultsFileNotice;
    private JPanel settingsFileNotice;
    private JLabel resultsFileStatusLabel;
    private JLabel settingsFileStatusLabel;
    private JTextArea resultsFileNameArea;
    private JTextArea settingsFileNameArea;

    private JButton exitButton;

    public ResultsPanel(GUIController controller) {
        this.controller = controller;
        buildLayout();
        addButtonFuns();
    }

    private void addButtonFuns() {
        exitButton.addActionListener(e -> controller.handleExit());
    }

    private void buildLayout() {
        if (summaryMetricLabels == null) createResultsComponents();
        removeAll();
        setLayout(new BorderLayout(0, 18));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));

        add(createResultsHeader(), BorderLayout.NORTH);
        add(createResultsBody(), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void createResultsComponents() {
        completionMessageLabel = resultsLabel(
                "Review the 12-month results and output file status below.", 15, false);
        summaryMetricLabels = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            summaryMetricLabels.add(resultsLabel("-", 18, true));
        }
        highestTypeLabel = resultsLabel("-", 14, true);
        lowestTypeLabel = resultsLabel("-", 14, true);
        resultsFileStatusLabel = resultsLabel("Results file pending", 13, true);
        settingsFileStatusLabel = resultsLabel("Settings file pending", 13, true);
        resultsFileNameArea = createFileNameArea();
        settingsFileNameArea = createFileNameArea();
        exitButton = new JButton("Exit");
        styleExitButton();
        createMonthlyTable();
    }

    private JPanel createResultsHeader() {
        JPanel header = new JPanel(new BorderLayout(0, 6));
        header.setOpaque(false);
        header.add(resultsLabel("Simulation Complete", 26, true), BorderLayout.NORTH);
        header.add(completionMessageLabel, BorderLayout.CENTER);
        return header;
    }

    private JPanel createResultsBody() {
        JPanel body = new JPanel(new GridBagLayout());
        body.setOpaque(false);
        JPanel left = createResultsColumn();
        JScrollPane right = createSidebarScroll();
        left.setPreferredSize(new Dimension(0, 450));
        right.setPreferredSize(new Dimension(0, 450));
        left.setMinimumSize(new Dimension(0, 0));
        right.setMinimumSize(new Dimension(0, 0));

        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;
        c.gridx = 0;
        c.weightx = 0.64;
        c.insets = new Insets(0, 0, 0, 6);
        body.add(left, c);

        c.gridx = 1;
        c.weightx = 0.36;
        c.insets = new Insets(0, 6, 0, 0);
        body.add(right, c);
        return body;
    }

    private JPanel createResultsColumn() {
        JPanel column = new JPanel(new BorderLayout(0, 12));
        column.setOpaque(false);
        column.add(createSummaryMetrics(), BorderLayout.NORTH);
        column.add(createHistoryCard(), BorderLayout.CENTER);
        return column;
    }

    private JPanel createSummaryMetrics() {
        JPanel metrics = new JPanel(new GridLayout(2, 3, 10, 10));
        metrics.setOpaque(false);
        String[] titles = {"Occupied Apartments", "Total Residents", "Total Moved Out",
                "12-Month Average AAS", "Total Utility Usage", "Occupancy"};

        for (int i = 0; i < titles.length; i++) {
            JPanel card = resultsCard(12);
            card.add(resultsLabel(titles[i], 11, false), BorderLayout.NORTH);
            card.add(summaryMetricLabels.get(i), BorderLayout.CENTER);
            metrics.add(card);
        }
        return metrics;
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
            JLabel label = resultsLabel(value == null ? "" : value.toString(), 11, true);
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

    private JPanel createHistoryCard() {
        JPanel card = createRoundedHistoryPanel();
        card.putClientProperty("FlatLaf.style",
                "background: #ffffff; arc: 28; border: 1,1,1,1,#edf0f2,1,28");

        JPanel title = new JPanel(new BorderLayout());
        title.setBackground(new Color(0xF5F6F8));
        title.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xE1E5EA)),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)));
        title.add(resultsLabel("Monthly Breakdown", 16, true), BorderLayout.CENTER);

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

    private JScrollPane createSidebarScroll() {
        JPanel aligned = new JPanel(new BorderLayout());
        aligned.setOpaque(false);
        aligned.add(createSidebar(), BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(aligned);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setOpaque(false);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout(0, 16));
        sidebar.setOpaque(false);
        JPanel notices = new JPanel(new GridLayout(2, 1, 0, 10));
        notices.setOpaque(false);

        resultsFileNotice = createFileNotice(resultsFileStatusLabel, resultsFileNameArea);
        settingsFileNotice = createFileNotice(settingsFileStatusLabel, settingsFileNameArea);
        notices.add(resultsFileNotice);
        notices.add(settingsFileNotice);

        JPanel content = new JPanel(new BorderLayout(0, 10));
        content.setOpaque(false);
        content.add(createSatisfactionCard(), BorderLayout.NORTH);
        content.add(notices, BorderLayout.CENTER);
        sidebar.add(content, BorderLayout.CENTER);
        sidebar.add(createExitRow(), BorderLayout.SOUTH);
        return sidebar;
    }

    private JPanel createSatisfactionCard() {
        JPanel card = resultsCard(18);
        card.add(resultsLabel("Satisfaction by Type", 18, true), BorderLayout.NORTH);

        JPanel values = new JPanel(new GridLayout(2, 1));
        values.setOpaque(false);
        values.add(createSatisfactionItem("Highest Average Satisfaction", highestTypeLabel));
        values.add(createSatisfactionItem("Lowest Average Satisfaction", lowestTypeLabel));
        card.add(values, BorderLayout.CENTER);
        return card;
    }

    private JPanel createSatisfactionItem(String title, JLabel value) {
        JPanel item = new JPanel(new BorderLayout(0, 4));
        item.setOpaque(false);
        item.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xE1E5EA)),
                BorderFactory.createEmptyBorder(10, 0, 10, 0)));

        item.add(resultsLabel(title, 12, false), BorderLayout.NORTH);
        item.add(value, BorderLayout.CENTER);
        return item;
    }

    private JTextArea createFileNameArea() {
        JTextArea area = new JTextArea("File name will appear here.", 2, 0);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        area.setForeground(new Color(0x6E747D));
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(false);
        area.setOpaque(false);
        return area;
    }

    private JPanel createFileNotice(JLabel status, JTextArea name) {
        JPanel notice = new JPanel(new BorderLayout(0, 4));
        notice.putClientProperty("FlatLaf.style",
                "background: #f0f3f7; arc: 20; border: 12,12,12,12,#dce0e5,1,20");
        status.setForeground(new Color(0x6E747D));
        notice.add(status, BorderLayout.NORTH);
        notice.add(name, BorderLayout.CENTER);
        return notice;
    }

    private void styleFileNotice(JPanel notice, JLabel status, JTextArea name, boolean success) {
        String background = success ? "#e9f7ef" : "#fff0f1";
        String border = success ? "#bfe2cc" : "#f0b7bc";
        Color foreground = new Color(success ? 0x155F39 : 0x8D2028);

        notice.putClientProperty("FlatLaf.style",
                "arc: 20; background: " + background +
                        "; border: 12,12,12,12," + border + ",1,20");
        status.setForeground(foreground);
        name.setForeground(foreground);
        notice.revalidate();
        notice.repaint();
    }

    private JPanel createExitRow() {
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        actions.setOpaque(false);
        actions.add(exitButton);
        return actions;
    }

    private void styleExitButton() {
        exitButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        exitButton.setMargin(new Insets(12, 18, 12, 18));
        exitButton.putClientProperty("JComponent.minimumHeight", 46);
        exitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        exitButton.putClientProperty("FlatLaf.style",
                "arc: 20; focusWidth: 0; innerFocusWidth: 0; background: #315ff4; " +
                        "foreground: #ffffff; borderColor: #315ff4; " +
                        "focusedBorderColor: #000000; focusedBackground: #315ff4; " +
                        "hoverBackground: #315ff4; pressedBackground: #315ff4");
    }

    private JPanel resultsCard(int padding) {
        JPanel card = new JPanel(new BorderLayout(0, 6));
        card.putClientProperty("FlatLaf.style", "background: #f5f6f8; arc: 28");
        card.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
        return card;
    }

    private JLabel resultsLabel(String text, int size, boolean bold) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, size));
        label.setForeground(new Color(bold ? 0x20242A : 0x6E747D));
        return label;
    }

    public void addMonthRow(MonthlyRecord record) {
        int monthNum = record.getMonthNumber();
        String aas = String.format("%.2f", record.getAverageSatisfaction());
        int residents = record.getTotalResidents();
        int occupied = record.getOccupiedSingles() + record.getOccupiedDoubles();
        int movedOut = record.getResidentsMovedOut();
        String utilityUsage = String.format("%.0f kWh", record.getUtilityUsage());
        monthlyTableModel.addRow(new Object[]{monthNum, aas, residents, occupied, movedOut, utilityUsage});
    }

    public void displayResults(SimulationResults results, int capacity) {
        summaryMetricLabels.get(0).setText(String.valueOf(results.getOccupiedSingles() + results.getOccupiedDoubles()));
        summaryMetricLabels.get(1).setText(String.valueOf(results.getTotalResidents()));
        summaryMetricLabels.get(2).setText(String.valueOf(results.getTotalMovedOut()));
        summaryMetricLabels.get(3).setText(String.format("%.2f", results.getAverageSatisfaction()));
        summaryMetricLabels.get(4).setText(String.format("%,.0f kWh", results.getTotalUtilityUsage()));
        summaryMetricLabels.get(5).setText(String.format("%.0f%%", ((double) results.getTotalResidents() / capacity) * 100));
        String highestTypeStr = String.join(", ", results.getHighestSatisfiedTypes());
        highestTypeLabel.setText(highestTypeStr);
        String lowestTypeStr = String.join(", ", results.getLowestSatisfiedTypes());
        lowestTypeLabel.setText(lowestTypeStr);
        for (MonthlyRecord record : results.getMonthlyRecords()) addMonthRow(record);
    }

    public void setFileStatus(String resultsFileName, boolean resultsSuccess, String settingsFile, boolean settingsSuccess) {
        resultsFileNameArea.setText(resultsFileName);
        resultsFileStatusLabel.setText(resultsSuccess ? "Results File Written" : "Error Writing File");
        styleFileNotice(resultsFileNotice, resultsFileStatusLabel,
                resultsFileNameArea, resultsSuccess);
        settingsFileNameArea.setText(settingsFile);
        settingsFileStatusLabel.setText(settingsSuccess ? "Results File Written" : "Error Writing File");
        styleFileNotice(settingsFileNotice, settingsFileStatusLabel,
                settingsFileNameArea, settingsSuccess);
    }
}