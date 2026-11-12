import java.util.ArrayList;

public class ReferenceData {
    private double baseCost;
    private double baselineUsage;
    private String[] utilityNames;
    private double[] utilityPrices;
    private ArrayList<ResidentType> residentTypes;
    private String[] amenityNames;
    private double[] amenityChances;
    private double[] amenityQualityContributions;
    private double[] amenityCosts;
    private int[] floorLimits;
    private int[] roomLimits;
    private int maximumNameLength;
    private int defaultFloors;
    private int defaultRoomsPerFloor;
    private int defaultApartmentType;
    private int defaultOccupancyPercent;
    private String defaultComplexName;
    private int[] defaultDistribution;

    public ReferenceData() {
        baseCost = 0;
        baselineUsage = 0;
        utilityNames = new String[2];
        utilityPrices = new double[2];
        residentTypes = new ArrayList<>();
        amenityNames = new String[4];
        amenityChances = new double[4];
        amenityQualityContributions = new double[4];
        amenityCosts = new double[4];
        floorLimits = new int[2];
        roomLimits = new int[2];
        maximumNameLength = 0;
        defaultFloors = 0;
        defaultRoomsPerFloor = 0;
        defaultApartmentType = 0;
        defaultOccupancyPercent = 0;
        defaultComplexName = "";
        defaultDistribution = new int[4];
    }

    public ReferenceData(double baseCost, double baselineUsage, String[] utilityNames, double[] utilityPrices,
                         ArrayList<ResidentType> residentTypes, String[] amenityNames, double[] amenityChances,
                         double[] amenityQualityContributions, double[] amenityCosts, int[] floorLimits,
                         int[] roomLimits, int maximumNameLength, int defaultFloors, int defaultRoomsPerFloor,
                         int defaultApartmentType, int defaultOccupancyPercent, String defaultComplexName,
                         int[] defaultDistribution) {
        this.baseCost = baseCost;
        this.baselineUsage = baselineUsage;
        this.utilityNames = utilityNames;
        this.utilityPrices = utilityPrices;
        this.residentTypes = residentTypes;
        this.amenityNames = amenityNames;
        this.amenityChances = amenityChances;
        this.amenityQualityContributions = amenityQualityContributions;
        this.amenityCosts = amenityCosts;
        this.floorLimits = floorLimits;
        this.roomLimits = roomLimits;
        this.maximumNameLength = maximumNameLength;
        this.defaultFloors = defaultFloors;
        this.defaultRoomsPerFloor = defaultRoomsPerFloor;
        this.defaultApartmentType = defaultApartmentType;
        this.defaultOccupancyPercent = defaultOccupancyPercent;
        this.defaultComplexName = defaultComplexName;
        this.defaultDistribution = defaultDistribution;
    }

    public void loadFromLines(String[] line, int amenityIndex, int utilityIndex) throws InvalidConfigurationException {
        switch (line[0]) {
            case "BaseCost" -> baseCost = Double.parseDouble(line[1]);
            case "BaselineUsage" -> baselineUsage = Double.parseDouble(line[1]);
            case "Utility" -> parseUtilityLine(line, utilityNames, utilityPrices, utilityIndex);
            case "FloorLimits" -> floorLimits = parseIntArray(line);
            case "RoomLimits" -> roomLimits = parseIntArray(line);
            case "MaximumNameLength" -> maximumNameLength = Integer.parseInt(line[1]);
            case "DefaultFloors" -> defaultFloors = Integer.parseInt(line[1]);
            case "DefaultRoomsPerFloor" -> defaultRoomsPerFloor = Integer.parseInt(line[1]);
            case "DefaultApartmentType" -> defaultApartmentType = Integer.parseInt(line[1]);
            case "DefaultOccupancyPercent" -> defaultOccupancyPercent = Integer.parseInt(line[1]);
            case "DefaultComplexName" -> defaultComplexName = line[1];
            case "DefaultDistribution" -> defaultDistribution = parseIntArray(line);
            case "ResidentType" -> residentTypes.add(parseResidentLine(line));
            case "Amenity" ->
                    parseAmenityLine(line, amenityNames, amenityChances, amenityQualityContributions, amenityCosts, amenityIndex);
            default -> throw new InvalidConfigurationException("Invalid reference data type", "referenceDataFile");
        }
    }

