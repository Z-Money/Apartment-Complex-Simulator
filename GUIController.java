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

        mainWindow.addScreen("startPanel", startPanel);
        mainWindow.addScreen("setupPanel", setupPanel);

        mainWindow.showScreen("startPanel");
        mainWindow.setVisible(true);
    }

    public void showSetupScreen() {
        // TODO: Display setup screen
    }

    public void handleSetupModeSelected(String mode) {
        // TODO: Record setup mode and enable/disable manual entry
        selectedSetupMode = mode;
        switch (mode) {
            case "default" -> mainWindow.showScreen("defaultPanel");
            case "manual" -> mainWindow.showScreen("setupPanel");
            case "import" -> mainWindow.showScreen("importPanel");
        }
    }

    public void handleManualSetupSubmitted() {
        // TODO: Read form values, build settings, report validation error via dialog box
    }

    public void handleUseDefaults() {

    }

    public void handleChooseImportFile() {

    }

    public void handleImportFileChosen(String fileName) {

    }

    public void handleUseImportedConfiguration() {

    }

    public String promptInvalidSetting(String settingName) {
        // TODO: Get custom value or default value for invalid setting and return choice
        return "";
    }

    public void handleReviewEdit() {

    }

    public void handleStartSimulation() {
        // TODO: Start simulation after config is shown
    }

    public void handleMonthCompleted(MonthlyRecord record, int totalMonths) {

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
}
