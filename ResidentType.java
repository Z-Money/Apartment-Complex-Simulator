public class ResidentType {
    private String typeName;
    private double noiseTolerance;
    private double amenityWeight;
    private double costSensitivity;
    private double utilityUsageRate;
    private double moveOutSensitivity;
    private double generationChance;

    public ResidentType(String typeName, double noiseTolerance, double amenityWeight, double costSensitivity, double utilityUsageRate, double moveOutSensitivity, double generationChance) {

    }

    public String getTypeName() {
        return this.typeName;
    }

    public double getNoiseTolerance() {
        return this.noiseTolerance;
    }

    public double getAmenityWeight() {
        return this.amenityWeight;
    }

    public double getCostSensitivity() {
        return this.costSensitivity;
    }

    public double getUtilityUsageRate() {
        return this.utilityUsageRate;
    }

    public double getMoveOutSensitivity() {
        return this.moveOutSensitivity;
    }

    public double getGenerationChance() {
        return this.generationChance;
    }
}
