import java.util.ArrayList;
import java.util.Random;

public class ResidentGenerator {
    private ArrayList<ResidentType> residentTypes;
    private int nextResidentId;

    public ResidentGenerator(ArrayList<ResidentType> residentTypes) {

    }

    public ArrayList<ResidentType> generateInitialResidents(int count, int[] distribution) {
        return null;
    }

    public double[] calculateTypeWeights(double apartmentQualityScore, double rent, double firstMonthRent) {
        return null;
    }

    public double[] normalizeWeights(double[] weights) {
        return null;
    }

    public ResidentType selectResidentType(double[] probabilities, Random random) {
        return null;
    }

    public ArrayList<Resident> generateMoveIns(int vacantCount, double apartmentQualityScore, double rent, double firstMonthRent, Random random) {
        return null;
    }
}
