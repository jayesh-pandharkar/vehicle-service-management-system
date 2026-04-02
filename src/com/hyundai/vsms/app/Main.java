package com.hyundai.vsms.app;

import com.hyundai.vsms.model.Customer;
import com.hyundai.vsms.model.ServiceRecord;
import com.hyundai.vsms.model.Vehicle;
import com.hyundai.vsms.service.CustomerService;
import com.hyundai.vsms.service.ServiceRecordService;
import com.hyundai.vsms.service.VehicleService;
import com.hyundai.vsms.util.ConsoleHelper;
import com.hyundai.vsms.util.DBConnection;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * ╔══════════════════════════════════════════════════════════════╗
 * ║    HYUNDAI MOTOR COMPANY — Vehicle Service Management System  ║
 * ║    Main Application Entry Point                               ║
 * ╚══════════════════════════════════════════════════════════════╝
 *
 * This is the CONTROLLER layer in MVC.
 * It handles user input, calls the appropriate Service layer method,
 * and displays results — but contains NO business logic itself.
 *
 * Architecture Flow:
 *   User Input → Main (Controller) → Service Layer → DAO → MySQL DB
 */
public class Main {

    // One Scanner for the whole app — never create multiple Scanners for System.in
    private static final Scanner scanner = new Scanner(System.in);

    // Service layer instances — these are our "tools"
    private static final CustomerService       customerService = new CustomerService();
    private static final VehicleService        vehicleService  = new VehicleService();
    private static final ServiceRecordService  serviceRecordService = new ServiceRecordService();

    // ── APPLICATION ENTRY POINT ───────────────────────────────────────────────

    public static void main(String[] args) {
        printWelcomeBanner();

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("Enter your choice");

            switch (choice) {
                // ── CUSTOMER MENU ──
                case 1  -> handleAddCustomer();
                case 2  -> handleViewAllCustomers();
                case 3  -> handleSearchCustomer();

                // ── VEHICLE MENU ──
                case 4  -> handleAddVehicle();
                case 5  -> handleViewVehiclesByCustomer();

                // ── SERVICE MENU ──
                case 6  -> handleAddServiceRecord();
                case 7  -> handleViewServicesByVehicle();
                case 8  -> handleViewServicesByCustomer();

                // ── REPORTS ──
                case 9  -> handleTotalRevenueReport();
                case 10 -> handleServiceFrequencyReport();
                case 11 -> handleCustomerRatingReport();
                case 12 -> handleMonthlyRevenueReport();

                // ── EXIT ──
                case 0 -> {
                    running = false;
                    ConsoleHelper.info("Thank you for using VSMS. Goodbye!");
                    DBConnection.closeConnection();
                }
                default -> ConsoleHelper.error("Invalid choice. Please enter a number from the menu.");
            }
        }