    public ResidentType parseResidentLine(String[] line) throws InvalidConfigurationException {
        try {
            String typeName = line[1];
            double noiseSensitivity = Double.parseDouble(line[2]);
            double amenityWeight = Double.parseDouble(line[3]);
            double costSensitivity = Double.parseDouble(line[4]);
            double utilityUsageRate = Double.parseDouble(line[5]);
            double moveOutSensitivity = Double.parseDouble(line[6]);
            double generationChance = Double.parseDouble(line[7]);
            return new ResidentType(typeName, noiseSensitivity, amenityWeight, costSensitivity, utilityUsageRate, moveOutSensitivity, generationChance);
        } catch (NumberFormatException e) {
            throw new InvalidConfigurationException("Invalid resident type value: " + e.getLocalizedMessage(), "residentType");
        }
    }

    public void parseUtilityLine(String[] line, String[] utilityNames, double[] utilityPrices, int index)
            throws InvalidConfigurationException {
        try {
            utilityNames[index] = line[1];
            utilityPrices[index] = Double.parseDouble(line[2]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new InvalidConfigurationException("Invalid setting: " + line[0], "referenceDataFile");
        }
    }

    public void parseAmenityLine(String[] line, String[] amenityNames, double[] amenityChances,
                                 double[] amenityQualityContribution, double[] amenityCost,
                                 int index) throws InvalidConfigurationException {
        try {
            amenityNames[index] = line[1];
            amenityChances[index] = Double.parseDouble(line[2]);
            amenityQualityContribution[index] = Double.parseDouble(line[3]);
            amenityCost[index] = Double.parseDouble(line[4]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new InvalidConfigurationException("Invalid setting: " + line[0], "referenceDataFile");
        }
    }

    public int[] parseIntArray(String[] line) throws NumberFormatException {
        int[] values = new int[line.length - 1];
        for (int i = 0; i < values.length; i++) values[i] = Integer.parseInt(line[i + 1]);
        return values;
    }

    public Amenity createAmenity(String name, String initialStatus) throws InvalidConfigurationException {
        int index = getAmenityByIndex(name, amenityNames);
        if (index != -1) {
            Amenity amenity;
            if (name.equals("Laundry Machines")) {
                amenity = new LaundryMachines(name, amenityChances[index], amenityQualityContributions[index], amenityCosts[index]);
                amenity.setCurrentStatus(initialStatus);
            } else if (name.equals("Pool")) {
                amenity = new Pool(name, amenityChances[index], amenityQualityContributions[index], amenityCosts[index]);
                amenity.setCurrentStatus(initialStatus);
            } else if (name.equals("Gym")) {
                amenity = new Gym(name, amenityChances[index], amenityQualityContributions[index], amenityCosts[index]);
                amenity.setCurrentStatus(initialStatus);
            } else if (name.equals("Security")) {
                amenity = new Security(name, amenityChances[index], amenityQualityContributions[index], amenityCosts[index]);
                amenity.setCurrentStatus(initialStatus);
            } else {
                amenity = null;
            }
            return amenity;
        }
        return null;
    }

    public int getAmenityByIndex(String name, String[] amenityNames) {
        int pos = -1;
        for (int i = 0; i < amenityNames.length; i++) {
            if (name.equals(amenityNames[i])) {
                pos = i;
            }
        }
        return pos;
    }

    public ResidentType findResidentType(String typeName) {
        ResidentType resType = null;
        for (int i = 0; i < residentTypes.size(); i++) {
            if (residentTypes.get(i).getTypeName().equals(typeName)) {
                resType = residentTypes.get(i);
            }
        }
        return resType;
    }

    public double getUtilityPrice(String utilityName) {
        double price = -1;
        for (int i = 0; i < utilityNames.length; i++) {
            if (utilityNames[i].equals(utilityName)) {
                price = utilityPrices[i];
            }
        }
        return price;
    }

    public double getBaseCost() {
        return baseCost;
    }

    public double getBaselineUsage() {
        return baselineUsage;
    }

    public ArrayList<ResidentType> getResidentTypes() {
        return residentTypes;
    }

    public String[] getAmenityNames() {
        return amenityNames;
    }

    public int[] getFloorLimits() {
        return floorLimits;
    }

    public int[] getRoomLimits() {
        return roomLimits;
    }

    public int getMaximumNameLength() {
        return maximumNameLength;
    }

    public int getDefaultFloors() {
        return defaultFloors;
    }

    public int getDefaultRoomsPerFloor() {
        return defaultRoomsPerFloor;
    }

    public int getDefaultApartmentType() {
        return defaultApartmentType;
    }

    public int getDefaultOccupancyPercent() {
        return defaultOccupancyPercent;
    }

    public String getDefaultComplexName() {
        return defaultComplexName;
    }

    public int[] getDefaultDistribution() {
        return defaultDistribution;
    }
}
