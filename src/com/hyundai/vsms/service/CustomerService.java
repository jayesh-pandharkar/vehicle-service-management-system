package com.hyundai.vsms.service;

import com.hyundai.vsms.dao.CustomerDAO;
import com.hyundai.vsms.model.Customer;

import java.util.List;

/**
 * CustomerService — Business Logic Layer
 *
 * Why do we need this layer if DAO already talks to DB?
 *
 * Answer: The Service layer is where BUSINESS RULES live.
 * For example:
 *  - "A customer must have a valid 10-digit phone number"
 *  - "Email is optional but if given, must contain @"
 *  - "Customer name cannot be empty"
 *
 * The DAO doesn't know about these rules — it just saves/fetches data.
 * The Service layer validates BEFORE passing to the DAO.
 *
 * This separation makes the code:
 *  ✔ Testable (you can unit-test validation without a DB)
 *  ✔ Maintainable (business rules in one place)
 *  ✔ Scalable (easy to swap DAO if DB changes)
 */
public class CustomerService {

    // The service layer USES the DAO — it does not extend it
    private final CustomerDAO customerDAO = new CustomerDAO();

    /**
     * Validates and adds a new customer.
     *
     * @return new customer ID, or -1 on failure
     */
    public int addCustomer(String name, String phone, String email, String city) {

        // ── Business Rule Validation ──────────────────────────────────────────

        if (name == null || name.trim().isEmpty()) {
            System.out.println("[VALIDATION] Customer name cannot be empty.");
            return -1;
        }

        if (phone == null || !phone.matches("\\d{10}")) {
            System.out.println("[VALIDATION] Phone number must be exactly 10 digits.");
            return -1;
        }

        if (email != null && !email.isEmpty() && !email.contains("@")) {
            System.out.println("[VALIDATION] Invalid email address.");
            return -1;
        }

        if (city == null || city.trim().isEmpty()) {
            System.out.println("[VALIDATION] City cannot be empty.");
            return -1;
        }

        // All validations passed → delegate to DAO
        Customer customer = new Customer(
            name.trim(),
            phone.trim(),
            (email != null ? email.trim() : null),
            city.trim()
        );

        int id = customerDAO.addCustomer(customer);
        if (id > 0) {
            System.out.println("[SUCCESS] Customer added with ID: " + id);
        }
        return id;
    }

    /**
     * Returns all customers. No business logic needed here — just forward.
     */
    public List<Customer> getAllCustomers() {
        return customerDAO.getAllCustomers();
    }

    /**
     * Looks up a customer by ID. Returns null if not found.
     */
    public Customer getCustomerById(int id) {
        Customer c = customerDAO.getCustomerById(id);
        if (c == null) {
            System.out.println("[INFO] No customer found with ID: " + id);
        }
        return c;
    }

    /**
     * Search by name keyword.
     */
    public List<Customer> searchCustomers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("[INFO] Search keyword is empty — returning all customers.");
            return customerDAO.getAllCustomers();
        }
        return customerDAO.searchByName(keyword.trim());
    }
}