        scanner.close();
    }

    // ── DISPLAY METHODS ───────────────────────────────────────────────────────

    private static void printWelcomeBanner() {
        System.out.println(ConsoleHelper.CYAN + ConsoleHelper.BOLD);
        System.out.println("  ██╗  ██╗██╗   ██╗██╗   ██╗███╗   ██╗██████╗  █████╗ ██╗");
        System.out.println("  ██║  ██║╚██╗ ██╔╝██║   ██║████╗  ██║██╔══██╗██╔══██╗██║");
        System.out.println("  ███████║ ╚████╔╝ ██║   ██║██╔██╗ ██║██║  ██║███████║██║");
        System.out.println("  ██╔══██║  ╚██╔╝  ██║   ██║██║╚██╗██║██║  ██║██╔══██║██║");
        System.out.println("  ██║  ██║   ██║   ╚██████╔╝██║ ╚████║██████╔╝██║  ██║██║");
        System.out.println("  ╚═╝  ╚═╝   ╚═╝    ╚═════╝ ╚═╝  ╚═══╝╚═════╝ ╚═╝  ╚═╝╚═╝");
        System.out.println(ConsoleHelper.RESET);
        System.out.println(ConsoleHelper.YELLOW + "       Vehicle Service Management System  v1.0" + ConsoleHelper.RESET);
        System.out.println(ConsoleHelper.YELLOW + "       Hyundai Motor Company — Nashik Service Center" + ConsoleHelper.RESET);
    }

    private static void printMainMenu() {
        System.out.println("\n" + ConsoleHelper.BOLD + ConsoleHelper.BLUE);
        System.out.println("  ┌─────────────────────────────────────────────┐");
        System.out.println("  │                  MAIN MENU                  │");
        System.out.println("  ├─────────────────────────────────────────────┤");
        System.out.println("  │  CUSTOMERS                                  │");
        System.out.println("  │   [1] Add New Customer                      │");
        System.out.println("  │   [2] View All Customers                    │");
        System.out.println("  │   [3] Search Customer by Name               │");
        System.out.println("  ├─────────────────────────────────────────────┤");
        System.out.println("  │  VEHICLES                                   │");
        System.out.println("  │   [4] Add Vehicle to Customer               │");
        System.out.println("  │   [5] View Vehicles by Customer             │");
        System.out.println("  ├─────────────────────────────────────────────┤");
        System.out.println("  │  SERVICE RECORDS                            │");
        System.out.println("  │   [6] Add Service Record                    │");
        System.out.println("  │   [7] View Service History (by Vehicle)     │");
        System.out.println("  │   [8] View Service History (by Customer)    │");
        System.out.println("  ├─────────────────────────────────────────────┤");
        System.out.println("  │  REPORTS                                    │");
        System.out.println("  │   [9]  Total Revenue Report                 │");
        System.out.println("  │   [10] Most Frequent Service Types          │");
        System.out.println("  │   [11] Customer Satisfaction Report         │");
        System.out.println("  │   [12] Monthly Revenue Breakdown            │");
        System.out.println("  ├─────────────────────────────────────────────┤");
        System.out.println("  │   [0] Exit                                  │");
        System.out.println("  └─────────────────────────────────────────────┘" + ConsoleHelper.RESET);
    }

    // ── CUSTOMER HANDLERS ─────────────────────────────────────────────────────

    private static void handleAddCustomer() {
        ConsoleHelper.printHeader("ADD NEW CUSTOMER");

        ConsoleHelper.prompt("Full Name");
        String name = scanner.nextLine().trim();

        ConsoleHelper.prompt("Phone Number (10 digits)");
        String phone = scanner.nextLine().trim();

        ConsoleHelper.prompt("Email Address (optional, press Enter to skip)");
        String email = scanner.nextLine().trim();

        ConsoleHelper.prompt("City");
        String city = scanner.nextLine().trim();

        customerService.addCustomer(name, phone, email.isEmpty() ? null : email, city);
    }

    private static void handleViewAllCustomers() {
        ConsoleHelper.printHeader("ALL CUSTOMERS");

        List<Customer> customers = customerService.getAllCustomers();
        if (customers.isEmpty()) {
            ConsoleHelper.info("No customers found.");
            return;
        }

        System.out.printf("  %-5s %-22s %-14s %-25s %-12s%n",
                          "ID", "Name", "Phone", "Email", "City");
        ConsoleHelper.printDivider();

        for (Customer c : customers) {
            System.out.printf("  %-5d %-22s %-14s %-25s %-12s%n",
                              c.getCustomerId(), c.getName(), c.getPhone(),
                              (c.getEmail() != null ? c.getEmail() : "—"),
                              c.getCity());
        }
        ConsoleHelper.info("Total customers: " + customers.size());
    }

    private static void handleSearchCustomer() {
        ConsoleHelper.printHeader("SEARCH CUSTOMER");
        ConsoleHelper.prompt("Enter name keyword");
        String keyword = scanner.nextLine().trim();

        List<Customer> results = customerService.searchCustomers(keyword);
        if (results.isEmpty()) {
            ConsoleHelper.info("No customers match: \"" + keyword + "\"");
        } else {
            results.forEach(c -> System.out.println("  " + c));
        }
    }

    // ── VEHICLE HANDLERS ──────────────────────────────────────────────────────

    private static void handleAddVehicle() {
        ConsoleHelper.printHeader("ADD VEHICLE");

        int customerId = readInt("Customer ID");
        ConsoleHelper.prompt("Vehicle Model (e.g. Hyundai Creta)");
        String model = scanner.nextLine().trim();

        ConsoleHelper.prompt("License Plate (e.g. MH-15-AB-1234)");
        String plate = scanner.nextLine().trim();

        int year = readInt("Purchase Year (e.g. 2022)");

        vehicleService.addVehicle(customerId, model, plate, year);
    }

    private static void handleViewVehiclesByCustomer() {
        ConsoleHelper.printHeader("VEHICLES BY CUSTOMER");
        int customerId = readInt("Customer ID");

        List<Vehicle> vehicles = vehicleService.getVehiclesByCustomer(customerId);
        if (vehicles.isEmpty()) {
            ConsoleHelper.info("No vehicles found.");
        } else {
            vehicles.forEach(v -> System.out.println("  " + v));
        }
    }

    // ── SERVICE RECORD HANDLERS ───────────────────────────────────────────────

    private static void handleAddServiceRecord() {
        ConsoleHelper.printHeader("ADD SERVICE RECORD");

        int vehicleId = readInt("Vehicle ID");
        ConsoleHelper.prompt("Service Type (e.g. Oil Change, Full Service, AC Service)");
        String serviceType = scanner.nextLine().trim();

        BigDecimal cost = readDecimal("Cost (in ₹)");
        double duration  = readDouble("Duration (hours, e.g. 1.5)");
        int rating       = readInt("Customer Rating (1-5)");

        ConsoleHelper.prompt("Service Date (YYYY-MM-DD, press Enter for today)");
        String dateStr   = scanner.nextLine().trim();

        ConsoleHelper.prompt("Notes (optional, press Enter to skip)");
        String notes     = scanner.nextLine().trim();

        LocalDate date;
        if (dateStr.isEmpty()) {
            date = LocalDate.now();
        } else {
            try {
                date = LocalDate.parse(dateStr);
            } catch (DateTimeParseException e) {
                ConsoleHelper.error("Invalid date format. Use YYYY-MM-DD.");
                return;
            }
        }

        serviceRecordService.addServiceRecord(
            vehicleId, date, serviceType, cost, duration, rating,
            notes.isEmpty() ? null : notes
        );
    }

    private static void handleViewServicesByVehicle() {
        ConsoleHelper.printHeader("SERVICE HISTORY BY VEHICLE");
        int vehicleId = readInt("Vehicle ID");

        List<ServiceRecord> records = serviceRecordService.getServiceHistoryByVehicle(vehicleId);
        if (!records.isEmpty()) {
            ConsoleHelper.printDivider();
            records.forEach(r -> System.out.println("  " + r));
            ConsoleHelper.printDivider();
            ConsoleHelper.info("Total records: " + records.size());
        }
    }

    private static void handleViewServicesByCustomer() {
        ConsoleHelper.printHeader("SERVICE HISTORY BY CUSTOMER");
        int customerId = readInt("Customer ID");

        List<ServiceRecord> records = serviceRecordService.getServiceHistoryByCustomer(customerId);
        if (!records.isEmpty()) {
            ConsoleHelper.printDivider();
            records.forEach(r -> System.out.println("  " + r));
            ConsoleHelper.printDivider();
            ConsoleHelper.info("Total records: " + records.size());
        }
    }

    // ── REPORT HANDLERS ───────────────────────────────────────────────────────

    private static void handleTotalRevenueReport() {
        ConsoleHelper.printHeader("TOTAL REVENUE REPORT");
        BigDecimal revenue = serviceRecordService.getTotalRevenue();
        System.out.println();
        System.out.println("  ┌────────────────────────────────────────┐");
        System.out.printf( "  │  Total Revenue Generated : ₹%-10.2f│%n", revenue);
        System.out.println("  └────────────────────────────────────────┘");
    }

    private static void handleServiceFrequencyReport() {
        ConsoleHelper.printHeader("MOST FREQUENT SERVICE TYPES");
        Map<String, Integer> freq = serviceRecordService.getMostFrequentServiceTypes();

        if (freq.isEmpty()) {
            ConsoleHelper.info("No service data available.");
            return;
        }

        System.out.println();
        System.out.println("  ┌──────────────────────────────┬──────────────┐");
        System.out.printf( "  │ %-28s │ Times Done   │%n", "Service Type");
        System.out.println("  ├──────────────────────────────┼──────────────┤");

        freq.forEach((type, count) -> {
            String bar = "█".repeat(count); // Mini ASCII bar chart!
            System.out.printf("  │ %-28s │ %3d  %-7s │%n", type, count, bar);
        });
        System.out.println("  └──────────────────────────────┴──────────────┘");
    }

    private static void handleCustomerRatingReport() {
        ConsoleHelper.printHeader("CUSTOMER SATISFACTION REPORT");
        serviceRecordService.printCustomerRatingReport();
    }

    private static void handleMonthlyRevenueReport() {
        ConsoleHelper.printHeader("MONTHLY REVENUE REPORT");
        serviceRecordService.printMonthlyRevenueReport();
    }

    // ── INPUT HELPERS ─────────────────────────────────────────────────────────

    /**
     * Reads an integer from user input with error handling.
     * Loops until a valid integer is entered.
     */
    private static int readInt(String label) {
        while (true) {
            ConsoleHelper.prompt(label);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                ConsoleHelper.error("Please enter a valid whole number.");
            }
        }
    }

    /**
     * Reads a decimal number (BigDecimal) for monetary values.
     */
    private static BigDecimal readDecimal(String label) {
        while (true) {
            ConsoleHelper.prompt(label);
            String input = scanner.nextLine().trim();
            try {
                BigDecimal val = new BigDecimal(input);
                if (val.compareTo(BigDecimal.ZERO) > 0) return val;
                ConsoleHelper.error("Value must be greater than 0.");
            } catch (NumberFormatException e) {
                ConsoleHelper.error("Please enter a valid decimal number (e.g. 1500.00).");
            }
        }
    }

    /**
     * Reads a double for duration values.
     */
    private static double readDouble(String label) {
        while (true) {
            ConsoleHelper.prompt(label);
            String input = scanner.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                ConsoleHelper.error("Please enter a valid number (e.g. 1.5).");
            }
        }
    }
}
