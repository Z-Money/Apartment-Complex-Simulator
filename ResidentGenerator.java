import java.util.ArrayList;
import java.util.Random;

public class ResidentGenerator {
    private ArrayList<ResidentType> residentTypes;
    private int nextResidentId;

    public ResidentGenerator(ArrayList<ResidentType> residentTypes) {
        this.residentTypes = residentTypes;
        nextResidentId = 1;
    }

    public ArrayList<Resident> generateInitialResidents(int count, int[] distribution) {
        int[] distributionCounts = new int[distribution.length];
        int total = 0;
        for (int i = 0; i < distribution.length; i++) {
            distributionCounts[i] = (int) (distribution[i] / 100.0 * count);
            total += distributionCounts[i];
        }
        if (total < count) {
            int biggestDistributionIndex = getBiggestGroupIndex(distribution);
            distributionCounts[biggestDistributionIndex] += count - total;
        }
        ArrayList<Resident> initialResidents = new ArrayList<>();
        for (int i = 0; i < distributionCounts.length; i++) {
            for (int j = 0; j < distributionCounts[i]; j++) {
                initialResidents.add(new Resident(nextResidentId, residentTypes.get(i)));
                nextResidentId++;
            }
        }
        return initialResidents;
    }

    public int getBiggestGroupIndex(int[] distribution) {
        int max = 0, maxIndex = 0;
        for (int i = 0; i < distribution.length; i++) {
            if (distribution[i] > max) {
                max = distribution[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    public double[] calculateTypeWeights(double apartmentQualityScore, double rent, double firstMonthRent) {
        double rentChange = 0;
        if (firstMonthRent > 0) {
            rentChange = rent / firstMonthRent - 1.0;
        }
        double[] weights = new double[residentTypes.size()];
        for (int i = 0; i < residentTypes.size(); i++) {
            ResidentType type = residentTypes.get(i);
            double adjustment = 1.0 + (type.getAmenityWeight() * apartmentQualityScore) - (type.getCostSensitivity() * rentChange);
            weights[i] = type.getGenerationChance() * adjustment;
        }
        return weights;
    }

    public double[] normalizeWeights(double[] weights) {
        double[] probabilities = new double[weights.length];
        double total = 0;
        for (int i = 0; i < weights.length; i++) {
            probabilities[i] = Math.max(0, weights[i]);
            total += probabilities[i];
        }
        if (total == 0) {
            for (int i = 0; i < probabilities.length; i++) {
                probabilities[i] = residentTypes.get(i).getGenerationChance();
                total += probabilities[i];
            }
        }
        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] /= total;
        }
        return probabilities;
    }

    public ResidentType selectResidentType(double[] probabilities, Random random) {
        double roll = random.nextDouble();
        double total = 0.0;
        for (int i = 0; i < probabilities.length; i++) {
            total += probabilities[i];
            if (roll < total) return residentTypes.get(i);
        }
        return residentTypes.getLast();
    }

    public ArrayList<Resident> generateMoveIns(int vacantCount, double apartmentQualityScore, double rent, double firstMonthRent, Random random) {
        ArrayList<Resident> newResidents = new ArrayList<>();
        double[] probabilities = normalizeWeights(calculateTypeWeights(apartmentQualityScore, rent, firstMonthRent));
        for (int i = 0; i < vacantCount; i++) {
            ResidentType type = selectResidentType(probabilities, random);
            newResidents.add(new Resident(nextResidentId, type));
            nextResidentId++;
        }
        return newResidents;
    }
}
