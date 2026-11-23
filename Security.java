import java.util.Random;

public class Security extends Amenity {
    private String[] possibleStatuses;
    private double costPerApartment;

    public Security(String name, double chance, double qualityContribution, double cost) {
        super(name, chance, qualityContribution, cost);
        possibleStatuses = new String[]{"FULL_OPERATION", "REDUCED_FORCE", "NOT_FUNCTIONING"};
        costPerApartment = 15;
    }

    @Override
    public String[] getPossibleStatuses() {
        return this.possibleStatuses;
    }

    @Override
    public String selectDegradedStatus(Random random) {
        return possibleStatuses[random.nextInt(1, possibleStatuses.length)];
    }

    @Override
    public double calculateOperationalStatusFactor() {
        double operationalStatusFactor = 0.0;
        if (getCurrentStatus().equals("FULL_OPERATION")) {
            operationalStatusFactor = 1.0;
        } else if (getCurrentStatus().equals("REDUCED_FORCE")) {
            operationalStatusFactor = 0.5;
        } else {
            operationalStatusFactor = 0.0;
        }
        return operationalStatusFactor;
    }

    @Override
    public double calculateUsageCost(int residentCount, int apartmentCount) {
        return costPerApartment * apartmentCount;
    }
}
