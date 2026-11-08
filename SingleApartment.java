import java.util.ArrayList;

public class SingleApartment extends Apartment {
    private Resident occupant;

    public SingleApartment(int floorNumber, int roomNumber) {
        super(floorNumber, roomNumber);
    }

    @Override
    public int getCapacity() {
        if (occupant == null) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public ArrayList<Resident> getResidents() {
        return null;
    }

    @Override
    public void addResident(Resident resident) {
        occupant = resident;
    }

    @Override
    public void removeResident(Resident resident) {
        occupant = null;
    }

    @Override
    public double calculateNoiseEmission() {
        return 0;
    }
}
