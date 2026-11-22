import java.util.ArrayList;
import java.util.Random;

public class ApartmentComplex {
    private String complexName;
    private int numberOfFloors;
    private int roomsPerFloor;
    private String layoutList;
    private ArrayList<Apartment> apartments;
    private ArrayList<Amenity> amenities;

    public ApartmentComplex(String complexName, int numberOfFloors, int roomsPerFloor) {
        this.complexName = complexName;
        this.numberOfFloors = numberOfFloors;
        this.roomsPerFloor = roomsPerFloor;
        layoutList = "";
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
            double percentSplit = (double) random.nextInt(30, 61) / 100;
            int maxSingles = (int) (percentSplit * (numberOfFloors * roomsPerFloor));
            int numOfSingles = 0;
            for (int i = 0; i < numberOfFloors; i++) {
                for (int j = 0; j < roomsPerFloor; j++) {
                    Apartment apartment;
                    if (random.nextBoolean() && numOfSingles < maxSingles) {
                        apartment = new SingleApartment(i, j);
                        numOfSingles++;
                        layoutList += "S";
                    } else {
                        apartment = new DoubleApartment(i, j);
                        layoutList += "D";
                    }
                    apartments.add(apartment);
                }
            }
        }
    }

    public void setLayout(String layout) {
        layoutList = layout;
        char[] apartmentType = layout.toCharArray();
        int arrayIndex = 0;
        for (int i = 0; i < numberOfFloors; i++) {
            for (int j = 0; j < roomsPerFloor; j++) {
                Apartment apartment;
                if (apartmentType[arrayIndex] == 'S') {
                    apartment = new SingleApartment(i, j);
                } else {
                    apartment = new DoubleApartment(i, j);
                }
                apartments.add(apartment);
                arrayIndex++;
            }
        }
    }

    public void addAmenity(Amenity amenity) {
        amenities.add(amenity);
    }

    public void placeResident(Resident resident, Random random) throws CapacityExceededException {
        ArrayList<Integer> vacantIndexes = new ArrayList<>();
        for (int i = 0; i < apartments.size(); i++) {
            if (apartments.get(i).getVacantSpace() != 0) {
                vacantIndexes.add(i);
            }
        }
        if (vacantIndexes.isEmpty())
            throw new CapacityExceededException("All apartments are currently full", -1, -1); // TODO: Figure out how to handle no floor problems

        int randIndex = random.nextInt(vacantIndexes.size());
        apartments.get(vacantIndexes.get(randIndex)).addResident(resident);
    }

    public void updateNoiseLevels() {
        for (Apartment apartment : apartments) {
            ArrayList<Resident> residents = apartment.getResidents();
            ArrayList<Apartment> neighbors = findNeighbors(apartment);
            double noiseTotal = 0.0, noiseAverage = 0.0;
            for (Apartment neighbor : neighbors) {
                noiseTotal += neighbor.calculateNoiseEmission();
            }
            noiseAverage = noiseTotal / neighbors.size();
            for (Resident resident : residents) {
                resident.setNoiseLevel(noiseAverage);
            }
        }
    }

    public ArrayList<Resident> processMoveOuts(double averageSatisfaction) {
        ArrayList<Resident> moveOuts = new ArrayList<>();
        for (Resident resident : getAllResidents()) {
            if (resident.shouldMoveOut(averageSatisfaction)) {
                Apartment apartment = findApartment(resident);
                apartment.removeResident(resident);
                moveOuts.add(resident);
            }
        }
        return moveOuts;
    }

    public ArrayList<Resident> getAllResidents() {
        ArrayList<Resident> allResidents = new ArrayList<>();
        for (Apartment apartment : apartments) {
            allResidents.addAll(apartment.getResidents());
        }
        return allResidents;
    }

    public int countOccupiedApartments(int capacity) {
        int occupiedApartments = 0;
        for (Apartment apartment : apartments) {
            if (apartment.getCapacity() == capacity && apartment.isOccupied()) occupiedApartments++;
        }
        return occupiedApartments;
    }

    public Apartment findApartment(Resident resident) {
        Apartment foundApartment = null;
        boolean found = false;
        for (Apartment apartment : apartments) {
            for (Resident apartmentResident : apartment.getResidents()) {
                if (apartmentResident.equals(resident)) {
                    foundApartment = apartment;
                    found = true;
                    break;
                }
            }
            if (found) break;
        }
        return foundApartment;
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

    public String getLayoutList() {
        return layoutList;
    }

    public ArrayList<Apartment> getApartments() {
        return apartments;
    }

    public ArrayList<Amenity> getAmenities() {
        return amenities;
    }

    public ArrayList<Apartment> findNeighbors(Apartment apartment) {
        // Check if bottom floor, top floor, leftMost, or rightMost
        ArrayList<Apartment> neighbors = new ArrayList<>();
        int floorNum = apartment.getFloorNumber(), roomNum = apartment.getRoomNumber();
        if (floorNum < numberOfFloors - 1) neighbors.add(apartments.get((floorNum + 1) * roomsPerFloor + roomNum));
        if (floorNum > 0) neighbors.add(apartments.get((floorNum - 1) * roomsPerFloor + roomNum));
        if (roomNum > 0) neighbors.add(apartments.get(floorNum * roomsPerFloor + (roomNum - 1)));
        if (roomNum < roomsPerFloor - 1) neighbors.add(apartments.get(floorNum * roomsPerFloor + (roomNum + 1)));
        return neighbors;
    }
}
