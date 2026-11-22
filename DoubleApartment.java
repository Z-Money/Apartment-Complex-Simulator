import java.util.ArrayList;

public class DoubleApartment extends Apartment {
    private ArrayList<Resident> occupants;
    private int maximumOccupants;

    public DoubleApartment(int floorNumber, int roomNumber) {
        super(floorNumber, roomNumber);
        occupants = new ArrayList<>();
    }

    @Override
    public int getCapacity() {
        return 2;
    }

    @Override
    public ArrayList<Resident> getResidents() {
        return occupants;
    }

    @Override
    public void addResident(Resident resident) throws CapacityExceededException {
        if (getResidentCount() >= getCapacity()) throw new CapacityExceededException("Apartment is full", getFloorNumber(), getRoomNumber());
        occupants.add(resident);
    }

    @Override
    public void removeResident(Resident resident) {
        occupants.remove(resident);
    }

    @Override
    public double calculateNoiseEmission() {
        double noiseEmission = 0.0;
        for (Resident resident : occupants) {
            noiseEmission += resident.calculateNoiseEmission();
        }
        return noiseEmission;
    }
}
