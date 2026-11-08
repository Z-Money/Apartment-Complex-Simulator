import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ApartmentComplexDatabase {

    public static void main(String[] args) {
        ApartmentComplexDatabase db = new ApartmentComplexDatabase();
        try {
            ReferenceData rd = db.readReferenceData("test.csv");
            System.out.println(rd.getDefaultComplexName());
            System.out.println(rd.getBaseCost());
            System.out.println(rd.getAmenityNames()[0]);
            System.out.println(rd.getResidentTypes().get(1).getTypeName());
            System.out.println(rd.getDefaultDistribution()[0]);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public ReferenceData readReferenceData(String fileName) throws FileNotFoundException {
        File referenceDataFile = new File(fileName);
        try {
            Scanner scan = new Scanner(referenceDataFile);
            if (!scan.hasNextLine()) throw new InvalidConfigurationException("Empty referenceData file", "referenceDataFile");
            else {
                double baseCost = 0, baselineUsage = 0;
                ArrayList<String> utilityNames = new ArrayList<>(), amenityNames = new ArrayList<>();
                ArrayList<Double> utilityPrices = new ArrayList<>(), amenityChances = new ArrayList<>(), amenityQualityContributions = new ArrayList<>(), amenityCost = new ArrayList<>();
                ArrayList<ResidentType> residentTypes = new ArrayList<>();
                ArrayList<Integer> floorLimits = new ArrayList<>(), roomLimits = new ArrayList<>(), defaultDistribution = new ArrayList<>();
                int maximumNameLength = 0, defaultFloors = 0, defaultRoomsPerFloor = 0, defaultApartmentType = 0, defaultOccupancyPercent = 0;
                String defaultComplexName = "";
                while (scan.hasNextLine()) {
                    String[] line = scan.nextLine().trim().split(",");
                    switch(line[0]) {
                        case "BaseCost" -> baseCost = Double.parseDouble(line[1]);
                        case "BaselineUsage" -> baselineUsage = Double.parseDouble(line[1]);
                        case "Utility" -> {utilityNames.add(line[1]); utilityPrices.add(Double.parseDouble(line[2]));}
                        case "FloorLimits" -> {floorLimits.add(Integer.parseInt(line[1])); floorLimits.add(Integer.parseInt(line[2]));}
                        case "RoomLimits" -> {roomLimits.add(Integer.parseInt(line[1])); roomLimits.add(Integer.parseInt(line[2]));}
                        case "MaximumNameLength" -> maximumNameLength = Integer.parseInt(line[1]);
                        case "DefaultFloors" -> defaultFloors = Integer.parseInt(line[1]);
                        case "DefaultRoomsPerFloor" -> defaultRoomsPerFloor = Integer.parseInt(line[1]);
                        case "DefaultApartmentType" -> defaultApartmentType = Integer.parseInt(line[1]);
                        case "DefaultOccupancyPercent" -> defaultOccupancyPercent = Integer.parseInt(line[1]);
                        case "DefaultComplexName" -> defaultComplexName = line[1];
                        case "DefaultDistribution" -> {defaultDistribution.add(Integer.parseInt(line[1])); defaultDistribution.add(Integer.parseInt(line[2])); defaultDistribution.add(Integer.parseInt(line[3])); defaultDistribution.add(Integer.parseInt(line[4]));}
                        case "ResidentType" -> residentTypes.add(parseResidentLine(line));
                        case "Amenity" -> {amenityNames.add(line[1]); amenityChances.add(Double.parseDouble(line[2])); amenityQualityContributions.add(Double.parseDouble(line[3])); amenityCost.add(Double.parseDouble(line[4]));}
                        default -> throw new InvalidConfigurationException("Invalid " + line[0], line[0]);
                    }
                }
                String[] utilityNamesArray = new String[utilityNames.size()];
                for (int i = 0; i < utilityNames.size(); i++) {
                    utilityNamesArray[i] = utilityNames.get(i);
                }

                double[] utilityPricesArray = new double[utilityPrices.size()];
                for (int i = 0; i < utilityPrices.size(); i++) {
                    utilityPricesArray[i] = utilityPrices.get(i);
                }

                String[] amenityNamesArray = new String[amenityNames.size()];
                for (int i = 0; i < amenityNames.size(); i++) {
                    amenityNamesArray[i] = amenityNames.get(i);
                }

                double[] amenityChancesArray = new double[amenityChances.size()];
                for (int i = 0; i < amenityChances.size(); i++) {
                    amenityChancesArray[i] = amenityChances.get(i);
                }

                double[] amenityQualityContributionsArray = new double[amenityQualityContributions.size()];
                for (int i = 0; i < amenityQualityContributions.size(); i++) {
                    amenityQualityContributionsArray[i] = amenityQualityContributions.get(i);
                }

                double[] amenityCostArray = new double[amenityCost.size()];
                for (int i = 0; i < amenityCost.size(); i++) {
                    amenityCostArray[i] = amenityCost.get(i);
                }

                int[] floorLimitsArray = new int[floorLimits.size()];
                for (int i = 0; i < floorLimits.size(); i++) {
                    floorLimitsArray[i] = floorLimits.get(i);
                }

                int[] roomLimitsArray = new int[roomLimits.size()];
                for (int i = 0; i < roomLimits.size(); i++) {
                    roomLimitsArray[i] = roomLimits.get(i);
                }

                int[] defaultDistributionArray = new int[defaultDistribution.size()];
                for (int i = 0; i < defaultDistribution.size(); i++) {
                    defaultDistributionArray[i] = defaultDistribution.get(i);
                }

                return new ReferenceData(
                        baseCost,
                        baselineUsage,
                        utilityNamesArray,
                        utilityPricesArray,
                        residentTypes,
                        amenityNamesArray,
                        amenityChancesArray,
                        amenityQualityContributionsArray,
                        amenityCostArray,
                        floorLimitsArray,
                        roomLimitsArray,
                        maximumNameLength,
                        defaultFloors,
                        defaultRoomsPerFloor,
                        defaultApartmentType,
                        defaultOccupancyPercent,
                        defaultComplexName,
                        defaultDistributionArray
                );
            }
        } catch (FileNotFoundException e) {
            // TODO: Do smth with the GUI
            return null;
        } catch (InvalidConfigurationException e) {
            // TODO: Do smth with the GUI
            return null;
        }
    }

    public ResidentType parseResidentLine(String[] line) throws InvalidConfigurationException {
        try {
            String typeName = line[1];
            double noiseTolerance = Double.parseDouble(line[2]);
            double amenityWeight = Double.parseDouble(line[3]);
            double costSensitivity = Double.parseDouble(line[4]);
            double utilityUsageRate = Double.parseDouble(line[5]);
            double moveOutSensitivity = Double.parseDouble(line[6]);
            double generationChance = Double.parseDouble(line[7]);
            return new ResidentType(typeName, noiseTolerance, amenityWeight, costSensitivity, utilityUsageRate, moveOutSensitivity, generationChance);
        } catch (NumberFormatException e) {
            throw new InvalidConfigurationException("Invalid resident type value: " + e.getLocalizedMessage(), "residentType");
        }
    }
}
