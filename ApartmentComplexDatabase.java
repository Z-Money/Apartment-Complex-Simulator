import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ApartmentComplexDatabase {
    public ReferenceData readReferenceData(String fileName) throws FileNotFoundException {
        File referenceDataFile = new File(fileName);
        try {
            Scanner scan = new Scanner(referenceDataFile);
            if (!scan.hasNextLine()) throw new InvalidConfigurationException("Empty referenceData file", "referenceDataFile");
            else {
                double baseCost, baselineUsage;
                ArrayList<String> utilityNames = new ArrayList<>(), amenityNames = new ArrayList<>();
                ArrayList<Double> utilityPrices = new ArrayList<>(), amenityChances = new ArrayList<>(), amenityQualityContributions = new ArrayList<>(), amenityCost = new ArrayList<>();
                ArrayList<ResidentType> residentTypes;
                ArrayList<Integer> floorLimits = new ArrayList<>(), roomLimits = new ArrayList<>(), defaultDistribution = new ArrayList<>();
                int maximumNameLength, defaultFloors, defaultRoomsPerFloor, defaultApartmentType, defaultOccupancyPercent;
                String defaultComplexName;
                while (scan.hasNextLine()) {
                    String[] line = scan.nextLine().trim().split(",");
                    switch(line[0]) {
                        case "BaseCost" -> baseCost = Double.parseDouble(line[1]);
                        case "BaselineUsage" -> baselineUsage = Double.parseDouble(line[1]);
                        case "Utility" -> {utilityNames.add(line[1]); utilityPrices.add(Double.parseDouble(line[2]));}
                        case "FloorLimits" -> {floorLimits.add(Integer.parseInt(line[1])); floorLimits.add(Integer.parseInt(line[2]));}
                        default -> throw new InvalidConfigurationException("Invalid " + line[0], line[0]);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            // TODO: Do smth with the GUI
            return null;
        } catch (InvalidConfigurationException e) {
            // TODO: Do smth with the GUI
            return null;
        }
        return null; // TODO: Return referenceData
    }
}
