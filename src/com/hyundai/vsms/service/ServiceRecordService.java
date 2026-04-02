package com.hyundai.vsms.service;

import com.hyundai.vsms.dao.ServiceDAO;
import com.hyundai.vsms.dao.VehicleDAO;
import com.hyundai.vsms.model.ServiceRecord;
import com.hyundai.vsms.model.Vehicle;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * ServiceRecordService — Business Logic Layer for Service Records.
 *
 * Key rules:
 *  - Vehicle must exist before adding a service
 *  - Cost must be positive
 *  - Rating must be 1–5
 *  - Service date cannot be in the future
 *  - Duration must be positive
 */
public class ServiceRecordService {

    private final ServiceDAO serviceDAO = new ServiceDAO();
    private final VehicleDAO vehicleDAO = new VehicleDAO(); // to validate vehicle exists

    /**
     * Validates and adds a new service record.
     */
    public int addServiceRecord(int vehicleId, LocalDate serviceDate, String serviceType,
                                BigDecimal cost, double durationHours, int rating, String notes) {

        // ── Validation: Vehicle must exist ────────────────────────────────────
        Vehicle vehicle = vehicleDAO.getVehicleById(vehicleId);
        if (vehicle == null) {
            System.out.println("[VALIDATION] Vehicle with ID " + vehicleId + " not found.");
            return -1;
        }

        // ── Validation: Service type required ─────────────────────────────────
        if (serviceType == null || serviceType.trim().isEmpty()) {
            System.out.println("[VALIDATION] Service type cannot be empty.");
            return -1;
        }

        // ── Validation: Cost must be positive ─────────────────────────────────
        if (cost == null || cost.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("[VALIDATION] Cost must be greater than zero.");
            return -1;
        }

        // ── Validation: Duration must be positive ─────────────────────────────
        if (durationHours <= 0) {
            System.out.println("[VALIDATION] Duration must be greater than 0 hours.");
            return -1;
        }

        // ── Validation: Rating 1–5 ────────────────────────────────────────────
        if (rating < 1 || rating > 5) {
            System.out.println("[VALIDATION] Rating must be between 1 and 5.");
            return -1;
        }

        // ── Validation: Date not in the future ───────────────────────────────
        if (serviceDate != null && serviceDate.isAfter(LocalDate.now())) {
            System.out.println("[VALIDATION] Service date cannot be in the future.");
            return -1;
        }

        // Use today if no date provided
        LocalDate date = (serviceDate != null) ? serviceDate : LocalDate.now();

        ServiceRecord record = new ServiceRecord(
            vehicleId, date, serviceType.trim(), cost, durationHours, rating, notes
        );

        int id = serviceDAO.addServiceRecord(record);
        if (id > 0) {
            System.out.printf("[SUCCESS] Service record added (ID: %d) for vehicle: %s%n",
                              id, vehicle.getModel());
        }
        return id;
    }

    /**
     * Returns service history for a specific vehicle.
     */
    public List<ServiceRecord> getServiceHistoryByVehicle(int vehicleId) {
        List<ServiceRecord> records = serviceDAO.getServicesByVehicle(vehicleId);
        if (records.isEmpty()) {
            System.out.println("[INFO] No service history found for Vehicle ID: " + vehicleId);
        }
        return records;
    }

    /**
     * Returns all services across all vehicles of a customer (via JOIN).
     */
    public List<ServiceRecord> getServiceHistoryByCustomer(int customerId) {
        List<ServiceRecord> records = serviceDAO.getServicesByCustomer(customerId);
        if (records.isEmpty()) {
            System.out.println("[INFO] No service history found for Customer ID: " + customerId);
        }
        return records;
    }

    // ── REPORT DELEGATES ──────────────────────────────────────────────────────

    public BigDecimal getTotalRevenue() {
        return serviceDAO.getTotalRevenue();
    }

    public Map<String, Integer> getMostFrequentServiceTypes() {
        return serviceDAO.getMostFrequentServiceTypes();
    }

    public void printCustomerRatingReport() {
        serviceDAO.printAverageRatingPerCustomer();
    }

    public void printMonthlyRevenueReport() {
        serviceDAO.printMonthlyRevenueReport();
    }
}
