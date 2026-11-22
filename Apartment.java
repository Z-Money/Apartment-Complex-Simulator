import java.util.ArrayList;

public abstract class Apartment {
    private int floorNumber;
    private int roomNumber;

    public Apartment(int floorNumber, int roomNumber) {
        this.floorNumber = floorNumber;
        this.roomNumber = roomNumber;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public boolean isOccupied() {
        return getResidentCount() > 0;
    }

    public int getResidentCount() {
        return getResidents().size();
    }

    public int getVacantSpace() {
        return getCapacity() - getResidentCount();
    }

    public abstract int getCapacity();

    public abstract ArrayList<Resident> getResidents();

    public abstract void addResident(Resident resident) throws CapacityExceededException;

    public abstract void removeResident(Resident resident);

    public abstract double calculateNoiseEmission();
}