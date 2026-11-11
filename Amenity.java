import java.util.Random;

public abstract class Amenity {
    private String name;
    private double chanceAtClosing;
    private double qualityContribution;
    private double monthlyCost;
    private String currentStatus;

    public Amenity(String name, double chanceAtClosing, double qualityContribution, double monthlyCost) {
        this.name = name;
        this.chanceAtClosing = chanceAtClosing;
        this.qualityContribution = qualityContribution;
        this.monthlyCost = monthlyCost;
    }

    public void evaluateMonthlyStatus(Random random) {

    }

    public void setCurrentStatus(String status) throws InvalidConfigurationException {
        if (isStatusAllowed(status)) {
            currentStatus = status;
        } else {
            throw new InvalidConfigurationException("Invalid amenity status", "amenityStatus");
        }
    }

    public boolean isStatusAllowed(String status) {
        boolean isAllowed = false;
        String[] possibleStatuses = getPossibleStatuses();
        for (String s : possibleStatuses) {
            if (status.equals(s)) {
                isAllowed = true;
                break;
            }
        }
        return isAllowed;
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

    public abstract double calculateQualityScore();

    public abstract String[] getPossibleStatuses();

    public abstract String selectDegradedStatus(Random random);

    public abstract double calculateOperationalStatusFactor();

    public abstract double calculateMonthlyCost(int residentCount, int apartmentCount);
}
