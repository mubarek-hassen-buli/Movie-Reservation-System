package ui;

import java.util.Scanner;

public class AboutPage {
    public static void show(java.util.Scanner scanner) {
        System.out.println("\n========== About Us ==========");
        System.out.println("Movie Reservation System for DDU 3rd Year Software Engineering Students");
        System.out.println("Developed by: Group 8");
        System.out.println("Members: ");
        System.out.println("1. Mubarak Hassen ID: 1501481");
        System.out.println("2. Abdlber Rejeb ID: 1500757");
        System.out.println("3. Hamza Safi ID: 1501212");
        System.out.println("4. Firagos Jemal ID: 1501147");
        System.out.println("5. Hermela Meaza ID: 1501259");
        // System.out.println("Admin : Handles file operations");
        // System.out.println("Clients : Access and view movies");
        System.out.println("University: Dire Dawa University");
        System.out.println("==============================\n");
        System.out.print("Press Enter to continue...");
        scanner.nextLine(); // Clear any existing input
        scanner.nextLine(); // Wait for Enter key
    }
}

