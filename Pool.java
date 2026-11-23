import java.util.Random;

public class Pool extends Amenity {
    private String[] possibleStatuses;

    public Pool(String name, double chance, double qualityContribution, double cost) {
        super(name, chance, qualityContribution, cost);
        possibleStatuses = new String[]{"FULL_OPERATION", "CLOSED"};
    }

    @Override
    public String[] getPossibleStatuses() {
        return this.possibleStatuses;
    }

    @Override
    public String selectDegradedStatus(Random random) {
        return possibleStatuses[1];
    }

    @Override
    public double calculateOperationalStatusFactor() {
        double operationalStatusFactor = 0.0;
        if (getCurrentStatus().equals("FULL_OPERATION")) {
            operationalStatusFactor = 1.0;
        } else {
            operationalStatusFactor = 0.0;
        }
        return operationalStatusFactor;
    }

    @Override
    public double calculateUsageCost(int residentCount, int apartmentCount) {
        return getBaseCost();
    }
}
