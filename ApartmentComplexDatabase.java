import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
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

    public boolean writeSettingsFile(String fileName, SimulationSettings settings) {
        boolean wroteFileSuccessfully;
        try {
            File newSettingsFile = new File(fileName);
            PrintWriter printer = new PrintWriter(newSettingsFile);
            printer.printf(getComplexInfo(settings));
            printer.printf(generateAmenityLines(settings));
            printer.printf(generateResidentDistributionLine(settings));
            printer.close();
            wroteFileSuccessfully = true;
        } catch (NullPointerException | FileNotFoundException e) {
            wroteFileSuccessfully = false; // TODO: Dialog box with GUI indicating error saving settings
        }
        return wroteFileSuccessfully;
    }

    public String generateAmenityLines(SimulationSettings settings) {
        StringBuilder amenityLine = new StringBuilder();
        String[] names = settings.getAmenityNames().toArray(new String[0]);
        String[] statuses = settings.getAmenityStatuses().toArray(new String[0]);
        for (int i = 0; i < names.length; i++) {
            amenityLine.append(String.format("Amenity,%s,%s%n", names[i], statuses[i]));
        }
        return amenityLine.toString();
    }

    public String generateResidentDistributionLine(SimulationSettings settings) {
        StringBuilder residentDistributionLine = new StringBuilder();
        residentDistributionLine.append("Resident Distribution");
        int[] residentDistributions = settings.getResidentDistributions();
        for (int residentDistribution : residentDistributions) {
            residentDistributionLine.append(String.format(",%d", residentDistribution));
        }
        return residentDistributionLine.toString();
    }

    private String getComplexInfo(SimulationSettings settings) {
        return String.format("Complex Name,%s%n", settings.getComplexName()) +
                String.format("Number of Floors,%d%n", settings.getNumberOfFloors()) +
                String.format("Rooms per Floor,%d%n", settings.getRoomsPerFloor()) +
                String.format("Apartment Type,%d%n", settings.getApartmentType()) +
                String.format("Initial Number of Residents,%d%n", settings.getInitialResidentCount()) +
                String.format("Apartment Layout,%s%n", settings.getBuildingLayout());
    }

    public boolean writeResultsFile(String fileName, SimulationSettings settings, SimulationResults results) {
        boolean wroteFileSuccessfully;
        try {
            File resultsFile = new File(fileName);
            PrintWriter printer = new PrintWriter(resultsFile);
            printer.printf(getComplexInfo(settings));
            for (String s : results.toCsvLines()) {
                printer.printf(s);
            }
            printer.close();
            wroteFileSuccessfully = true;
        } catch (NullPointerException | FileNotFoundException e) {
            wroteFileSuccessfully = false;
        }
        return wroteFileSuccessfully;
    }
}
