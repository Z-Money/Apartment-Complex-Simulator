import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class ResultsPanel extends JPanel {
    private GUIController controller;
    private JTextArea configurationArea;
    private ArrayList<JLabel> summaryMetricLabels;
    private JTable monthlyTable;
    private DefaultTableModel monthlyTableModel;
    private JLabel highestTypeLabel;
    private JLabel lowestTypeLabel;
    private JButton startOverButton;
    private JButton exitButton;

    public ResultsPanel(GUIController controller) {

    }

    public void displayConfiguration(String text) {

    }

    public void displayResults(SimulationResults results, String[] utilityNames) {

    }

    public void setFileStatus(String resultsFileName, boolean resultsSuccess, String settingsFile, boolean settingsSuccess) {

    }

    public void buildLayout() {

    }
}
