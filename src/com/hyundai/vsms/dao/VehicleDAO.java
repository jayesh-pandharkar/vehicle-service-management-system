package com.hyundai.vsms.dao;

import com.hyundai.vsms.model.Vehicle;
import com.hyundai.vsms.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * VehicleDAO — Handles all DB operations for the 'vehicles' table.
 */
public class VehicleDAO {

    // ── CREATE ────────────────────────────────────────────────────────────────

    /**
     * Adds a new vehicle record linked to a customer.
     *
     * @param vehicle Vehicle object (must have a valid customerId)
     * @return generated vehicle_id, or -1 on failure
     */
    public int addVehicle(Vehicle vehicle) {
        String sql = "INSERT INTO vehicles (customer_id, model, license_plate, purchase_year) "
                   + "VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = DBConnection.getConnection()
                                       .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1,    vehicle.getCustomerId());
            ps.setString(2, vehicle.getModel());
            ps.setString(3, vehicle.getLicensePlate());
            ps.setInt(4,    vehicle.getPurchaseYear());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    int newId = keys.getInt(1);
                    vehicle.setVehicleId(newId);
                    return newId;
                }
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            // This happens if license_plate is not unique
            System.err.println("[ERROR] License plate already exists: " + vehicle.getLicensePlate());
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to add vehicle: " + e.getMessage());
        }
        return -1;
    }

    // ── READ ──────────────────────────────────────────────────────────────────

    /**
     * Returns all vehicles for a specific customer.
     * Used when viewing a customer's garage.
     */
    public List<Vehicle> getVehiclesByCustomer(int customerId) {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles WHERE customer_id = ? ORDER BY purchase_year DESC";

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                vehicles.add(mapRowToVehicle(rs));
            }

        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to fetch vehicles: " + e.getMessage());
        }
        return vehicles;
    }

    /**
     * Returns all vehicles in the system.
     */
    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles ORDER BY vehicle_id";

        try (Statement stmt = DBConnection.getConnection().createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {

            while (rs.next()) {
                vehicles.add(mapRowToVehicle(rs));
            }

        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to fetch all vehicles: " + e.getMessage());
        }
        return vehicles;
    }

    /**
     * Finds a vehicle by its ID.
     */
    public Vehicle getVehicleById(int vehicleId) {
        String sql = "SELECT * FROM vehicles WHERE vehicle_id = ?";

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, vehicleId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRowToVehicle(rs);
            }

        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to fetch vehicle: " + e.getMessage());
        }
        return null;
    }

    // ── PRIVATE HELPER ────────────────────────────────────────────────────────

    private Vehicle mapRowToVehicle(ResultSet rs) throws SQLException {
        return new Vehicle(
            rs.getInt("vehicle_id"),
            rs.getInt("customer_id"),
            rs.getString("model"),
            rs.getString("license_plate"),
            rs.getInt("purchase_year")
        );
    }
}
