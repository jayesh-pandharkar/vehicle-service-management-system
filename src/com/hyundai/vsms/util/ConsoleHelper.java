package com.hyundai.vsms.util;

/**
 * ConsoleHelper — Utility class for formatted console output.
 *
 * Centralizes all the "pretty printing" logic so the main menu
 * code stays clean and readable.
 */
public class ConsoleHelper {

    // ANSI colour codes for terminal styling
    public static final String RESET  = "\u001B[0m";
    public static final String BOLD   = "\u001B[1m";
    public static final String CYAN   = "\u001B[36m";
    public static final String GREEN  = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED    = "\u001B[31m";
    public static final String BLUE   = "\u001B[34m";

    /** Prints a section header with a box border */
    public static void printHeader(String title) {
        int width = 60;
        String line = "═".repeat(width);
        System.out.println("\n" + CYAN + BOLD + "╔" + line + "╗");
        System.out.printf("║  %-" + (width - 2) + "s║%n", title);
        System.out.println("╚" + line + "╝" + RESET);
    }

    /** Prints a simple separator line */
    public static void printDivider() {
        System.out.println(CYAN + "  " + "─".repeat(58) + RESET);
    }

    /** Prints a success message in green */
    public static void success(String msg) {
        System.out.println(GREEN + "  ✔  " + msg + RESET);
    }

    /** Prints an error message in red */
    public static void error(String msg) {
        System.out.println(RED + "  ✘  " + msg + RESET);
    }

    /** Prints an info message in yellow */
    public static void info(String msg) {
        System.out.println(YELLOW + "  ℹ  " + msg + RESET);
    }

    /** Prompts the user for input */
    public static void prompt(String label) {
        System.out.print(BOLD + "  » " + label + ": " + RESET);
    }
}
