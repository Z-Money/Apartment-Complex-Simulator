public class ResidentType {
    private String typeName;
    private double noiseSensitivity;
    private double amenityWeight;
    private double costSensitivity;
    private double utilityUsageRate;
    private double moveOutSensitivity;
    private double generationChance;

    public ResidentType(String typeName, double noiseSensitivity, double amenityWeight, double costSensitivity, double utilityUsageRate, double moveOutSensitivity, double generationChance) {
        this.typeName = typeName;
        this.noiseSensitivity = noiseSensitivity;
        this.amenityWeight = amenityWeight;
        this.costSensitivity = costSensitivity;
        this.utilityUsageRate = utilityUsageRate;
        this.moveOutSensitivity = moveOutSensitivity;
        this.generationChance = generationChance;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public double getNoiseSensitivity() {
        return this.noiseSensitivity;
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
