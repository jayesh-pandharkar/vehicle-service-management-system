package com.hyundai.vsms.dao;

import com.hyundai.vsms.model.ServiceRecord;
import com.hyundai.vsms.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * ServiceDAO — Handles all DB operations for the 'services' table.
 *
 * This is the most feature-rich DAO — it includes standard CRUD
 * AND advanced SQL queries using JOIN, GROUP BY, and aggregations.
 */
public class ServiceDAO {

    // ── CREATE ────────────────────────────────────────────────────────────────

    /**
     * Inserts a new service record for a vehicle.
     *
     * @return generated service_id, or -1 on failure
     */
    public int addServiceRecord(ServiceRecord record) {
        String sql = "INSERT INTO services "
                   + "(vehicle_id, service_date, service_type, cost, duration_hours, rating, notes) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = DBConnection.getConnection()
                                       .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1,        record.getVehicleId());
            ps.setDate(2,       Date.valueOf(record.getServiceDate())); // LocalDate → java.sql.Date
            ps.setString(3,     record.getServiceType());
            ps.setBigDecimal(4, record.getCost());
            ps.setDouble(5,     record.getDurationHours());
            ps.setInt(6,        record.getRating());
            ps.setString(7,     record.getNotes());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    int newId = keys.getInt(1);
                    record.setServiceId(newId);
                    return newId;
                }
            }

        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to add service record: " + e.getMessage());
        }
        return -1;
    }

    // ── READ ──────────────────────────────────────────────────────────────────

    /**
     * Returns full service history for a specific vehicle.
     * Ordered newest-first so the latest service shows up at the top.
     */
    public List<ServiceRecord> getServicesByVehicle(int vehicleId) {
        List<ServiceRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM services WHERE vehicle_id = ? ORDER BY service_date DESC";

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, vehicleId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                records.add(mapRowToServiceRecord(rs));
            }

        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to fetch service history: " + e.getMessage());
        }
        return records;
    }

    /**
     * ADVANCED QUERY #1 — Service history by customer using JOIN.
     *
     * SQL: JOIN vehicles + services + filter by customer_id
     * This returns all services across all of a customer's vehicles.
     *
     * Interview Talking Point: "I wrote a 3-table JOIN query to retrieve
     * aggregated service history at the customer level."
     */
    public List<ServiceRecord> getServicesByCustomer(int customerId) {
        List<ServiceRecord> records = new ArrayList<>();

        // JOIN: services → vehicles → (filter by customer_id)
        String sql = "SELECT s.* FROM services s "
                   + "JOIN vehicles v ON s.vehicle_id = v.vehicle_id "
                   + "WHERE v.customer_id = ? "
                   + "ORDER BY s.service_date DESC";

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                records.add(mapRowToServiceRecord(rs));
            }

        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to fetch services by customer: " + e.getMessage());
        }
        return records;
    }

    // ── REPORTS (Advanced SQL) ────────────────────────────────────────────────

    /**
     * REPORT 1: Total Revenue
     * SQL: SUM(cost) — simple aggregation
     */
    public BigDecimal getTotalRevenue() {
        String sql = "SELECT COALESCE(SUM(cost), 0) AS total_revenue FROM services";

        try (Statement stmt = DBConnection.getConnection().createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getBigDecimal("total_revenue");
            }

        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to calculate total revenue: " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }

    /**
     * REPORT 2: Most Frequent Service Types
     *
     * ADVANCED QUERY #2 — GROUP BY + COUNT + ORDER BY
     * Groups all service records by type and counts how many times each
     * service was performed. Returns a sorted map (type → count).
     *
     * SQL Pattern: SELECT col, COUNT(*) FROM table GROUP BY col ORDER BY COUNT(*) DESC
     */
    public Map<String, Integer> getMostFrequentServiceTypes() {
        // LinkedHashMap preserves insertion order (already sorted by DB)
        Map<String, Integer> result = new LinkedHashMap<>();

        String sql = "SELECT service_type, COUNT(*) AS service_count "
                   + "FROM services "
                   + "GROUP BY service_type "
                   + "ORDER BY service_count DESC";

        try (Statement stmt = DBConnection.getConnection().createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {

            while (rs.next()) {
                result.put(rs.getString("service_type"), rs.getInt("service_count"));
            }

        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to fetch service frequency: " + e.getMessage());
        }
        return result;
    }

    /**
     * REPORT 3: Average Customer Rating
     *
     * ADVANCED QUERY #3 — AVG() aggregation + JOIN across 3 tables
     * Joins customers → vehicles → services, groups by customer,
     * and calculates the average rating per customer.
     */
    public void printAverageRatingPerCustomer() {
        String sql = "SELECT c.name, "
                   + "       ROUND(AVG(s.rating), 2) AS avg_rating, "
                   + "       COUNT(s.service_id)     AS total_services, "
                   + "       SUM(s.cost)              AS total_spent "
                   + "FROM customers c "
                   + "JOIN vehicles v  ON c.customer_id = v.customer_id "
                   + "JOIN services s  ON v.vehicle_id  = s.vehicle_id "
                   + "GROUP BY c.customer_id, c.name "
                   + "ORDER BY avg_rating DESC";

        System.out.println("\n  ┌─────────────────────────────────────────────────────────────────────┐");
        System.out.println("  │          CUSTOMER SATISFACTION REPORT                               │");
        System.out.println("  ├──────────────────────┬────────────┬─────────────┬───────────────────┤");
        System.out.printf( "  │ %-20s │ Avg Rating │ # Services  │ Total Spent       │%n", "Customer Name");
        System.out.println("  ├──────────────────────┼────────────┼─────────────┼───────────────────┤");

        try (Statement stmt = DBConnection.getConnection().createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String stars = "★".repeat((int) Math.round(rs.getDouble("avg_rating")));
                System.out.printf(
                    "  │ %-20s │ %4.2f  %-4s │ %11d │ ₹%-17.2f│%n",
                    rs.getString("name"),
                    rs.getDouble("avg_rating"),
                    stars,
                    rs.getInt("total_services"),
                    rs.getBigDecimal("total_spent")
                );
            }

        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to fetch rating report: " + e.getMessage());
        }
        System.out.println("  └──────────────────────┴────────────┴─────────────┴───────────────────┘");
    }

    /**
     * REPORT 4: Monthly Revenue — GROUP BY MONTH
     * Breaks down total revenue month by month for the current year.
     */
    public void printMonthlyRevenueReport() {
        String sql = "SELECT MONTHNAME(service_date) AS month_name, "
                   + "       MONTH(service_date)     AS month_num, "
                   + "       COUNT(*)                AS jobs, "
                   + "       SUM(cost)               AS revenue "
                   + "FROM services "
                   + "WHERE YEAR(service_date) = YEAR(CURDATE()) "
                   + "GROUP BY month_num, month_name "
                   + "ORDER BY month_num";

        System.out.println("\n  ┌─────────────────────────────────────────────────────┐");
        System.out.println("  │           MONTHLY REVENUE REPORT (Current Year)     │");
        System.out.println("  ├──────────────┬───────────────┬───────────────────────┤");
        System.out.printf( "  │ %-12s │ Jobs Done     │ Revenue               │%n", "Month");
        System.out.println("  ├──────────────┼───────────────┼───────────────────────┤");

        try (Statement stmt = DBConnection.getConnection().createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                System.out.printf(
                    "  │ %-12s │ %13d │ ₹%-20.2f│%n",
                    rs.getString("month_name"),
                    rs.getInt("jobs"),
                    rs.getBigDecimal("revenue")
                );
            }
            if (!hasData) {
                System.out.println("  │ No service records found for current year.          │");
            }

        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to fetch monthly revenue: " + e.getMessage());
        }
        System.out.println("  └──────────────┴───────────────┴───────────────────────┘");
    }

    // ── PRIVATE HELPER ────────────────────────────────────────────────────────

    private ServiceRecord mapRowToServiceRecord(ResultSet rs) throws SQLException {
        return new ServiceRecord(
            rs.getInt("service_id"),
            rs.getInt("vehicle_id"),
            rs.getDate("service_date").toLocalDate(), // java.sql.Date → LocalDate
            rs.getString("service_type"),
            rs.getBigDecimal("cost"),
            rs.getDouble("duration_hours"),
            rs.getInt("rating"),
            rs.getString("notes")
        );
    }
}
