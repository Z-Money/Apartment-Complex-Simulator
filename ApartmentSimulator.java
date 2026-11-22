import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class ApartmentSimulator {
    private ReferenceData referenceData;
    private ApartmentComplexDatabase database;
    private SimulationSettings simSettings;
    private ApartmentComplex complex;
    private ResidentGenerator residentGenerator;
    private SimulationState simState;
    private SimulationResults simResults;
    private GUIController guiController;
    private Random random;

    public ApartmentSimulator() {
        guiController = new GUIController(this);
        database = new ApartmentComplexDatabase();
        random = new Random();
        try {
            referenceData = database.readReferenceData("referenceData.csv"); // Temp hardcode until GUI passes file path
        } catch (InvalidConfigurationException e) {
            guiController.showError("Reference Data Error", String.format("Critical error. %s Program will now end.", e.getMessage()));
            guiController.handleExit();
        }
        simSettings = new SimulationSettings(referenceData);
        residentGenerator = new ResidentGenerator(referenceData.getResidentTypes());
    }

    public static void main(String[] args) {
        try {
            System.setProperty("flatlaf.useNativeLibrary", "false");
            FlatLightLaf.setup();
            SwingUtilities.invokeLater(() -> {
                try {
                    ApartmentSimulator sim = new ApartmentSimulator();
                    sim.runSimulator();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "The simulation failed to initialize:\n" + e.getMessage(), "Startup Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "The simulation failed to initialize:\n" + e.getMessage(), "Startup Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void runSimulator() {
        guiController.startGUI();
    }

    public void useDefaultSetup() {
        simSettings.loadDefaults();
    }

    public String[] getDefaultFormValues() {
        return simSettings.getDefaultFormValues();
    }

    public String[] getReviewText() {
        String[] simValues = simSettings.toDisplayText();
        simValues[10] = String.valueOf(complex.getApartments().size());
        int capacity = 0;
        for (Apartment apartment : complex.getApartments()) {
            capacity += apartment.getCapacity();
        }
        simValues[11] = String.valueOf(capacity);
        return simValues;
    }

    public String[] importSetup(String fileName) throws InvalidConfigurationException {
        return database.readSettingsFile(fileName);
    }

    public int calculateResidentCapacity() {
        int capacity = 0;
        for (Apartment apartment : complex.getApartments()) {
            capacity += apartment.getCapacity();
        }
        return capacity;
    }

    public ArrayList<InvalidConfigurationException> applySetup(String[] setupLines) {
        SimulationSettings candidate = new SimulationSettings(referenceData);
        ArrayList<InvalidConfigurationException> invalidSettings = candidate.applyImportedValues(setupLines);
        if (invalidSettings.isEmpty()) simSettings = candidate;
        return invalidSettings;
    }

    public ArrayList<InvalidConfigurationException> applyImportSetup(String[] setupLines) {
        SimulationSettings candidate = new SimulationSettings(referenceData);
        ArrayList<InvalidConfigurationException> invalidSettings = candidate.applyImportedValues(setupLines);
        for (InvalidConfigurationException e : candidate.checkMissingValues(setupLines)) invalidSettings.add(e);
        simSettings = candidate;
        return invalidSettings;
    }

    public void beginSimulation() throws CapacityExceededException, InvalidConfigurationException {
        simState = new SimulationState();
        simResults = new SimulationResults(complex.getComplexName());
        int initialResidentCount;
        if (simSettings.getInitialResidentCount() == -1) {
            initialResidentCount = simSettings.calculateResidentCount(simSettings.getInitialOccupancyPercentage(), calculateResidentCapacity());
            simSettings.setInitialResidentCount(initialResidentCount);
        } else initialResidentCount = simSettings.getInitialResidentCount();
        ArrayList<Resident> initialResidents = residentGenerator.generateInitialResidents(initialResidentCount, simSettings.getResidentDistributions());
        try {
            for (Resident resident : initialResidents) complex.placeResident(resident, random);
        } catch (CapacityExceededException e) {
            guiController.showError("Capacity Error", e.getMessage());
            guiController.handleExit();
        }
    }

    public void runSimulation() {
        Thread simulationThread = new Thread(() -> {
            for (int i = 0; i < 12; i++) {
                try {
                    sleep(4000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    guiController.showError("Simulation Interruption", "The simulation was interrupted while running. Please run the program again.");
                }
                runMonth();
                simState.advanceMonth();
            }
            try {
                sleep(4000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                guiController.showError("Simulation Interruption", "The simulation was interrupted while running. Please run the program again.");
            }
            finishSimulation();
        });
        simulationThread.start();
    }

    public void finishSimulation() {
        String resultsFileName = formatFileName("results");
        String settingsFileName = formatFileName("settings");
        simResults.compile(complex, simState, referenceData.getResidentTypes());
        boolean settingsSuccess = false, resultsSuccess = false;
        try {
            settingsSuccess = database.writeSettingsFile(settingsFileName, simSettings);
            if (!settingsSuccess) throw new IOException("Error writing settings file");
        } catch (IOException e) {
            guiController.showError("File Writing Error", e.getMessage());
        }
        try {
            resultsSuccess = database.writeResultsFile(resultsFileName, simSettings, simResults);
            if (!resultsSuccess) throw new IOException("Error writing  results file");
        } catch (IOException e) {
            guiController.showError("File Writing Error", e.getMessage());
        }
        guiController.handleSimulationComplete(simResults, settingsFileName, settingsSuccess, resultsFileName, resultsSuccess);
    }

    public void buildComplex() {
        String complexName = simSettings.getComplexName();
        int floors = simSettings.getNumberOfFloors();
        int rooms = simSettings.getRoomsPerFloor();
        complex = new ApartmentComplex(complexName, floors, rooms);
        int apartmentType = simSettings.getApartmentType();
        if (simSettings.getBuildingLayout().isBlank() || !guiController.getSelectedSetupMode().equals("import")) {
            complex.buildLayout(apartmentType, random);
            simSettings.setBuildingLayout(complex.getLayoutList());
        } else complex.setLayout(simSettings.getBuildingLayout());
        String[] amenityNames = simSettings.getAmenityNames().toArray(new String[0]);
        String[] amenityStatuses = simSettings.getAmenityStatuses().toArray(new String[0]);
        for (int i = 0; i < amenityNames.length; i++) {
            try {
                Amenity amenity = referenceData.createAmenity(amenityNames[i], amenityStatuses[i]);
                complex.addAmenity(amenity);
            } catch (InvalidConfigurationException e) {
                guiController.showError("Complex Construction Error", e.getMessage());
                guiController.returnToStart();
            }
        }
    }

    private void runMonth() {
        evaluateAmenities();
        double qualityScore = calculateQualityScore();
        double operatingCost = calculateOperatingCost();
        double rent = calculateRentPerResident(operatingCost);
        if (simState.getCurrentMonth() == 1) simState.recordFirstMonthRent(rent);
        complex.updateNoiseLevels();
        double averageSatisfaction = evaluateResidents(qualityScore, rent);
        int moveOuts = processMoveOuts(averageSatisfaction);
        try {
            processMoveIns(qualityScore, rent);
        } catch (CapacityExceededException e) {
            guiController.showError("Capacity Error", e.getMessage());
            guiController.handleExit();
        }
        double utilityUsage = 0.0;
        for (Resident resident : complex.getAllResidents())
            utilityUsage += resident.calculateUtilityUsage(referenceData.getBaselineUsage());
        MonthlyRecord record = recordMonth(averageSatisfaction, moveOuts, utilityUsage);
        int totalMonths = simState.getTotalMonths();
        String[] amenityNames = new String[complex.getAmenities().size()];
        String[] amenityStatuses = new String[complex.getAmenities().size()];
        for (int i = 0; i < complex.getAmenities().size(); i++) {
            amenityNames[i] = complex.getAmenities().get(i).getName();
            amenityStatuses[i] = complex.getAmenities().get(i).getCurrentStatus();
        }
        String[][] amenityInfo = new String[][]{amenityNames, amenityStatuses};
        int capacity = calculateResidentCapacity();
        SwingUtilities.invokeLater(() -> {
            guiController.handleMonthCompleted(record, totalMonths, amenityInfo, capacity, rent, operatingCost);
        });
    }

    private void evaluateAmenities() {
        for (Amenity amenity : complex.getAmenities()) {
            amenity.evaluateMonthlyStatus(random);
        }
    }

    private double calculateQualityScore() {
        double qualityScore = 0.0;
        for (Amenity amenity : complex.getAmenities()) {
            qualityScore += amenity.calculateQualityScore();
        }
        return qualityScore;
    }

    private double calculateOperatingCost() {
        double operatingCost = 0.0;
        operatingCost += referenceData.getBaseCost();
        for (Amenity amenity : complex.getAmenities()) {
            int residentCount = complex.getAllResidents().size(), apartmentCount = complex.getNumberOfFloors() * complex.getRoomsPerFloor();
            operatingCost += amenity.calculateMonthlyCost(residentCount, apartmentCount);
        }
        for (Resident resident : complex.getAllResidents()) {
            operatingCost += resident.calculateUtilityUsage(referenceData.getBaselineUsage());
        }
        return operatingCost;
    }

    private double calculateRentPerResident(double operatingCost) {
        double rentPerResident = 0.0;
        if (!complex.getAllResidents().isEmpty()) {
            rentPerResident = calculateOperatingCost() / complex.getAllResidents().size();
        }
        return rentPerResident;
    }

    private double evaluateResidents(double apartmentQualityScore, double rent) {
        double totalSatisfaction = 0.0, averageSatisfaction = 0.0;
        ArrayList<Resident> residents = complex.getAllResidents();
        for (Resident resident : residents) {
            totalSatisfaction += resident.calculateSatisfaction(apartmentQualityScore, rent, simState.getFirstMonthRent());
        }
        if (!residents.isEmpty()) averageSatisfaction = totalSatisfaction / residents.size();
        return averageSatisfaction;
    }

    private int processMoveOuts(double averageSatisfaction) {
        ArrayList<Resident> moveOuts = complex.processMoveOuts(averageSatisfaction);
        return moveOuts.size();
    }

    private void processMoveIns(double apartmentQualityScore, double rent) throws CapacityExceededException {
        int vacantCount = countVacantSpaces();
        vacantCount = random.nextInt(0, vacantCount + 1);
        ArrayList<Resident> moveIns = residentGenerator.generateMoveIns(vacantCount, apartmentQualityScore, rent, simState.getFirstMonthRent(), random);
        try {
            for (Resident resident : moveIns) complex.placeResident(resident, random);
        } catch (CapacityExceededException e) {
            guiController.showError("Capacity Error", e.getMessage());
            guiController.handleExit();
        }
    }

    private MonthlyRecord recordMonth(double averageSatisfaction, int movedOut, double utilityUsage) {
        int monthNumber = simState.getCurrentMonth();
        int totalResidents = complex.getAllResidents().size();
        int occupiedSingles = complex.countOccupiedApartments(1);
        int occupiedDoubles = complex.countOccupiedApartments(2);
        MonthlyRecord monthlyRecord = new MonthlyRecord(monthNumber, averageSatisfaction, totalResidents, occupiedSingles, occupiedDoubles, movedOut, utilityUsage);
        simState.addMonthlyRecord(monthlyRecord);
        simState.addMovedOut(movedOut);
        simState.addUtilityUsage(utilityUsage);
        return monthlyRecord;
    }

    private int countVacantSpaces() {
        int vacantSpaces = 0;
        for (Apartment apartment : complex.getApartments()) vacantSpaces += apartment.getVacantSpace();
        return vacantSpaces;
    }

    public String formatFileName(String fileName) {
        String complexName = complex.getComplexName().toLowerCase();
        String newFileName = fileName;
        for (String s : complexName.split(" ")) {
            newFileName += ("_" + s);
        }
        return newFileName + ".csv";
    }

    public String getComplexName() {
        return complex.getComplexName();
    }

    public void sleep(int duration) throws InterruptedException {
        Thread.sleep(duration);
    }

    public String getComplexLayout() {
        return complex.getLayoutList();
    }
}
