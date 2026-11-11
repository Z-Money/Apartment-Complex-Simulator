import java.util.ArrayList;

public class SimulationSettings {
    private ReferenceData referenceData;
    private String complexName;
    private int numberOfFloors;
    private int roomsPerFloor;
    private int apartmentType;
    private int initialResidentCount;
    private String[] amenityNames;
    private String[] amenityStatuses;
    private int[] residentDistributions;

    public SimulationSettings(ReferenceData referenceData) {
        this.referenceData = referenceData;
        complexName = "";
        numberOfFloors = 0;
        roomsPerFloor = 0;
        apartmentType = 0;
        initialResidentCount = 0;
        amenityNames = new String[4];
        amenityStatuses = new String[4];
        residentDistributions = new int[4];
    }

    public void loadDefaults() {
        complexName = referenceData.getDefaultComplexName();
        numberOfFloors = referenceData.getDefaultFloors();
        roomsPerFloor = referenceData.getDefaultRoomsPerFloor();
        apartmentType = referenceData.getDefaultApartmentType();
        residentDistributions = referenceData.getDefaultDistribution();
    }

    public int calculateDefaultResidentCount(int capacity) {
        return 0;
    }

    public int parseWholeNumber(String text, String settingName) {
        return 0;
    }

    public void setComplexName(String name) throws InvalidConfigurationException {
        if (!name.isBlank() && name.length() <= referenceData.getMaximumNameLength()) {
            complexName = name;
        } else {
            throw new InvalidConfigurationException("Invalid apartment complex name", "complexName");
        }
    }

    public void setNumberOfFloors(int floors) throws InvalidConfigurationException {
        int[] limits = referenceData.getFloorLimits();
        int min = limits[0], max = limits[1];
        if (min <= floors && floors <= max) {
            numberOfFloors = floors;
        } else {
            throw new InvalidConfigurationException("Invalid number of floors", "numberOfFloors");
        }
    }

    public void setRoomsPerFloor(int rooms) throws InvalidConfigurationException {
        int[] limits = referenceData.getRoomLimits();
        int min = limits[0], max = limits[1];
        if (min <= rooms && rooms <= max) {
            roomsPerFloor = rooms;
        } else {
            throw new InvalidConfigurationException("Invalid number of rooms per floor", "roomsPerFloor");
        }
    }

    public void setApartmentType(int type) throws InvalidConfigurationException {
        if (type == 1 || type == 2) {
            apartmentType = type;
        } else {
            throw new InvalidConfigurationException("Invalid apartment type", "apartmentType");
        }
    }

    public void setInitialResidentCount(int count, int capacity) throws InvalidConfigurationException {
        if (count >= 1 && count <= capacity) {
            initialResidentCount = count;
        } else {
            throw new InvalidConfigurationException("Invalid initial resident count", "initialResidentCount");
        }
    }

    public void setAmenityStatus(String name, String status) {
        String[] validAmenityNames = referenceData.getAmenityNames();
        for (int i = 0; i < validAmenityNames.length; i++) {
            if (name.equals(validAmenityNames[i])) {
                amenityNames[i] = name;
                amenityStatuses[i] = status;
            }
        }
    }

    public void setResidentDistributions(int[] percentages) throws InvalidConfigurationException {
        int total = 0;
        for (int num : percentages) {
            if (num >= 0 && num <= 100) {
                total += num;
            } else {
                throw new InvalidConfigurationException("Invalid resident distribution", "residentDistribution");
            }
        }
    }

    public ArrayList<String> applyImportedValues(String[] values) {
        ArrayList<String> invalidValues = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            try {
                String[] line = values[i].trim().split(",");
                switch (line[0]) {
                    case "Complex Name" -> setComplexName(line[1]);
                    case "Number of Floors" -> setNumberOfFloors(Integer.parseInt(line[1]));
                    case "Rooms per Floor" -> setRoomsPerFloor(Integer.parseInt(line[1]));
                    case "Apartment Type" -> setApartmentType(Integer.parseInt(line[1]));
                    case "Initial Number of Residents" -> setInitialResidentCount(Integer.parseInt(line[1]), 100);
                    case "Amenity" -> setAmenityStatus(line[1], line[2]);
                    case "Resident Distribution" ->
                            setResidentDistributions(new int[]{Integer.parseInt(line[1]), Integer.parseInt(line[2]), Integer.parseInt(line[3]), Integer.parseInt(line[4])});
                    default -> throw new InvalidConfigurationException("Invalid setting on row " + i, "settingsFile");
                }
            } catch (NumberFormatException e) {
                continue;
            } catch (InvalidConfigurationException e) {
                invalidValues.add(values[i]);
            }
        }
        return invalidValues;
    }

    public String[] toKeyValueArray() {
        return null;
    }

    public boolean isComplete() {
        return false;
    }

    public String getComplexName() {
        return complexName;
    }

    public int getNumberOfFloors() {
        return numberOfFloors;
    }

    public int getRoomsPerFloor() {
        return roomsPerFloor;
    }

    public int getApartmentType() {
        return apartmentType;
    }

    public int getInitialResidentCount() {
        return initialResidentCount;
    }

    public String[] getAmenityNames() {
        return amenityNames;
    }

    public String[] getAmenityStatuses() {
        return amenityStatuses;
    }

    public int[] getResidentDistributions() {
        return residentDistributions;
    }
}
