import java.util.Random;

public abstract class Amenity {
    private String name;
    private double chanceAtClosing;
    private double qualityContribution;
    private double monthlyCost;
    private String currentStatus;

    public Amenity(String name, double chanceAtClosing, double qualityContribution, double monthlyCost){

    }

    public void evaluateMonthlyStatus(Random random) {

    }

    public void setCurrentStatus(String status) {

    }

    public boolean isStatusAllowed(String status) {
        return false;
    }

    public double calculateQualityScore() {
        return 0;
    }

    public double calculateMonthlyCost() {
        return 0;
    }

    public String getName() {
        return this.name;
    }

    public String getCurrentStatus() {
        return this.currentStatus;
    }

    public double getChanceAtClosing() {
        return this.chanceAtClosing;
    }

    public abstract String[] getPossibleStatuses();
    public abstract String selectDegradedStatus(Random random);
    public abstract double calculateOperationalStatusFactor();
    public abstract double calculateMonthlyCost(int residentCount, int apartmentCount);
}
