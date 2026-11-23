import javax.swing.*;
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
        this.app = app;
        mainWindow = new MainWindow("Apartment Complex Simulator");

        startPanel = new StartPanel(this);
        setupPanel = new SetupPanel(this);
        importPanel = new ImportPanel(this);
        reviewPanel = new ReviewPanel(this);
        simulationPanel = new SimulationPanel();
        resultsPanel = new ResultsPanel(this);

        mainWindow.addScreen("startPanel", startPanel);
        mainWindow.addScreen("setupPanel", setupPanel);
        mainWindow.addScreen("importPanel", importPanel);
        mainWindow.addScreen("reviewPanel", reviewPanel);
        mainWindow.addScreen("simulationPanel", simulationPanel);
        mainWindow.addScreen("resultsPanel", resultsPanel);
    }

    public void startGUI() {
        mainWindow.showScreen("startPanel");
        mainWindow.setVisible(true);
    }

    public void returnToStart() {
        mainWindow.showScreen("startPanel");
    }

    public void handleSetupModeSelected(String mode) {
        selectedSetupMode = mode;
        switch (mode) {
            case "default" -> handleUseDefaults();
            case "manual" -> mainWindow.showScreen("setupPanel");
            case "import" -> mainWindow.showScreen("importPanel");
        }
    }

    public void handleManualSetupSubmitted() {
        String[] formValues = setupPanel.getFormValues();
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
    }

    public void loadDefaultSetupValues() {
        app.useDefaultSetup();
        setupPanel.setFormValues(app.getDefaultFormValues());
        setupPanel.clearFieldErrors();
    }

    public void handleUseDefaults() {
        app.useDefaultSetup();
        if (setupPanel.getRetrievedSettings() == null || !Arrays.equals(app.getReviewText(), setupPanel.getRetrievedSettings())) {
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
            for (InvalidConfigurationException e : errors) errorStrList.add(e.getSettingName() + ": " + e.getMessage());
            if (setupPanel.getRetrievedSettings() == null || !Arrays.equals(app.getReviewText(), setupPanel.getRetrievedSettings())) {
                app.buildComplex();
                setupPanel.setRetrievedSettings(app.getReviewText());
            }
            importPanel.showPreview(app.getReviewText());
            importPanel.showValidationStatus(errorStrList);
        } catch (InvalidConfigurationException e) {
            ArrayList<String> error = new ArrayList<>();
            error.add(e.getMessage());
            importPanel.showValidationStatus(error);
        }
    }

    public void handleUseImportedConfiguration() {
        if (!importPanel.isFileLoaded()) {
            showError("General Error", "You cannot validate or proceed without first importing a file!");
            return;
        }
        String[] formValues = importPanel.getImportFormValues();
        formValues[5] = String.format("Apartment Layout,%s", app.getComplexLayout());
        ArrayList<InvalidConfigurationException> errors = app.applySetup(formValues);
        ArrayList<String> errorStrList = new ArrayList<>();
        for (InvalidConfigurationException e : errors) errorStrList.add(e.getSettingName() + ": " + e.getMessage());
        if (!errors.isEmpty()) {
            importPanel.showValidationStatus(errorStrList);
        } else {
            if (setupPanel.getRetrievedSettings() == null || !Arrays.equals(formValues, setupPanel.getRetrievedSettings())) {
                app.buildComplex();
                setupPanel.setRetrievedSettings(formValues);
            }
            importPanel.showValidationStatus(errorStrList);
            reviewPanel.displayConfiguration(app.getReviewText());
            mainWindow.showScreen("reviewPanel");
        }
    }

    public void handleReviewEdit() {
        switch (selectedSetupMode) {
            case "default" -> mainWindow.showScreen("startPanel");
            case "manual" -> mainWindow.showScreen("setupPanel");
            case "import" -> mainWindow.showScreen("importPanel");
        }
    }

    public void handleStartSimulation() {
        try {
            simulationPanel.fillApartmentName(app.getComplexName());
            mainWindow.showScreen("simulationPanel");
            app.beginSimulation();
        } catch (CapacityExceededException | InvalidConfigurationException e) {
            showError("Critical Error", e.getMessage());
            handleExit();
        }
        app.runSimulation(); // AURA
    }

    public void handleMonthCompleted(MonthlyRecord record, int totalMonths, String[][] amenityInfo, int capacity, double rent, double operatingCost, double qualityScore) {
        simulationPanel.updateProgress(record.getMonthNumber(), totalMonths);
        simulationPanel.updateMetrics(record.getTotalResidents(), capacity, rent, record.getAverageSatisfaction());
        simulationPanel.updateAmenityStatuses(amenityInfo[0], amenityInfo[1]);
        simulationPanel.updateMonthlySummary(qualityScore, operatingCost, record.getResidentsMovedOut());
        simulationPanel.addMonthRow(record);
    }

    public void handleSimulationComplete(SimulationResults simResults, String settingFileName, boolean settingSuccess, String resultFileName, boolean resultSuccess) {
        resultsPanel.displayResults(simResults, app.calculateResidentCapacity());
        resultsPanel.setFileStatus(settingFileName, settingSuccess, resultFileName, resultSuccess);
        mainWindow.showScreen("resultsPanel");
    }

    public void handleExit() {
        mainWindow.dispose();
        System.exit(0);
    }

    public void showError(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public String getSelectedSetupMode() {
        return selectedSetupMode;
    }
}
