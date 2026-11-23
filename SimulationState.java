import java.util.ArrayList;

public class SimulationState {
    private int currentMonth;
    private int totalMonths;
    private double firstMonthRent;
    private int totalMovedOut;
    private double cumulativeUtilityUsage;
    private ArrayList<MonthlyRecord> monthlyRecords;

    public SimulationState() {
        this.currentMonth = 1;
        this.totalMonths = 0;
        this.totalMovedOut = 0;
        this.cumulativeUtilityUsage = 0;
        this.monthlyRecords = new ArrayList<>();
    }

    public void recordFirstMonthRent(double rent) {
        firstMonthRent = rent;
    }

    public void addMovedOut(int count) {
        totalMovedOut += count;
    }

    public void addUtilityUsage(double amount) {
        cumulativeUtilityUsage += amount;
    }

    public void addMonthlyRecord(MonthlyRecord record) {
        monthlyRecords.add(record);
    }

    public void advanceMonth() {
        currentMonth += 1;
        totalMonths += 1;
    }

    public int getCurrentMonth() {
        return currentMonth;
    }

    public int getTotalMonths() {
        return totalMonths;
    }

    public double getFirstMonthRent() {
        return firstMonthRent;
    }

    public int getTotalMovedOut() {
        return totalMovedOut;
    }

    public double getCumulativeUtilityUsage() {
        return cumulativeUtilityUsage;
    }

    public ArrayList<MonthlyRecord> getMonthlyRecords() {
        return monthlyRecords;
    }
}
