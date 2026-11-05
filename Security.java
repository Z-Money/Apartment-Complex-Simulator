import java.util.Random;

public class Security extends Amenity {
    private String[] possibleStatuses;

    public Security(double chance, double qualityContribution, double cost) {
        super("Security", chance, qualityContribution, cost);
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
