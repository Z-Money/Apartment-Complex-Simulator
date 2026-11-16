import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class SimulationPanel extends JPanel {
    private GUIController controller;
    private JLabel monthLabel;
    private JProgressBar progressBar;
    private JLabel residentsMetricLabel;
    private JLabel occupancyMetricLabel;
    private JLabel rentMetricLabel;
    private JLabel aasMetricLabel;
    private JTable monthlyTable;
    private DefaultTableModel monthlyTableModel;
    private ArrayList<JLabel> amenityStatusLabels;
    private JLabel aqsLabel;
    private JLabel operatingCostLabel;
    private JLabel movedOutLabel;

    public SimulationPanel(GUIController controller) {

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

    private void buildLayout() {

    }
}
