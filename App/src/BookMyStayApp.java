

import java.util.*;

// Reservation class representing a guest’s booking intent
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Centralized inventory class
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    public void addRoomType(String roomType, int availability) {
        inventory.put(roomType, availability);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public boolean decrementAvailability(String roomType) {
        if (inventory.containsKey(roomType) && inventory.get(roomType) > 0) {
            inventory.put(roomType, inventory.get(roomType) - 1);
            return true;
        }
        return false;
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println("Room Type: " + entry.getKey() +
                    " | Availability: " + entry.getValue());
        }
    }
}

// Booking Service class
class BookingService {
    private Queue<Reservation> requestQueue;
    private RoomInventory inventory;
    private Map<String, Set<String>> allocatedRooms;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        requestQueue = new LinkedList<>();
        allocatedRooms = new HashMap<>();
    }

    // Add booking request to queue
    public void addRequest(Reservation reservation) {
        requestQueue.add(reservation);
        System.out.println("Booking request added for " + reservation.getGuestName());
    }

    // Process requests in FIFO order
    public void processRequests() {
        System.out.println("\nProcessing Booking Requests...");
        while (!requestQueue.isEmpty()) {
            Reservation reservation = requestQueue.poll();
            allocateRoom(reservation);
        }
    }

    // Allocate room safely
    private void allocateRoom(Reservation reservation) {
        String roomType = reservation.getRoomType();

        if (inventory.getAvailability(roomType) > 0) {
            // Generate unique room ID
            String roomId = UUID.randomUUID().toString();

            // Ensure uniqueness using Set
            allocatedRooms.putIfAbsent(roomType, new HashSet<>());
            allocatedRooms.get(roomType).add(roomId);

            // Update inventory immediately
            inventory.decrementAvailability(roomType);

            // Confirm reservation
            System.out.println("Reservation Confirmed: Guest " + reservation.getGuestName() +
                    " | Room Type: " + roomType +
                    " | Room ID: " + roomId);
        } else {
            System.out.println("Reservation Failed: No availability for " + roomType +
                    " (Guest: " + reservation.getGuestName() + ")");
        }
    }

    // Display allocated rooms
    public void displayAllocations() {
        System.out.println("\nAllocated Rooms:");
        for (Map.Entry<String, Set<String>> entry : allocatedRooms.entrySet()) {
            System.out.println("Room Type: " + entry.getKey() +
                    " | Room IDs: " + entry.getValue());
        }
    }
}

// Application entry point
public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("Welcome to Book My Stay App!");
        System.out.println("Application Version: 6.1\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 2);
        inventory.addRoomType("Double Room", 1);
        inventory.addRoomType("Suite Room", 1);

        // Initialize booking service
        BookingService bookingService = new BookingService(inventory);

        // Guests submit booking requests
        bookingService.addRequest(new Reservation("Alice", "Single Room"));
        bookingService.addRequest(new Reservation("Bob", "Double Room"));
        bookingService.addRequest(new Reservation("Charlie", "Suite Room"));
        bookingService.addRequest(new Reservation("Diana", "Single Room"));
        bookingService.addRequest(new Reservation("Ethan", "Single Room")); // should fail

        // Process requests and confirm reservations
        bookingService.processRequests();

        // Display final allocations and inventory state
        bookingService.displayAllocations();
        inventory.displayInventory();

        // Program terminates after displaying information
    }
}
