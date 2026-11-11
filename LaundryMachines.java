import java.util.Random;

public class LaundryMachines extends Amenity {
    private String[] possibleStatuses;

    public LaundryMachines(String name, double chance, double qualityContribution, double cost) {
        super(name, chance, qualityContribution, cost);
        possibleStatuses = new String[]{"FULL_OPERATION", "HALF_OPERATION", "CLOSED"};
    }

    @Override
    public String[] getPossibleStatuses() {
        return this.possibleStatuses;
    }

    @Override
    public String selectDegradedStatus(Random random) {
        return "";
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
    public double calculateMonthlyCost(int residentCount, int apartmentCount) {
        return 0;
    }
}
