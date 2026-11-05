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
        this.firstMonthRent = rent;
    }

    public void addMovedOut(int count) {
        this.totalMovedOut += count;
    }

    public void addUtilityUsage(double amount) {
        this.cumulativeUtilityUsage += amount;
    }

    public void addMonthlyRecord(MonthlyRecord record) {
        this.monthlyRecords.add(record);
    }

    public void advanceMonth() {
        this.currentMonth += 1;
        this.totalMonths += 1;
    }

    public boolean isSimulationComplete() {
        if (totalMonths == 12) {
            return true;
        } else {
            return false;
        }
    }

    public int getCurrentMonth() {
        return this.currentMonth;
    }

    public int getTotalMonths() {
        return this.totalMonths;
    }

    public double getFirstMonthRent() {
        return this.firstMonthRent;
    }

    public int getTotalMovedOut() {
        return this.totalMovedOut;
    }

    public double getCumulativeUtilityUsage() {
        return this.cumulativeUtilityUsage;
    }

    public ArrayList<MonthlyRecord> getMonthlyRecords() {
        return this.monthlyRecords;
    }

}
