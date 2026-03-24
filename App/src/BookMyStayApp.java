
import java.util.*;

// Custom exception for invalid booking scenarios
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation class
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void displayReservation() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room Type: " + roomType);
    }
}

// Room Inventory class with validation
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    public void addRoomType(String roomType, int availability) {
        inventory.put(roomType, availability);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, -1); // -1 indicates invalid room type
    }

    public boolean decrementAvailability(String roomType) throws InvalidBookingException {
        if (!inventory.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }
        int currentAvailability = inventory.get(roomType);
        if (currentAvailability <= 0) {
            throw new InvalidBookingException("No availability for room type: " + roomType);
        }
        inventory.put(roomType, currentAvailability - 1);
        return true;
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println("Room Type: " + entry.getKey() +
                    " | Availability: " + entry.getValue());
        }
    }
}

// Booking Service with validation
class BookingService {
    private RoomInventory inventory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void confirmReservation(String guestName, String roomType) {
        try {
            // Validate input
            if (guestName == null || guestName.trim().isEmpty()) {
                throw new InvalidBookingException("Guest name cannot be empty.");
            }

            // Validate room type and availability
            inventory.decrementAvailability(roomType);

            // Generate reservation ID
            String reservationId = UUID.randomUUID().toString();
            Reservation reservation = new Reservation(reservationId, guestName, roomType);

            // Confirm reservation
            System.out.println("Reservation Confirmed:");
            reservation.displayReservation();

        } catch (InvalidBookingException e) {
            // Graceful failure handling
            System.out.println("Reservation Failed: " + e.getMessage());
        }
    }
}

// Application entry point
public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("Welcome to Book My Stay App!");
        System.out.println("Application Version: 9.1\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 1);
        inventory.addRoomType("Double Room", 0); // unavailable
        inventory.addRoomType("Suite Room", 2);

        // Initialize booking service
        BookingService bookingService = new BookingService(inventory);

        // Valid booking
        bookingService.confirmReservation("Alice", "Single Room");

        // Invalid booking: empty guest name
        bookingService.confirmReservation("", "Suite Room");

        // Invalid booking: unavailable room type
        bookingService.confirmReservation("Bob", "Double Room");

        // Invalid booking: non-existent room type
        bookingService.confirmReservation("Charlie", "Penthouse");

        // Display final inventory state
        inventory.displayInventory();

        // Program terminates after displaying information
    }
}
