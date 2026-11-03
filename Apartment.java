import java.util.ArrayList;

public abstract class Apartment {
    private int floorNumber;
    private int roomNumber;

    public Apartment(int floorNumber, int roomNumber) {

    }

    public int getFloorNumber() {
        return this.floorNumber;
    }

    public int getRoomNumber() {
        return this.roomNumber;
    }

    public boolean isOccupied() {
        // TODO: Figure out how to determine isOccupied
        return false;
    }

    public int getResidentCount() {
        // TODO: Figure out how to determine this
        return 0;
    }

    public abstract int getCapacity();

    public abstract ArrayList<Resident> getResidents();

    public abstract void addResident(Resident resident);

    public abstract void removeResident(Resident resident);

    public abstract double calculateNoiseEmission();
}