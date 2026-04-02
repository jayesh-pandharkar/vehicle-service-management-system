package com.hyundai.vsms.model;

/**
 * Customer — Plain Old Java Object (POJO)
 *
 * Maps directly to the 'customers' table in MySQL.
 * Each field corresponds to a column. This is the "Model" layer in MVC.
 */
public class Customer {

    private int    customerId;
    private String name;
    private String phone;
    private String email;
    private String city;

    // ── Constructors ──────────────────────────────────────────────────────────

    /** Used when creating a new customer (ID assigned by DB) */
    public Customer(String name, String phone, String email, String city) {
        this.name  = name;
        this.phone = phone;
        this.email = email;
        this.city  = city;
    }

    /** Used when reading an existing customer from the DB */
    public Customer(int customerId, String name, String phone, String email, String city) {
        this.customerId = customerId;
        this.name       = name;
        this.phone      = phone;
        this.email      = email;
        this.city       = city;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public int    getCustomerId() { return customerId; }
    public void   setCustomerId(int customerId) { this.customerId = customerId; }

    public String getName()  { return name; }
    public void   setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void   setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void   setEmail(String email) { this.email = email; }

    public String getCity()  { return city; }
    public void   setCity(String city) { this.city = city; }

    // ── toString — used for printing in the console ───────────────────────────
    @Override
    public String toString() {
        return String.format(
            "Customer [ID: %d | Name: %-20s | Phone: %s | City: %s]",
            customerId, name, phone, city
        );
    }
}
