import java.util.ArrayList;
import java.util.Arrays;

public class GUIController {
    private ApartmentSimulator app;
    private MainWindow mainWindow;
    private StartPanel startPanel;
    private SetupPanel setupPanel;
    private ImportPanel importPanel;
    private ReviewPanel reviewPanel;
    private SimulationPanel simulationPanel;
    private ResultsPanel resultsPanel;
    private String selectedSetupMode;

    public GUIController(ApartmentSimulator app) {
        // TODO: Init window and three panels
        this.app = app;
        mainWindow = new MainWindow("Apartment Complex Simulator");

        startPanel = new StartPanel(this);
        setupPanel = new SetupPanel(this);
        importPanel = new ImportPanel(this);
        reviewPanel = new ReviewPanel(this);
        simulationPanel = new SimulationPanel(this);

        mainWindow.addScreen("startPanel", startPanel);
        mainWindow.addScreen("setupPanel", setupPanel);
        mainWindow.addScreen("importPanel", importPanel);
        mainWindow.addScreen("reviewPanel", reviewPanel);
        mainWindow.addScreen("simulationPanel", simulationPanel);
    }

    public void startGUI() {
        mainWindow.showScreen("startPanel");
        mainWindow.setVisible(true);
    }

    public void returnToStart() {
        mainWindow.showScreen("startPanel");
    }

    public void showSetupScreen() {
        // TODO: Display setup screen
    }

    public void handleSetupModeSelected(String mode) {
        // TODO: Record setup mode and enable/disable manual entry
        selectedSetupMode = mode;
        switch (mode) {
            case "default" -> handleUseDefaults();
            case "manual" -> mainWindow.showScreen("setupPanel");
            case "import" -> mainWindow.showScreen("importPanel");
        }
    }

    public void handleManualSetupSubmitted() {
        // TODO: Read form values, build settings, report validation error via dialog box
        String[] formValues = setupPanel.getFormValues();
//        try {
        ArrayList<InvalidConfigurationException> errors = app.applySetup(formValues);
        if (!errors.isEmpty()) {
            setupPanel.clearFieldErrors();
            for (InvalidConfigurationException error : errors) {
                setupPanel.showFieldErrors(error.getSettingName(), error.getMessage());
            }
        } else {
            if (setupPanel.getRetrievedSettings() == null || !Arrays.equals(formValues, setupPanel.getRetrievedSettings())) {
                app.buildComplex();
                setupPanel.setRetrievedSettings(formValues);
            }
            reviewPanel.displayConfiguration(app.getReviewText());
            mainWindow.showScreen("reviewPanel");
        }
//            app.beginSimulation();
//            app.runSimulation();
//            String resultsFileName = app.formatFileName("results");
//            String settingsFileName = app.formatFileName("settings");
//            app.finishSimulation(resultsFileName, settingsFileName);
//        } catch (InvalidConfigurationException e) {
//            throw new RuntimeException(e);
//        }
    }

    public void loadDefaultSetupValues() {
        app.useDefaultSetup();
        setupPanel.setFormValues(app.getDefaultFormValues());
        setupPanel.clearFieldErrors();
    }

    public void handleUseDefaults() {
        if (setupPanel.getRetrievedSettings() == null || !Arrays.equals(app.getReviewText(), setupPanel.getRetrievedSettings())) {
            app.useDefaultSetup();
            app.buildComplex();
            setupPanel.setRetrievedSettings(app.getReviewText());
        }
        reviewPanel.displayConfiguration(app.getReviewText());
        mainWindow.showScreen("reviewPanel");
    }

    public void handleImportFileChosen(String fileName) {
        try {
            String[] readValues = app.importSetup(fileName);
            ArrayList<InvalidConfigurationException> errors = app.applyImportSetup(readValues);
            ArrayList<String> errorStrList = new ArrayList<>();
            for (InvalidConfigurationException e : errors) errorStrList.add(e.getMessage());
            if (setupPanel.getRetrievedSettings() == null || !Arrays.equals(app.getReviewText(), setupPanel.getRetrievedSettings())) {
                app.buildComplex();
                setupPanel.setRetrievedSettings(app.getReviewText());
            }
            importPanel.showPreview(app.getReviewText());
            importPanel.showValidationStatus(errorStrList);
            //else {
            // Code to show review panel
                /*if (setupPanel.getRetrievedSettings() == null || !Arrays.equals(app.getReviewText(), setupPanel.getRetrievedSettings())) {
                    app.buildComplex();
                    setupPanel.setRetrievedSettings(app.getReviewText());
                }
                reviewPanel.displayConfiguration(app.getReviewText());
                mainWindow.showScreen("reviewPanel");*/

            //}
        } catch (InvalidConfigurationException e) {
            ArrayList<String> error = new ArrayList<>();
            error.add(e.getMessage());
            importPanel.showValidationStatus(error);
        }
    }

    public void handleUseImportedConfiguration() {
        String[] formValues = importPanel.getImportFormValues();
        ArrayList<InvalidConfigurationException> errors = app.applySetup(formValues);
        ArrayList<String> errorStrList = new ArrayList<>();
        for (InvalidConfigurationException e : errors) errorStrList.add(e.getMessage());
        if (!errors.isEmpty()) {
            importPanel.showValidationStatus(errorStrList);
        } else {
            if (setupPanel.getRetrievedSettings() == null || !Arrays.equals(formValues, setupPanel.getRetrievedSettings())) {
                app.buildComplex();
                setupPanel.setRetrievedSettings(formValues);
            }
            reviewPanel.displayConfiguration(app.getReviewText());
            mainWindow.showScreen("reviewPanel");
        }
    }

    public String promptInvalidSetting(String settingName) {
        // TODO: Get custom value or default value for invalid setting and return choice
        return "";
    }

    public void handleReviewEdit() {
        switch (selectedSetupMode) {
            case "default" -> mainWindow.showScreen("startPanel");
            case "manual" -> mainWindow.showScreen("setupPanel");
            case "import" -> mainWindow.showScreen("importPanel");
        }
    }

    public void handleStartSimulation() {
        // TODO: Start simulation after config is shown
        try {
            simulationPanel.fillApartmentName(app.getComplexName());
            mainWindow.showScreen("simulationPanel");
            app.beginSimulation();
        } catch (CapacityExceededException | InvalidConfigurationException e) {
            // TODO: Show dialog box reporting error and to restart the program
            System.out.println("I dont feel too good mr stark");
        }
        app.runSimulation(); // AURA
    }

    public void handleMonthCompleted(MonthlyRecord record, int totalMonths, String[][] amenityInfo, int capacity, double rent, double operatingCost) {
        simulationPanel.updateProgress(record.getMonthNumber(), totalMonths);
        simulationPanel.updateMetrics(record.getTotalResidents(), capacity, rent, record.getAverageSatisfaction());
        simulationPanel.updateAmenityStatuses(amenityInfo[0], amenityInfo[1]);
        simulationPanel.updateMonthlySummary(record.getAverageSatisfaction(), operatingCost, record.getResidentsMovedOut());
        simulationPanel.addMonthRow(record);
    }

    public void handleSimulationComplete(SimulationResults results) {

    }

    public void handleFileWriteResult(String resultsFileName, boolean resultsSuccess, String settingsFileName, boolean settingsSuccess) {

    }

    public void handleStartOver() {

    }

    public void handleExit() {
        // TODO: Close the program
    }

    public void showError(String message) {
        // TODO: Displays error messages to user via dialog box
    }

    public String getSelectedSetupMode() {
        return selectedSetupMode;
    }
}
