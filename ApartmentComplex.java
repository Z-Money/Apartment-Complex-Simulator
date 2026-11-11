import java.util.ArrayList;
import java.util.Random;

public class ApartmentComplex {
    private String complexName;
    private int numberOfFloors;
    private int roomsPerFloor;
    private ArrayList<Apartment> apartments;
    private ArrayList<Amenity> amenities;

    public ApartmentComplex(String complexName, int numberOfFloors, int roomsPerFloor) {
        this.complexName = complexName;
        this.numberOfFloors = numberOfFloors;
        this.roomsPerFloor = roomsPerFloor;
        apartments = new ArrayList<>();
        amenities = new ArrayList<>();
    }

    public void buildLayout(int apartmentType, Random random) {
        if (apartmentType == 1) {
            for (int i = 0; i < numberOfFloors; i++) {
                for (int j = 0; j < roomsPerFloor; j++) {
                    Apartment apartment = new SingleApartment(i, j);
                    apartments.add(apartment);
                }
            }
        } else {
            double percentSplit = (double) random.nextInt(30, 60) / 100;
            int maxSingles = (int) (percentSplit * (numberOfFloors * roomsPerFloor));
            int numOfSingles = 0;
            for (int i = 0; i < numberOfFloors; i++) {
                for (int j = 0; j < roomsPerFloor; j++) {
                    Apartment apartment;
                    if (random.nextBoolean() && numOfSingles < maxSingles) {
                        apartment = new SingleApartment(i, j);
                        numOfSingles++;
                    } else {
                        apartment = new DoubleApartment(i, j);
                    }
                    apartments.add(apartment);
                }
            }
        }
    }

    public void addAmenity(Amenity amenity) {
        amenities.add(amenity);
    }

    public void placeResident(Resident resident, Random random) {

    }

    public void updateNoiseLevels() {

    }

    public ArrayList<Resident> processMoveOuts(double averageSatisfaction) {
        return null;
    }

    public ArrayList<Resident> getAllResidents() {
        return null;
    }

    public int countOccupiedApartments(int capacity) {
        return 0;
    }

    public Apartment findApartment(Resident resident) {
        return null;
    }

    public String getComplexName() {
        return this.complexName;
    }

    public int getNumberOfFloors() {
        return this.numberOfFloors;
    }

    public int getRoomsPerFloor() {
        return this.roomsPerFloor;
    }

    public ArrayList<Apartment> getApartments() {
        return apartments;
    }

    public ArrayList<Amenity> getAmenities() {
        return amenities;
    }

    private ArrayList<Apartment> findNeighbors(Apartment apartment) {
        return null;
    }
}
