import java.util.ArrayList;

public class DoubleApartment extends Apartment {
    private ArrayList<Resident> occupants;
    private int maximumOccupants;

    public DoubleApartment(int floorNumber, int roomNumber) {
        super(floorNumber, roomNumber);
    }

    @Override
    public int getCapacity() {
        return 0;
    }

    @Override
    public ArrayList<Resident> getResidents() {
        return null;
    }

    @Override
    public void addResident(Resident resident) {

    }

    @Override
    public void removeResident(Resident resident) {

    }

    @Override
    public double calculateNoiseEmission() {
        return 0;
    }
}
