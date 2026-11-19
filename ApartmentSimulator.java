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
            System.out.println(sim.complex.getLayoutList());
            sim.runSimulation();
            String resultsFileName = sim.formatFileName("results");
            String settingsFileName = sim.formatFileName("settings");
            sim.finishSimulation(resultsFileName, settingsFileName);
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

    public int calculateResidentCapacity() {
        int capacity = 0;
        for (Apartment apartment : complex.getApartments()) {
            capacity += apartment.getCapacity();
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
        buildComplex();
        simState = new SimulationState();
        simResults = new SimulationResults(complex.getComplexName());
        int initialResidentCount;
        if (simSettings.getInitialOccupancyPercentage() == -1) initialResidentCount = simSettings.calculateResidentCount(simSettings.getInitialOccupancyPercentage(), calculateResidentCapacity());
        else initialResidentCount = simSettings.getInitialResidentCount();
        ArrayList<Resident> initialResidents = residentGenerator.generateInitialResidents(initialResidentCount, simSettings.getResidentDistributions());
        for (Resident resident : initialResidents) complex.placeResident(resident, random);
    }

    public void runSimulation() {
        for (int i = 0; i < 12; i++) {
            runMonth();
            simState.advanceMonth();
        }
    }

    public void finishSimulation(String resultsFileName, String settingsFileName) {
        simResults.compile(complex, simState, referenceData.getResidentTypes());
        database.writeSettingsFile(settingsFileName, simSettings);
        database.writeResultsFile(resultsFileName, simSettings, simResults);
    }

    public void buildComplex() {
        String complexName = simSettings.getComplexName();
        int floors = simSettings.getNumberOfFloors();
        int rooms = simSettings.getRoomsPerFloor();
        complex = new ApartmentComplex(complexName, floors, rooms);
        int apartmentType = simSettings.getApartmentType();
        if (simSettings.getBuildingLayout().isEmpty()) complex.buildLayout(apartmentType, random);
        else complex.setLayout(simSettings.getBuildingLayout());
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
        if (simState.getCurrentMonth() == 1) simState.recordFirstMonthRent(rent);
        complex.updateNoiseLevels();
        double averageSatisfaction = evaluateResidents(qualityScore, rent);
        int moveOuts = processMoveOuts(averageSatisfaction);
        try {
            processMoveIns(qualityScore, rent);
        } catch (CapacityExceededException e) {
            // TODO: Smth smth GUI
        }
        double utilityUsage = 0.0;
        for (Resident resident : complex.getAllResidents()) utilityUsage += resident.calculateUtilityUsage(referenceData.getBaselineUsage());
        recordMonth(averageSatisfaction, moveOuts, utilityUsage);
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
        ArrayList<Resident> moveIns = residentGenerator.generateMoveIns(vacantCount, apartmentQualityScore, rent, simState.getFirstMonthRent(), random);
        for (Resident resident : moveIns) complex.placeResident(resident, random);
    }

    private void recordMonth(double averageSatisfaction, int movedOut, double utilityUsage) {
        int monthNumber = simState.getCurrentMonth();
        int totalResidents = complex.getAllResidents().size();
        int occupiedSingles = complex.countOccupiedApartments(1);
        int occupiedDoubles = complex.countOccupiedApartments(2);
        MonthlyRecord monthlyRecord = new MonthlyRecord(monthNumber, averageSatisfaction, totalResidents, occupiedSingles, occupiedDoubles, movedOut, utilityUsage);
        simState.addMonthlyRecord(monthlyRecord);
        simState.addMovedOut(movedOut);
        simState.addUtilityUsage(utilityUsage);
    }

    private int countVacantSpaces() {
        int vacantSpaces = 0;
        for (Apartment apartment : complex.getApartments()) vacantSpaces += apartment.getVacantSpace();
        return vacantSpaces;
    }

    private String formatFileName(String fileName) {
        String complexName = complex.getComplexName().toLowerCase();
        String newFileName = fileName;
        for (String s : complexName.split(" ")) {
            newFileName += ("_" + s);
        }
        return newFileName + ".csv";
    }
}
