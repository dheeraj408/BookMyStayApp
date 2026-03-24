
import java.util.*;

// Reservation class representing a confirmed booking
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;
    private boolean isCancelled;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.isCancelled = false;
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

    public String getRoomId() {
        return roomId;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void cancel() {
        this.isCancelled = true;
    }

    public void displayReservation() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room Type: " + roomType +
                " | Room ID: " + roomId +
                " | Status: " + (isCancelled ? "Cancelled" : "Active"));
    }
}

// Room Inventory class
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

    public void incrementAvailability(String roomType) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, inventory.get(roomType) + 1);
        }
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
    private RoomInventory inventory;
    private Map<String, Reservation> reservations;
    private Stack<String> rollbackStack;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        this.reservations = new HashMap<>();
        this.rollbackStack = new Stack<>();
    }

    // Confirm reservation
    public void confirmReservation(String guestName, String roomType) {
        if (inventory.getAvailability(roomType) > 0) {
            String reservationId = UUID.randomUUID().toString();
            String roomId = UUID.randomUUID().toString();

            Reservation reservation = new Reservation(reservationId, guestName, roomType, roomId);
            reservations.put(reservationId, reservation);

            inventory.decrementAvailability(roomType);
            rollbackStack.push(roomId);

            System.out.println("Reservation Confirmed:");
            reservation.displayReservation();
        } else {
            System.out.println("Reservation Failed: No availability for " + roomType +
                    " (Guest: " + guestName + ")");
        }
    }

    // Cancel reservation with rollback
    public void cancelReservation(String reservationId) {
        Reservation reservation = reservations.get(reservationId);

        if (reservation == null) {
            System.out.println("Cancellation Failed: Reservation not found.");
            return;
        }

        if (reservation.isCancelled()) {
            System.out.println("Cancellation Failed: Reservation already cancelled.");
            return;
        }

        // Perform rollback
        reservation.cancel();
        inventory.incrementAvailability(reservation.getRoomType());
        rollbackStack.push(reservation.getRoomId());

        System.out.println("Reservation Cancelled:");
        reservation.displayReservation();
    }

    // Display all reservations
    public void displayReservations() {
        System.out.println("\nAll Reservations:");
        for (Reservation reservation : reservations.values()) {
            reservation.displayReservation();
        }
    }

    // Display rollback stack
    public void displayRollbackStack() {
        System.out.println("\nRollback Stack (Released Room IDs): " + rollbackStack);
    }

    public RoomInventory getInventory() {
        return inventory;
    }

    public Map<String, Reservation> getReservations() {
        return reservations;
    }

    public Stack<String> getRollbackStack() {
        return rollbackStack;
    }
}

// Application entry point
public class BookMyStayApp{
    public static void main(String[] args) {
        System.out.println("Welcome to Book My Stay App!");
        System.out.println("Application Version: 10.1\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 1);
        inventory.addRoomType("Double Room", 1);

        // Initialize booking service
        BookingService bookingService = new BookingService(inventory);

        // Confirm reservations
        bookingService.confirmReservation("Alice", "Single Room");
        bookingService.confirmReservation("Bob", "Double Room");

        // Display reservations and inventory
        bookingService.displayReservations();
        inventory.displayInventory();

        // Cancel Alice's reservation
        System.out.println("\n--- Cancellation Request ---");
        for (Reservation r : bookingService.getReservations().values()) {
            if (r.getGuestName().equals("Alice")) {
                bookingService.cancelReservation(r.getReservationId());
            }
        }

        // Display updated reservations, inventory, and rollback stack
        bookingService.displayReservations();
        inventory.displayInventory();
        bookingService.displayRollbackStack();

        // Program terminates after displaying information
    }
}
