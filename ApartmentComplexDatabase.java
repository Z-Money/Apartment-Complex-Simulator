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
            System.out.println(rd.getUtilityPrice("Water"));
        } catch (InvalidConfigurationException e) {
            // TODO: Connect to GUI
        }
    }

    public ReferenceData readReferenceData(String fileName) throws InvalidConfigurationException {
        try (Scanner scan = new Scanner(new File(fileName))) {
            if (!scan.hasNextLine()) throw new InvalidConfigurationException("Empty reference data file", "referenceDataFile");
            return handleLines(scan);
        } catch (FileNotFoundException e) {
            throw new InvalidConfigurationException("Missing file: " + fileName, "referenceDataFile");
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

    public ReferenceData handleLines(Scanner scan) throws InvalidConfigurationException {
        double baseCost = 0, baselineUsage = 0;
        String[] utilityNames = new String[2], amenityNames = new String[4];
        double[] utilityPrices = new double[2], amenityChances = new double[4], amenityCost = new double[4], amenityQualityContribution = new double[4];
        int[] floorLimits = new int[2], roomLimits = new int[2], defaultDistribution = new int[4];
        int maximumNameLength = 0, defaultFloors = 0, defaultRoomsPerFloor = 0, defaultApartmentType = 0, defaultOccupancyPercent = 0, row = 0, amenityIndex = 0, utilityIndex = 0;
        String defaultComplexName = "";
        ArrayList<ResidentType> residentTypes = new ArrayList<>();
        try {
            while (scan.hasNextLine()) {
                row++;
                String[] line = scan.nextLine().trim().split(",");
                switch (line[0]) {
                    case "BaseCost" -> baseCost = Double.parseDouble(line[1]);
                    case "BaselineUsage" -> baselineUsage = Double.parseDouble(line[1]);
                    case "Utility" -> utilityIndex = parseUtilityLine(line, utilityNames, utilityPrices, utilityIndex);
                    case "FloorLimits" -> {for (int i = 0; i < 2; i++) floorLimits[i] = Integer.parseInt(line[i+1]);}
                    case "RoomLimits" -> {for (int i = 0; i < 2; i++) roomLimits[i] = Integer.parseInt(line[i+1]);}
                    case "MaximumNameLength" -> maximumNameLength = Integer.parseInt(line[1]);
                    case "DefaultFloors" -> defaultFloors = Integer.parseInt(line[1]);
                    case "DefaultRoomsPerFloor" -> defaultRoomsPerFloor = Integer.parseInt(line[1]);
                    case "DefaultApartmentType" -> defaultApartmentType = Integer.parseInt(line[1]);
                    case "DefaultOccupancyPercent" -> defaultOccupancyPercent = Integer.parseInt(line[1]);
                    case "DefaultComplexName" -> defaultComplexName = line[1];
                    case "DefaultDistribution" -> {for (int i = 0; i < 4; i++) defaultDistribution[i] = Integer.parseInt(line[i+1]);}
                    case "ResidentType" -> residentTypes.add(parseResidentLine(line));
                    case "Amenity" -> {amenityNames[amenityIndex] = line[1]; amenityChances[amenityIndex] = Double.parseDouble(line[2]); amenityQualityContribution[amenityIndex] = Double.parseDouble(line[3]); amenityCost[amenityIndex] = Double.parseDouble(line[4]); amenityIndex++;
                    }
                    default -> throw new InvalidConfigurationException("Invalid " + line[0] + " value on line " + row, "referenceDataFile");
                }
            }
        }  catch (NumberFormatException e) {
            throw new InvalidConfigurationException("Invalid setting on row " + row + " in reference data file", "referenceDataFile");
        }
        return new ReferenceData(baseCost,baselineUsage,utilityNames,utilityPrices,residentTypes,amenityNames,
                amenityChances,amenityQualityContribution,amenityCost,floorLimits,roomLimits,maximumNameLength,
                defaultFloors,defaultRoomsPerFloor,defaultApartmentType,defaultOccupancyPercent,defaultComplexName,
                defaultDistribution);
    }

    public int parseUtilityLine(String[] line, String[] utilityNames, double[] utilityPrices, int index) throws InvalidConfigurationException {
        try {
            utilityNames[index] = line[1];
            utilityPrices[index] = Double.parseDouble(line[2]);
            return index + 1;
        } catch (NumberFormatException e) {
            throw new InvalidConfigurationException("Invalid setting: " + line[0], "referenceDataFile");
        }
    }
}
