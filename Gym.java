import java.util.Random;

public class Gym extends Amenity {
    private String[] possibleStatuses;

    public Gym(double chance, double qualityContribution, double cost) {
        super("Gym", chance, qualityContribution, cost);
        possibleStatuses = new String[]{"Full-Operation", "Reduced Machines", "Closed"};
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
        return 0;
    }

    @Override
    public double calculateMonthlyCost(int residentCount, int apartmentCount) {
        return 0;
    }
}
