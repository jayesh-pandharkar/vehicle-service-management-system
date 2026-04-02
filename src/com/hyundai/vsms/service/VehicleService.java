package com.hyundai.vsms.service;

import com.hyundai.vsms.dao.CustomerDAO;
import com.hyundai.vsms.dao.VehicleDAO;
import com.hyundai.vsms.model.Customer;
import com.hyundai.vsms.model.Vehicle;

import java.util.List;

/**
 * VehicleService — Business Logic Layer for Vehicles.
 *
 * Key rules enforced here:
 *  - Customer must exist before adding a vehicle
 *  - Purchase year must be realistic (not in the future, not before 1900)
 *  - License plate must follow a basic format check
 */
public class VehicleService {

    private final VehicleDAO  vehicleDAO  = new VehicleDAO();
    private final CustomerDAO customerDAO = new CustomerDAO(); // needed to validate customer exists

    /**
     * Validates and adds a vehicle linked to a customer.
     */
    public int addVehicle(int customerId, String model, String licensePlate, int purchaseYear) {

        // ── Business Rule: Customer must exist ────────────────────────────────
        Customer customer = customerDAO.getCustomerById(customerId);
        if (customer == null) {
            System.out.println("[VALIDATION] Customer with ID " + customerId + " not found.");
            return -1;
        }

        // ── Business Rule: Model name required ────────────────────────────────
        if (model == null || model.trim().isEmpty()) {
            System.out.println("[VALIDATION] Vehicle model cannot be empty.");
            return -1;
        }

        // ── Business Rule: License plate format ───────────────────────────────
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            System.out.println("[VALIDATION] License plate cannot be empty.");
            return -1;
        }

        // ── Business Rule: Realistic purchase year ────────────────────────────
        int currentYear = java.time.LocalDate.now().getYear();
        if (purchaseYear < 1990 || purchaseYear > currentYear) {
            System.out.printf("[VALIDATION] Purchase year must be between 1990 and %d.%n", currentYear);
            return -1;
        }

        Vehicle vehicle = new Vehicle(customerId, model.trim(), licensePlate.trim().toUpperCase(), purchaseYear);
        int id = vehicleDAO.addVehicle(vehicle);

        if (id > 0) {
            System.out.printf("[SUCCESS] Vehicle added (ID: %d) for customer: %s%n", id, customer.getName());
        }
        return id;
    }

    /**
     * Returns all vehicles belonging to a customer.
     */
    public List<Vehicle> getVehiclesByCustomer(int customerId) {
        List<Vehicle> vehicles = vehicleDAO.getVehiclesByCustomer(customerId);
        if (vehicles.isEmpty()) {
            System.out.println("[INFO] No vehicles registered for Customer ID: " + customerId);
        }
        return vehicles;
    }

    /**
     * Returns all vehicles in the system.
     */
    public List<Vehicle> getAllVehicles() {
        return vehicleDAO.getAllVehicles();
    }

    /**
     * Finds a specific vehicle by ID.
     */
    public Vehicle getVehicleById(int vehicleId) {
        Vehicle v = vehicleDAO.getVehicleById(vehicleId);
        if (v == null) {
            System.out.println("[INFO] No vehicle found with ID: " + vehicleId);
        }
        return v;
    }
}
