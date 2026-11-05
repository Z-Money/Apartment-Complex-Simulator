import java.util.Random;

public class Pool extends Amenity {
    private String[] possibleStatuses;

    public Pool(double chance, double qualityContribution, double cost) {
        super("Pool", chance, qualityContribution, cost);
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
