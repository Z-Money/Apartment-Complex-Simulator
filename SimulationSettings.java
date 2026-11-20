import java.util.ArrayList;

public class SimulationSettings {
    private ReferenceData referenceData;
    private String complexName;
    private int numberOfFloors;
    private int roomsPerFloor;
    private int apartmentType;
    private int initialOccupancyPercentage;
    private int initialResidentCount;
    private String buildingLayout;
    private ArrayList<String> amenityNames;
    private ArrayList<String> amenityStatuses;
    private int[] residentDistributions;

    public SimulationSettings(ReferenceData referenceData) {
        this.referenceData = referenceData;
        complexName = "";
        numberOfFloors = 0;
        roomsPerFloor = 0;
        apartmentType = 0;
        initialOccupancyPercentage = 0;
        initialResidentCount = -1;
        buildingLayout = "";
        amenityNames = new ArrayList<>();
        amenityStatuses = new ArrayList<>();
        residentDistributions = new int[4];
    }

    public void loadDefaults() {
        complexName = referenceData.getDefaultComplexName();
        numberOfFloors = referenceData.getDefaultFloors();
        roomsPerFloor = referenceData.getDefaultRoomsPerFloor();
        apartmentType = referenceData.getDefaultApartmentType();
        initialOccupancyPercentage = referenceData.getDefaultOccupancyPercent();
        residentDistributions = referenceData.getDefaultDistribution();
        for (String amenityName : referenceData.getAmenityNames()) {
            amenityNames.add(amenityName);
            amenityStatuses.add("FULL_OPERATION");
        }
    }

    public int calculateResidentCount(int occupancyPercent, int capacity) {
        return (int) (capacity * ( (double) occupancyPercent / 100));
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

    public void setInitialOccupancyPercentage(int initialOccupancyPercentage) throws InvalidConfigurationException {
        if (initialOccupancyPercentage >= 0 && initialOccupancyPercentage <= 100) {
            this.initialOccupancyPercentage = initialOccupancyPercentage;
        } else {
            throw new InvalidConfigurationException("Invalid initial resident count", "initialOccupancyPercentage");
        }
    }

    public void setInitialResidentCount(int count) throws InvalidConfigurationException {
        if (count >= 1) {
            initialResidentCount = count;
        } else {
            throw new InvalidConfigurationException("Invalid initial resident count", "initialResidentCount");
        }
    }

    public void setBuildingLayout(String buildingLayout) {
        this.buildingLayout = buildingLayout;
    }

    public void setAmenityStatus(String name, String status) {
        String[] validAmenityNames = referenceData.getAmenityNames();
        for (int i = 0; i < validAmenityNames.length; i++) {
            if (name.equals(validAmenityNames[i])) {
                amenityNames.add(name);
                amenityStatuses.add(status);
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
        if (total != 100) throw new InvalidConfigurationException("Invalid resident distribution", "residentDistribution");
        residentDistributions = percentages;
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
                    case "Initial Number of Residents" -> setInitialResidentCount(Integer.parseInt(line[1]));
                    case "Occupancy Percentage" -> setInitialOccupancyPercentage(Integer.parseInt(line[1]));
                    case "Apartment Layout" -> setBuildingLayout(line[1]);
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

    public int getInitialOccupancyPercentage() {
        return initialOccupancyPercentage;
    }

    public int getInitialResidentCount() {
        return initialResidentCount;
    }

    public String getBuildingLayout() {
        return buildingLayout;
    }

    public ArrayList<String> getAmenityNames() {
        return amenityNames;
    }

    public ArrayList<String> getAmenityStatuses() {
        return amenityStatuses;
    }

    public int[] getResidentDistributions() {
        return residentDistributions;
    }

    public String[] getDefaultFormValues() {
        ArrayList<String> defaultFormValues = new ArrayList<>();
        defaultFormValues.add(referenceData.getDefaultComplexName());
        defaultFormValues.add(String.valueOf(referenceData.getDefaultFloors()));
        defaultFormValues.add(String.valueOf(referenceData.getDefaultRoomsPerFloor()));
        defaultFormValues.add(String.valueOf(referenceData.getDefaultApartmentType()));
        defaultFormValues.add(String.valueOf(referenceData.getDefaultOccupancyPercent()));
        for (String amenityName : referenceData.getAmenityNames()) {
            int index = amenityNames.indexOf(amenityName);
            defaultFormValues.add(index >= 0 ? "true" : "false");
            defaultFormValues.add(index >= 0 ? amenityStatuses.get(index) : "FULL_OPERATION");
        }
        for (int percent : referenceData.getDefaultDistribution()) {
            defaultFormValues.add(String.valueOf(percent));
        }
        return defaultFormValues.toArray(new String[0]);
    }
}