package com.hyundai.vsms.model;

/**
 * Vehicle — POJO mapping to the 'vehicles' table.
 *
 * Notice the 'customerId' field — this represents the Foreign Key
 * relationship between vehicles and customers.
 */
public class Vehicle {

    private int    vehicleId;
    private int    customerId;
    private String model;
    private String licensePlate;
    private int    purchaseYear;

    // ── Constructors ──────────────────────────────────────────────────────────

    /** For inserting a new vehicle */
    public Vehicle(int customerId, String model, String licensePlate, int purchaseYear) {
        this.customerId   = customerId;
        this.model        = model;
        this.licensePlate = licensePlate;
        this.purchaseYear = purchaseYear;
    }

    /** For reading an existing vehicle from DB */
    public Vehicle(int vehicleId, int customerId, String model,
                   String licensePlate, int purchaseYear) {
        this.vehicleId    = vehicleId;
        this.customerId   = customerId;
        this.model        = model;
        this.licensePlate = licensePlate;
        this.purchaseYear = purchaseYear;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public int    getVehicleId()    { return vehicleId; }
    public void   setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }

    public int    getCustomerId()   { return customerId; }
    public void   setCustomerId(int customerId) { this.customerId = customerId; }

    public String getModel()        { return model; }
    public void   setModel(String model) { this.model = model; }

    public String getLicensePlate() { return licensePlate; }
    public void   setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public int    getPurchaseYear() { return purchaseYear; }
    public void   setPurchaseYear(int purchaseYear) { this.purchaseYear = purchaseYear; }

    @Override
    public String toString() {
        return String.format(
            "Vehicle [ID: %d | Model: %-18s | Plate: %-14s | Year: %d | CustomerID: %d]",
            vehicleId, model, licensePlate, purchaseYear, customerId
        );
    }
}
