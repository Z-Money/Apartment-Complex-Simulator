import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ApartmentComplexDatabase {

    public ReferenceData readReferenceData(String fileName) throws InvalidConfigurationException {
        try (Scanner scan = new Scanner(new File(fileName))) {
            if (!scan.hasNextLine())
                throw new InvalidConfigurationException("Empty reference data file", "referenceDataFile");
            return handleLines(scan);
        } catch (FileNotFoundException e) {
            throw new InvalidConfigurationException("Error opening file: " + fileName, "referenceDataFile");
        }
    }

    public ReferenceData handleLines(Scanner scan) throws InvalidConfigurationException {
        int row = 0, amenityIndex = 0, utilityIndex = 0;
        ReferenceData referenceData = new ReferenceData();
        try {
            while (scan.hasNextLine()) {
                row++;
                String[] line = scan.nextLine().trim().split(",");
                referenceData.loadFromLines(line, amenityIndex, utilityIndex);
                if (line[0].equals("Utility")) utilityIndex++;
                if (line[0].equals("Amenity")) amenityIndex++;
            }
            if (utilityIndex != 2 || amenityIndex != 4 || referenceData.getResidentTypes().size() != 4)
                throw new InvalidConfigurationException("Reference data is missing required utility, amenity, or resident type lines", "referenceDataFile");
            return referenceData;
        } catch (NumberFormatException e) {
            throw new InvalidConfigurationException("Invalid setting on row " + row + " in reference data file", "referenceDataFile");
            // TODO: Link to GUI at some point soon
        }
    }

    public String[] readSettingsFile(String fileName) throws InvalidConfigurationException {
        try (Scanner scan = new Scanner(new File(fileName))) {
            if (!scan.hasNextLine()) throw new InvalidConfigurationException("Empty settings file", "settingsFile");
            ArrayList<String> lines = new ArrayList<>();
            while (scan.hasNextLine()) {
                lines.add(scan.nextLine());
            }
            return lines.toArray(new String[0]);
        } catch (FileNotFoundException e) {
            throw new InvalidConfigurationException("Error opening file: " + fileName, "settingsFile");
        }
    }
}
