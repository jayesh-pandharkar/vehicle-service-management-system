package com.hyundai.vsms.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * ServiceRecord — POJO mapping to the 'services' table.
 *
 * Uses:
 *  - LocalDate  for service_date (modern Java date handling)
 *  - BigDecimal for cost         (precise monetary values — never use float/double for money!)
 */
public class ServiceRecord {

    private int        serviceId;
    private int        vehicleId;
    private LocalDate  serviceDate;
    private String     serviceType;
    private BigDecimal cost;
    private double     durationHours;
    private int        rating;         // 1–5
    private String     notes;

    // ── Constructors ──────────────────────────────────────────────────────────

    /** For inserting a new service record */
    public ServiceRecord(int vehicleId, LocalDate serviceDate, String serviceType,
                         BigDecimal cost, double durationHours, int rating, String notes) {
        this.vehicleId     = vehicleId;
        this.serviceDate   = serviceDate;
        this.serviceType   = serviceType;
        this.cost          = cost;
        this.durationHours = durationHours;
        this.rating        = rating;
        this.notes         = notes;
    }

    /** For reading from DB */
    public ServiceRecord(int serviceId, int vehicleId, LocalDate serviceDate,
                         String serviceType, BigDecimal cost, double durationHours,
                         int rating, String notes) {
        this.serviceId     = serviceId;
        this.vehicleId     = vehicleId;
        this.serviceDate   = serviceDate;
        this.serviceType   = serviceType;
        this.cost          = cost;
        this.durationHours = durationHours;
        this.rating        = rating;
        this.notes         = notes;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public int        getServiceId()     { return serviceId; }
    public void       setServiceId(int serviceId) { this.serviceId = serviceId; }

    public int        getVehicleId()     { return vehicleId; }
    public void       setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }

    public LocalDate  getServiceDate()   { return serviceDate; }
    public void       setServiceDate(LocalDate serviceDate) { this.serviceDate = serviceDate; }

    public String     getServiceType()   { return serviceType; }
    public void       setServiceType(String serviceType) { this.serviceType = serviceType; }

    public BigDecimal getCost()          { return cost; }
    public void       setCost(BigDecimal cost) { this.cost = cost; }

    public double     getDurationHours() { return durationHours; }
    public void       setDurationHours(double durationHours) { this.durationHours = durationHours; }

    public int        getRating()        { return rating; }
    public void       setRating(int rating) { this.rating = rating; }

    public String     getNotes()         { return notes; }
    public void       setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        // Build star rating display (e.g., ★★★★☆ for 4)
        String stars = "★".repeat(rating) + "☆".repeat(5 - rating);
        return String.format(
            "Service [ID: %d | Vehicle: %d | Date: %s | Type: %-20s | Cost: ₹%-8.2f | Hours: %.1f | Rating: %s]",
            serviceId, vehicleId, serviceDate, serviceType, cost, durationHours, stars
        );
    }
}
