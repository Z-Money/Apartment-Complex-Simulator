public class MonthlyRecord {
    private int monthNumber;
    private double averageSatisfaction;
    private int totalResidents;
    private int occupiedSingles;
    private int occupiedDoubles;
    private int residentsMovedOut;
    private double utilityUsage;

    public MonthlyRecord(int monthNumber, double averageSatisfaction, int totalResidents, int occupiedSingles, int occupiedDoubles, int residentsMovedOut, double utilityUsage) {
        this.monthNumber = monthNumber;
        this.averageSatisfaction = averageSatisfaction;
        this.totalResidents = totalResidents;
        this.occupiedSingles = occupiedSingles;
        this.occupiedDoubles = occupiedDoubles;
        this.residentsMovedOut = residentsMovedOut;
        this.utilityUsage = utilityUsage;
    }

    public int getMonthNumber() {
        return this.monthNumber;
    }

    public double getAverageSatisfaction() {
        return this.averageSatisfaction;
    }

    public int getTotalResidents() {
        return this.totalResidents;
    }

    public int getOccupiedSingles() {
        return this.occupiedSingles;
    }

    public int getOccupiedDoubles() {
        return this.occupiedDoubles;
    }

    public int getResidentsMovedOut() {
        return this.residentsMovedOut;
    }

    public double getUtilityUsage() {
        return this.utilityUsage;
    }
}
