public class CapacityExceededException extends Exception {
    private int floorNumber;
    private int roomNumber;

    public CapacityExceededException(String message, int floorNumber, int roomNumber) {
        super(message);
        this.floorNumber = floorNumber;
        this.roomNumber = roomNumber;
    }

    public CapacityExceededException(String message) {
        super(message);
    }

    public int getFloorNumber() {
        return this.floorNumber;
    }

    public int getRoomNumber() {
        return this.roomNumber;
    }
}
