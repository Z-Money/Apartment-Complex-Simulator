import java.util.Random;

public class LaundryMachines extends Amenity {
    private String[] possibleStatuses;

    public LaundryMachines(String name, double chance, double qualityContribution, double cost) {
        super(name, chance, qualityContribution, cost);
        possibleStatuses = new String[]{"Full-Operation", "Half-Operation", "Closed"};
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
