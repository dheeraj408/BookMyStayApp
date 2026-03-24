/**
 * UseCase8BookingHistoryReport.java
 *
 * This class demonstrates booking history tracking and reporting
 * in the Book My Stay application. Confirmed reservations are stored
 * in chronological order and reports are generated for administrative review.
 *
 * @author YourName
 * @version 8.1
 */

import java.util.*;

// Reservation class representing a confirmed booking
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

// Booking History class
class BookingHistory {
    private List<Reservation> confirmedBookings;

    public BookingHistory() {
        confirmedBookings = new ArrayList<>();
    }

    // Add confirmed reservation to history
    public void addReservation(Reservation reservation) {
        confirmedBookings.add(reservation);
        System.out.println("Reservation stored in history: " + reservation.getReservationId());
    }

    // Retrieve all reservations
    public List<Reservation> getAllReservations() {
        return confirmedBookings;
    }
}

// Reporting service class
class BookingReportService {
    private BookingHistory history;

    public BookingReportService(BookingHistory history) {
        this.history = history;
    }

    // Display all reservations in chronological order
    public void displayBookingHistory() {
        System.out.println("\nBooking History Report:");
        for (Reservation reservation : history.getAllReservations()) {
            reservation.displayReservation();
        }
    }

    // Generate summary report
    public void generateSummaryReport() {
        System.out.println("\nSummary Report:");
        Map<String, Integer> roomTypeCount = new HashMap<>();

        for (Reservation reservation : history.getAllReservations()) {
            roomTypeCount.put(reservation.getRoomType(),
                    roomTypeCount.getOrDefault(reservation.getRoomType(), 0) + 1);
        }

        for (Map.Entry<String, Integer> entry : roomTypeCount.entrySet()) {
            System.out.println("Room Type: " + entry.getKey() +
                    " | Total Bookings: " + entry.getValue());
        }
    }
}

// Application entry point
public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("Welcome to Book My Stay App!");
        System.out.println("Application Version: 8.1\n");

        // Initialize booking history
        BookingHistory history = new BookingHistory();

        // Simulate confirmed reservations
        Reservation r1 = new Reservation(UUID.randomUUID().toString(), "Alice", "Single Room");
        Reservation r2 = new Reservation(UUID.randomUUID().toString(), "Bob", "Suite Room");
        Reservation r3 = new Reservation(UUID.randomUUID().toString(), "Charlie", "Double Room");
        Reservation r4 = new Reservation(UUID.randomUUID().toString(), "Diana", "Single Room");

        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);
        history.addReservation(r4);

        // Initialize reporting service
        BookingReportService reportService = new BookingReportService(history);

        // Admin requests booking history
        reportService.displayBookingHistory();

        // Admin requests summary report
        reportService.generateSummaryReport();

        // Program terminates after displaying reports
    }
}
