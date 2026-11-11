import java.util.Random;

public class LaundryMachines extends Amenity {
    private String[] possibleStatuses;
    private double costPerResident;

    public LaundryMachines(String name, double chance, double qualityContribution, double cost) {
        super(name, chance, qualityContribution, cost);
        possibleStatuses = new String[]{"FULL_OPERATION", "HALF_OPERATION", "CLOSED"};
        costPerResident = 9.8;
    }

    @Override
    public String[] getPossibleStatuses() {
        return this.possibleStatuses;
    }

    @Override
    public String selectDegradedStatus(Random random) {
        return possibleStatuses[1 + random.nextInt(0, possibleStatuses.length) - 1];
    }

    @Override
    public double calculateOperationalStatusFactor() {
        double operationalStatusFactor = 0.0;
        if (getCurrentStatus().equals("FULL_OPERATION")) { // FULL_OPERATION
            operationalStatusFactor = 1.0;
        } else if (getCurrentStatus().equals("HALF_OPERATION")) {
            operationalStatusFactor = 0.5;
        } else {
            operationalStatusFactor = 0.0;
        }
        return operationalStatusFactor;
    }

    @Override
    public double calculateUsageCost(int residentCount, int apartmentCount) {
        return getBaseCost() + (costPerResident * residentCount);
    }
}
