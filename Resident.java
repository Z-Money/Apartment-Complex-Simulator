public class Resident {
    private int residentId;
    private ResidentType residentType;
    private double currentSatisfaction;
    private double noiseLevel;

    public Resident(int residentId, ResidentType residentType) {

    }

    public double calculateSatisfaction(double averageQualityScore, double rent) {
        return 0;
    }

    public boolean shouldMoveOut(double averageSatisfaction) {
        return false;
    }

    public double calculateUtilityUsage(double baselineUsage) {
        return 0;
    }

    public void setNoiseLevel(double noiseLevel) {
        this.noiseLevel = noiseLevel;
    }

    public int getResidentId() {
        return this.residentId;
    }

    public ResidentType getResidentType() {
        return this.residentType;
    }

    public double getCurrentSatisfaction() {
        return this.currentSatisfaction;
    }
}
