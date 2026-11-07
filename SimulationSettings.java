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
        if (min <= floors && floors >= max) {
            numberOfFloors = floors;
        } else {
            throw new InvalidConfigurationException("Invalid number of floors", "numberOfFloors");
        }
    }

    public void setRoomsPerFloor(int rooms) throws InvalidConfigurationException {
        int[] limits = referenceData.getRoomLimits();
        int min = limits[0], max = limits[1];
        if (min <= rooms && rooms >= max) {
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

    public void setAmenityStatuses(String[] names, String[] statuses) {

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
        for (int i = 0; i < values.length - 1; i++) {
            try {
                if (i == 0) setComplexName(values[0]);
                if (i == 1) setNumberOfFloors(Integer.parseInt(values[1]));
                if (i == 2) setRoomsPerFloor(Integer.parseInt(values[2]));
                if (i == 3) setApartmentType(Integer.parseInt(values[3]));
                if (i == 4) setInitialResidentCount(Integer.parseInt(values[4]), Integer.parseInt(values[5]));
                if (i == 6) return null;

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
