import com.formdev.flatlaf.FlatLightLaf;

import java.awt.FlowLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class ApartmentSimulator {
    private ReferenceData referenceData;
    private ApartmentComplexDatabase database;
    private SimulationSettings simSettings;
    private ApartmentComplex complex;
    private ResidentGenerator residentGenerator;
    private SimulationState simState;
    private SimulationResults simResults;
//    private GUIController guiController;
    private Random random;

    public ApartmentSimulator() {
        database = new ApartmentComplexDatabase();
        random = new Random();
        try {
            referenceData = database.readReferenceData("test.csv"); // Temp hardcode until GUI passes file path
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
        residentGenerator = new ResidentGenerator(referenceData.getResidentTypes());
//        guiController = new GUIController();
    }

    public static void main(String[] args) {
//        System.setProperty("flatlaf.useNativeLibrary", "false");
//        FlatLightLaf.setup();
//
//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("FlatLaf Styling");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setSize(400, 300);
//
//            // 1. Set the layout manager to FlowLayout
//            frame.setLayout(new FlowLayout());
//
//            JButton button = new JButton("Click Me!");
//            // 2. Use setPreferredSize instead of setSize
//            button.setPreferredSize(new Dimension(150, 50));
//            frame.add(button);
//
//            frame.setLocationRelativeTo(null);
//            frame.setVisible(true);
//        });

//            ReferenceData refData = db.readReferenceData("test.csv");
//            SimulationSettings simSettings = new SimulationSettings(refData);
//            ArrayList<String> invalidValues = simSettings.applyImportedValues(db.readSettingsFile("settings.csv"));
        ApartmentSimulator sim = new ApartmentSimulator();
        String[] stuff = sim.importSetup("settings.csv");
        try {
            sim.applySetup(stuff);
            sim.beginSimulation();
        } catch (InvalidConfigurationException | CapacityExceededException e) {
            throw new RuntimeException(e); // TODO: Connect to GUI
        }
    }

    public void runSimulator() {
        // TODO: Start GUI
    }

    public void useDefaultSetup() {
        try {
            referenceData = database.readReferenceData("test.csv");
            simSettings = new SimulationSettings(referenceData);
            simSettings.loadDefaults();
        } catch (InvalidConfigurationException e) {
            // TODO: Do stuff with GUI
        }
    }

    public String[] importSetup(String fileName) {
        try {
            return database.readSettingsFile(fileName);
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public int calculateResidentCapacity(int floors, int rooms, int apartmentType) {
        int capacity;
        if (apartmentType == 1) {
            capacity = rooms * floors;
        } else {
            // TODO: Get total num of singles/doubles and calc capacity using nums
            capacity = -1;
        }
        return capacity;
    }

    public void applySetup(String[] setupLines) throws InvalidConfigurationException {
        simSettings = new SimulationSettings(referenceData);
        ArrayList<String> invalidSettings = simSettings.applyImportedValues(setupLines);
        if (!invalidSettings.isEmpty()) {
            throw new InvalidConfigurationException("Invalid settings: " + invalidSettings.toString(), "settingsFile");
        }
    }

    public void beginSimulation() throws CapacityExceededException {
        String complexName = simSettings.getComplexName();
        int floors = simSettings.getNumberOfFloors();
        int rooms = simSettings.getRoomsPerFloor();
        buildComplex();
        ArrayList<Resident> initialResidents = residentGenerator.generateInitialResidents(simSettings.getInitialResidentCount(), simSettings.getResidentDistributions());
        for (Resident resident : initialResidents) complex.placeResident(resident, random);
    }

    public void runSimulation() {
        // TODO: Run 12 times
    }

    public void finishSimulation(String resultsFileName, String settingsFileName) {
        // TODO: Make sim run so I can finish sim
    }

    public void buildComplex() {
        String complexName = simSettings.getComplexName();
        int floors = simSettings.getNumberOfFloors();
        int rooms = simSettings.getRoomsPerFloor();
        complex = new ApartmentComplex(complexName, floors, rooms);
        int apartmentType = simSettings.getApartmentType();
        complex.buildLayout(apartmentType, random);
        String[] amenityNames = simSettings.getAmenityNames();
        String[] amenityStatuses = simSettings.getAmenityStatuses();
        for (int i = 0; i < amenityNames.length; i++) {
            try {
                Amenity amenity = referenceData.createAmenity(amenityNames[i], amenityStatuses[i]);
                complex.addAmenity(amenity);
            } catch (InvalidConfigurationException e) {
                throw new RuntimeException(e); // TODO: Prob should do smth about this once GUI exist
            }
        }
    }

    private void runMonth() {
        // TODO: Increase simState by 1 month, run monthly calculations and store in simResults
        evaluateAmenities();
        double qualityScore = calculateQualityScore();
        double operatingCost = calculateOperatingCost();
        double rent = calculateRentPerResident(operatingCost);
        double averageSatisfaction = evaluateResidents(qualityScore, rent);
        int moveOuts = processMoveOuts(averageSatisfaction);
        processMoveIns(qualityScore, rent);
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
            totalSatisfaction += resident.calculateSatisfaction(apartmentQualityScore, rent);
        }
        if (!residents.isEmpty()) averageSatisfaction = totalSatisfaction / residents.size();
        return averageSatisfaction;
    }

    private int processMoveOuts(double averageSatisfaction) {
        ArrayList<Resident> moveOuts = complex.processMoveOuts(averageSatisfaction);
        return moveOuts.size();
    }

    private void processMoveIns(double apartmentQualityScore, double rent) {
        int vacantCount = countVacantSpaces();
//        ResidentGenerator
    }

    private void recordMonth(double averageSatisfaction, int movedOut, double utilityUsage) {
        // TODO: Store month results in MonthlyRecord and update running totals
    }

    private int countVacantSpaces() {
        int vacantSpaces = 0;
        for (Apartment apartment : complex.getApartments()) vacantSpaces += apartment.getVacantSpace();
        return vacantSpaces;
    }
}
