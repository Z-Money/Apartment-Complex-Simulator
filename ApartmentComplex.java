import java.util.ArrayList;
import java.util.Random;

public class ApartmentComplex {
    private String complexName;
    private int numberOfFloors;
    private int roomsPerFloor;
    private ArrayList<Apartment> apartments;
    private ArrayList<Amenity> amenities;

    public ApartmentComplex(String complexName, int numberOfFloors, int roomsPerFloor) {

    }

    public void buildLayout(int apartmentType, Random random) {

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
        return null;
    }

    public ArrayList<Amenity> getAmenities() {
        return null;
    }

    private ArrayList<Apartment> findNeighbors(Apartment apartment) {
        return null;
    }
}
