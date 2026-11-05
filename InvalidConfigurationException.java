public class InvalidConfigurationException extends Exception {
    private String settingName;

    public InvalidConfigurationException(String message, String settingName) {
        super(message);
        this.settingName = settingName;
    }

    public String getSettingName() {
        return this.settingName;
    }
}
