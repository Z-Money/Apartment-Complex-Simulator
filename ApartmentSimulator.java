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
    //    private SimulationResults simResults;
//    private GUIController guiController;
    private Random random;

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
//            System.out.println(simSettings.getAmenityNames()[3] + "," + simSettings.getAmenityStatuses()[3]);
            ApartmentSimulator sim = new ApartmentSimulator();
            String[] stuff = sim.importSetup("settings.csv");
            sim.applySetup(stuff);
            sim.beginSimulation();
    }

    public ApartmentSimulator() {
        database = new ApartmentComplexDatabase();
        random = new Random();
        try {
            referenceData = database.readReferenceData("test.csv"); // Temp hardcode until GUI passes file path
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
//        guiController = new GUIController();
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

    public void applySetup(String[] setupLines) {
        simSettings = new SimulationSettings(referenceData);
        simSettings.applyImportedValues(setupLines);
    }

    public void beginSimulation() {
        String complexName = simSettings.getComplexName();
        int floors = simSettings.getNumberOfFloors();
        int rooms = simSettings.getRoomsPerFloor();
        buildComplex();
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
        for (int i = 0; i <amenityNames.length; i++) {
            Amenity amenity = referenceData.createAmenity(amenityNames[i], amenityStatuses[i]);
            complex.addAmenity(amenity);
        }
    }
}
