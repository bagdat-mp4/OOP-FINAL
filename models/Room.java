package models;

import enums.RoomType;
import java.io.Serializable;

public class Room implements Serializable {
    private String roomNumber;
    private int capacity;
    private RoomType type;
    private boolean isAvailable;

    public Room(String roomNumber, int capacity, RoomType type) {
        this.roomNumber = roomNumber;
        this.capacity = capacity;
        this.type = type;
        this.isAvailable = true;
    }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public RoomType getType() { return type; }
    public void setType(RoomType type) { this.type = type; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    @Override
    public String toString() {
        return "Room " + roomNumber + " (" + type + ", capacity: " + capacity + ")";
    }
}
