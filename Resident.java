public class Resident {
    private int residentId;
    private ResidentType residentType;
    private double currentSatisfaction;
    private double noiseLevel;

    public Resident(int residentId, ResidentType residentType) {
        this.residentId = residentId;
        this.residentType = residentType;
        currentSatisfaction = 0.0;
        noiseLevel = 0.0;
    }

    public double calculateSatisfaction(double averageQualityScore, double rent, double firstMonthRent) {
        double amenityScore = residentType.getAmenityWeight() * averageQualityScore;
        double noiseScore = residentType.getNoiseSensitivity() * noiseLevel;
        double relativeRentChange = firstMonthRent == 0 ? 0.0 : (rent / firstMonthRent) - 1.0;
        relativeRentChange = Math.max(-1.0, Math.min(1.0, relativeRentChange));
        double costScore = residentType.getCostSensitivity() * relativeRentChange;
        double satisfactionScore = amenityScore - noiseScore - costScore;
        currentSatisfaction = satisfactionScore;
        return satisfactionScore;
    }

    public boolean shouldMoveOut(double averageSatisfaction) {
        return currentSatisfaction < (averageSatisfaction * residentType.getMoveOutSensitivity());
    }

    public double calculateUtilityUsage(double baselineUsage) {
        return baselineUsage * residentType.getUtilityUsageRate();
    }

    public void setNoiseLevel(double noiseLevel) {
        this.noiseLevel = noiseLevel;
    }

    public ResidentType getResidentType() {
        return residentType;
    }

    public double getCurrentSatisfaction() {
        return currentSatisfaction;
    }

    public double calculateNoiseEmission() {
        return residentType.calculateNoiseEmission();
    }
}
