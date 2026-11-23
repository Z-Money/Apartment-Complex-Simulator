import java.util.Random;

public abstract class Amenity {
    private String name;
    private double chanceAtClosing;
    private double qualityContribution;
    private double baseCost;
    private String currentStatus;

    public Amenity(String name, double chanceAtClosing, double qualityContribution, double baseCost) {
        this.name = name;
        this.chanceAtClosing = chanceAtClosing;
        this.qualityContribution = qualityContribution;
        this.baseCost = baseCost;
    }

    public void evaluateMonthlyStatus(Random random) {
        double randomChance = random.nextDouble();
        if (chanceAtClosing > randomChance) {
            currentStatus = selectDegradedStatus(random); // Doesn't use setCurrentStatus since status comes from list of possible statuses
        } else {
            currentStatus = "FULL_OPERATION"; // Doesn't use setCurrentStatus since possibleStatuses are constant and all have FULL_OPERATION
        }
    }

    public void setCurrentStatus(String status) throws InvalidConfigurationException {
        if (isStatusAllowed(status)) {
            currentStatus = status;
        } else {
            throw new InvalidConfigurationException("Invalid status " + status + " for " + name, "amenityStatus");
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

    public double calculateQualityScore() {
        return qualityContribution * calculateOperationalStatusFactor();
    }

    public double calculateMonthlyCost(int residentCount, int apartmentCount) {
        return calculateUsageCost(residentCount, apartmentCount) * calculateOperationalStatusFactor();
    }

    public String getName() {
        return name;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public double getBaseCost() {
        return baseCost;
    }

    public abstract String[] getPossibleStatuses();

    public abstract String selectDegradedStatus(Random random);

    public abstract double calculateOperationalStatusFactor();

    public abstract double calculateUsageCost(int residentCount, int apartmentCount);
}
