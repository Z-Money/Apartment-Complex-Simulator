public class GUIController {
    private ApartmentSimulator app;
    private MainWindow mainWindow;
    private SetupPanel setupPanel;
    private SimulationPanel simulationPanel;
    private ResultsPanel resultsPanel;
    private String selectedSetupMode;

    public GUIController(ApartmentSimulator app) {
        // TODO: Init window and three panels
    }

    public void showSetupScreen() {
        // TODO: Display setup screen
    }

    public void handleSetupModeSelected(String mode) {
        // TODO: Record setup mode and enable/disable manual entry
    }

    public void handleSetupSubmitted() {
        // TODO: Read form values, build settings, report validation error via dialog box
    }

    public void handleImportRequested() {
        // TODO: Import settings file from user
    }

    public void handleStartSimulation() {
        // TODO: Start simulation after config is shown
    }

    public void handleExit() {
        // TODO: Close the program
    }

    public String promptInvalidSetting(String settingName) {
        // TODO: Get custom value or default value for invalid setting and return choice
        return "";
    }

    public String chooseFile(boolean forSaving) {
        // TODO: Open file chooser to open or save files and return fileName
        return "";
    }

    public void showActiveConfiguration(String text) {
        // TODO: Display active config before first month
    }

    public void updateMonthProgress(MonthlyRecord record, int totalMonths) {
        // TODO: Update progress bar and add month's data to table
    }

    public void showResults(SimulationResults results) {
        // TODO: Switch to results screen and displays final results
    }

    public void showFileNotice(String fileName, boolean success) {
        // TODO: Tell user if file was written successfully
    }

    public void showError(String message) {
        // TODO: Displays error messages to user via dialog box
    }
}
