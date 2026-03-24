

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

// Add-On Service class
class Service {
    private String serviceName;
    private double serviceCost;

    public Service(String serviceName, double serviceCost) {
        this.serviceName = serviceName;
        this.serviceCost = serviceCost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getServiceCost() {
        return serviceCost;
    }

    @Override
    public String toString() {
        return serviceName + " ($" + serviceCost + ")";
    }
}

// Manager class for add-on services
class AddOnServiceManager {
    private Map<String, List<Service>> reservationServices;

    public AddOnServiceManager() {
        reservationServices = new HashMap<>();
    }

    // Attach service to reservation
    public void addServiceToReservation(Reservation reservation, Service service) {
        reservationServices.putIfAbsent(reservation.getReservationId(), new ArrayList<>());
        reservationServices.get(reservation.getReservationId()).add(service);
        System.out.println("Service added: " + service.getServiceName() +
                " for Reservation ID: " + reservation.getReservationId());
    }

    // Display services for a reservation
    public void displayServicesForReservation(Reservation reservation) {
        List<Service> services = reservationServices.getOrDefault(reservation.getReservationId(), new ArrayList<>());
        System.out.println("\nReservation Details:");
        reservation.displayReservation();

        if (services.isEmpty()) {
            System.out.println("No add-on services selected.");
        } else {
            System.out.println("Selected Services:");
            double totalCost = 0;
            for (Service service : services) {
                System.out.println("- " + service);
                totalCost += service.getServiceCost();
            }
            System.out.println("Total Additional Cost: $" + totalCost);
        }
    }
}

// Application entry point
public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("Welcome to Book My Stay App!");
        System.out.println("Application Version: 7.1\n");

        // Create a confirmed reservation
        Reservation reservation = new Reservation(UUID.randomUUID().toString(), "Alice", "Suite Room");

        // Initialize add-on service manager
        AddOnServiceManager serviceManager = new AddOnServiceManager();

        // Guest selects optional services
        Service breakfast = new Service("Breakfast", 20.0);
        Service spa = new Service("Spa Access", 50.0);
        Service airportPickup = new Service("Airport Pickup", 30.0);

        serviceManager.addServiceToReservation(reservation, breakfast);
        serviceManager.addServiceToReservation(reservation, spa);
        serviceManager.addServiceToReservation(reservation, airportPickup);

        // Display reservation with selected services
        serviceManager.displayServicesForReservation(reservation);

        // Program terminates after displaying information
    }
}
