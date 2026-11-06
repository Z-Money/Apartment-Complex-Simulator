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

    public Amenity createAmenity(String name, String initialStatus) {
        return null;
    }

    public ResidentType findResidentType(String typeName) {
        for (int i = 0; i < residentTypes.size() - 1; i++) {
            if (residentTypes.get(i).getTypeName().equals(typeName)) {
                return residentTypes.get(i);
            }
        }
        return null;
    }

    public double getUtilityPrice(String utilityName) {
        for (int i = 0; i < utilityNames.length - 1; i++) {
            if (utilityNames[i].equals(utilityName)) {
                return amenityCosts[i];
            }
        }
        return -1;
    }

    public double getBaseCost() {
        return this.baseCost;
    }

    public double getBaselineUsage() {
        return this.baselineUsage;
    }

    public ArrayList<ResidentType> getResidentTypes() {
        return this.residentTypes;
    }

    public String[] getAmenityNames() {
        return this.amenityNames;
    }

    public int[] getFloorLimits() {
        return this.floorLimits;
    }

    public int[] getRoomLimits() {
        return this.roomLimits;
    }

    public int getMaximumNameLength() {
        return this.maximumNameLength;
    }

    public int getDefaultFloors() {
        return this.defaultFloors;
    }

    public int getDefaultRoomsPerFloor() {
        return this.defaultRoomsPerFloor;
    }

    public int getDefaultApartmentType() {
        return this.defaultApartmentType;
    }

    public int getDefaultOccupancyPercent() {
        return this.defaultOccupancyPercent;
    }

    public String getDefaultComplexName() {
        return this.defaultComplexName;
    }

    public int[] getDefaultDistribution() {
        return this.defaultDistribution;
    }
}
