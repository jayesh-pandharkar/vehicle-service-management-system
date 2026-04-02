package com.hyundai.vsms.dao;

import com.hyundai.vsms.model.Customer;
import com.hyundai.vsms.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CustomerDAO — Data Access Object
 *
 * ONLY this class is allowed to talk to the 'customers' table.
 * This separation keeps database logic away from business logic.
 * Think of DAO as the "gatekeeper" of a specific table.
 */
public class CustomerDAO {

    // ── CREATE ────────────────────────────────────────────────────────────────

    /**
     * Inserts a new customer into the database.
     *
     * We use PreparedStatement (not Statement) to:
     *  1. Prevent SQL Injection attacks
     *  2. Handle special characters in names automatically
     *
     * @param customer Customer object to insert
     * @return the generated customer_id, or -1 on failure
     */
    public int addCustomer(Customer customer) {
        String sql = "INSERT INTO customers (name, phone, email, city) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = DBConnection.getConnection()
                                       .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, customer.getName());
            ps.setString(2, customer.getPhone());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getCity());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                // Retrieve the auto-generated primary key
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int newId = generatedKeys.getInt(1);
                    customer.setCustomerId(newId);
                    return newId;
                }
            }

        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to add customer: " + e.getMessage());
        }
        return -1; // Indicates failure
    }

    // ── READ ──────────────────────────────────────────────────────────────────

    /**
     * Returns all customers from the database.
     */
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers ORDER BY customer_id";

        try (Statement stmt       = DBConnection.getConnection().createStatement();
             ResultSet rs         = stmt.executeQuery(sql)) {

            while (rs.next()) {
                customers.add(mapRowToCustomer(rs));
            }

        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to fetch customers: " + e.getMessage());
        }
        return customers;
    }

    /**
     * Finds a single customer by their ID.
     *
     * @param customerId The ID to look up
     * @return Customer object or null if not found
     */
    public Customer getCustomerById(int customerId) {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, customerId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRowToCustomer(rs);
            }

        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to fetch customer: " + e.getMessage());
        }
        return null;
    }

    /**
     * Search customers by name (case-insensitive, partial match).
     */
    public List<Customer> searchByName(String nameKeyword) {
        List<Customer> results = new ArrayList<>();
        String sql = "SELECT * FROM customers WHERE name LIKE ?";

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, "%" + nameKeyword + "%"); // Wildcard search

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(mapRowToCustomer(rs));
            }

        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to search customers: " + e.getMessage());
        }
        return results;
    }

    // ── PRIVATE HELPER ────────────────────────────────────────────────────────

    /**
     * Maps a single ResultSet row to a Customer object.
     * Extracted as a helper to avoid code duplication.
     */
    private Customer mapRowToCustomer(ResultSet rs) throws SQLException {
        return new Customer(
            rs.getInt("customer_id"),
            rs.getString("name"),
            rs.getString("phone"),
            rs.getString("email"),
            rs.getString("city")
        );
    }
}
