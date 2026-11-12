import java.util.ArrayList;

public class SingleApartment extends Apartment {
    private Resident occupant;

    public SingleApartment(int floorNumber, int roomNumber) {
        super(floorNumber, roomNumber);
    }

    @Override
    public int getCapacity() {
        return 1;
    }

    @Override
    public ArrayList<Resident> getResidents() {
        ArrayList<Resident> residents = new ArrayList<>();
        if (occupant != null) {
            residents.add(occupant);
        }
        return residents;
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
        double noiseEmission = 0.0;
        if (occupant == null) {
            noiseEmission = 0;
        } else {
            noiseEmission = occupant.calculateNoiseEmission();
        }
        return noiseEmission;
    }
}
