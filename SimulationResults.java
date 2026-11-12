import java.util.ArrayList;

public class SimulationResults {
    private String complexName;
    private int occupiedSingles;
    private int occupiedDoubles;
    private int totalResidents;
    private int totalMovedOut;
    private double averageSatisfaction;
    private double totalUtilityUsage;
    private ArrayList<String> highestSatisfiedTypes;
    private ArrayList<String> lowestSatisfiedTypes;
    private ArrayList<MonthlyRecord> monthlyRecords;

    public SimulationResults(String complexName) {
        this.complexName = complexName;
        highestSatisfiedTypes = new ArrayList<>();
        lowestSatisfiedTypes = new ArrayList<>();
        monthlyRecords = new ArrayList<>();
    }

    public void compile(ApartmentComplex complex, SimulationState simState,
                        ArrayList<ResidentType> residentTypes) {
        ArrayList<Apartment> apartments = complex.getApartments();
        for (Apartment a : apartments) {
            if (a.getCapacity() == 1 && a.isOccupied()) occupiedSingles++;
            else if (a.getCapacity() == 2 && a.isOccupied()) occupiedDoubles++;
        }
        totalResidents = complex.getAllResidents().size();
        totalMovedOut = simState.getTotalMovedOut();
        averageSatisfaction = calculateAverageSatisfaction();
        totalUtilityUsage = simState.getCumulativeUtilityUsage();
        ArrayList<Resident> residents = complex.getAllResidents();
        // TODO: Remove following comments
        highestSatisfiedTypes = getHighestSatisfaction(residents); // W slop
        lowestSatisfiedTypes = getLowestSatisfaction(residents); // More W slop
    }

    public ArrayList<String> getHighestSatisfaction(ArrayList<Resident> residents) {
        double highestSatisfaction = residents.getFirst().getCurrentSatisfaction();
        ArrayList<String> highestResidents = new ArrayList<>();
        for (Resident r : residents) {
            if (r.getCurrentSatisfaction() > highestSatisfaction) {
                highestSatisfaction = r.getCurrentSatisfaction();
            }
        }
        for (Resident r : residents) {
            if (r.getCurrentSatisfaction() == highestSatisfaction) {
                highestResidents.add(r.getResidentType().getTypeName());
            }
        }
        return highestResidents;
    }

    public ArrayList<String> getLowestSatisfaction(ArrayList<Resident> residents) {
        double lowestSatisfaction = residents.getFirst().getCurrentSatisfaction();
        ArrayList<String> lowestResidents = new ArrayList<>();
        for (Resident r : residents) {
            if (r.getCurrentSatisfaction() < lowestSatisfaction) {
                lowestSatisfaction = r.getCurrentSatisfaction();
            }
        }
        for (Resident r : residents) {
            if (r.getCurrentSatisfaction() == lowestSatisfaction) {
                lowestResidents.add(r.getResidentType().getTypeName());
            }
        }
        return lowestResidents;
    }

    public String toDisplayText() {
        // TODO: Format once I figure GUI out
        return "";
    }

    public ArrayList<String> toCsvLines() {
        // TODO: Yeah I genuinely don't know
        return null;
    }

    public String getComplexName() {
        return complexName;
    }

    public ArrayList<MonthlyRecord> getMonthlyRecords() {
        return monthlyRecords;
    }

    private double calculateAverageSatisfaction() {
        double totalAvgSatisfaction = 0.0;
        for (MonthlyRecord mr : monthlyRecords) totalAvgSatisfaction += mr.getAverageSatisfaction();
        return totalAvgSatisfaction / monthlyRecords.size();
    }

    private void determineExtremeTypes(ArrayList<Resident> residents, ArrayList<ResidentType> types) {
        double[] averageSatisfactions = new double[types.size()];
        int[] typeCounts = new int[types.size()];
        for (Resident r : residents) {
            for (int j = 0; j < types.size(); j++) {
                if (r.getResidentType().equals(types.get(j))) {
                    averageSatisfactions[j] += r.getCurrentSatisfaction();
                    typeCounts[j]++;
                    break;
                }
            }
        }
        for (int i = 0; i < averageSatisfactions.length; i++) {
            if (typeCounts[i] != 0) {
                averageSatisfactions[i] /= typeCounts[i];
            }
        }
        double highestSatisfaction = averageSatisfactions[0];
        double lowestSatisfaction = averageSatisfactions[0];
        for (double satisfaction : averageSatisfactions) {
            if (satisfaction > highestSatisfaction) highestSatisfaction = satisfaction;
            else if (satisfaction < lowestSatisfaction) lowestSatisfaction = satisfaction;
        }
        for (int i = 0; i < averageSatisfactions.length; i++) {
            if (averageSatisfactions[i] == highestSatisfaction) highestSatisfiedTypes.add(types.get(i).getTypeName());
            else if (averageSatisfactions[i] == lowestSatisfaction) lowestSatisfiedTypes.add(types.get(i).getTypeName());
        }
    }
}
