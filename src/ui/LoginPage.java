package ui;

import model.User;
import utils.AuthService;
import utils.UserNotFoundException;
import utils.IncorrectPasswordException;

import java.util.Scanner;
import java.io.IOException;

public class LoginPage {

    private static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
        } catch (IOException | InterruptedException ex) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }

    public static void showLogin() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            clearScreen();
            System.out.println("==== WELCOME ====");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Choose an option (1-3): ");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    handleLogin(scanner);
                    break;
                case "2":
                    handleRegistration(scanner);
                    break;
                case "3":
                    System.out.println("Thank you for using Movie Reservation System. Goodbye!");
                    return;
                default:
                    System.out.println("⚠️  Invalid option. Please try again.");
                    pause(scanner);
            }
        }
    }

    private static void handleLogin(Scanner scanner) {
        System.out.println("\n==== LOGIN ====");
        System.out.print("Username: ");
        String username = scanner.nextLine();

        String password = readPassword(scanner);

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("⚠️  Username and password cannot be empty.");
            pause(scanner);
            return;
        }

        try {
            User user = AuthService.login(username, password);
            System.out.println("Login successful! Welcome, " + user.getUsername() + ".");

            if (user.getRole().equalsIgnoreCase("admin")) {
                AdminPage.showAdminPage(user);
            } else {
                // If user logs out from client page, clear the screen and show logout message
                boolean shouldLogout = ClientPage.showClientPage(user);
                if (shouldLogout) {
                    clearScreen();
                    System.out.println("You have been successfully logged out.");
                    System.out.print("Press Enter to continue...");
                    scanner.nextLine();
                    return;  // Return to login screen
                }
            }

        } catch (UserNotFoundException | IncorrectPasswordException e) {
            System.out.println("⚠️  Login failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("⚠️  Unexpected error occurred: " + e.getMessage());
        }

        pause(scanner);
    }

    private static void handleRegistration(Scanner scanner) {
        System.out.println("\n==== REGISTRATION ====");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        String password = readPassword(scanner);

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("⚠️  Username and password cannot be empty.");
            pause(scanner);
            return;
        }
        
        // Set default role as client
        String role = "client";
        System.out.println("Role set to: " + role);

        try {
            AuthService.register(username, password, role);
            User user = AuthService.login(username, password);
            System.out.println("✅ Registration successful! Welcome, " + user.getUsername() + ".");

            if (user.getRole().equalsIgnoreCase("admin")) {
                AdminPage.showAdminPage(user);
            } else {
                // If user logs out from client page, clear the screen and show logout message
                boolean shouldLogout = ClientPage.showClientPage(user);
                if (shouldLogout) {
                    clearScreen();
                    System.out.println("You have been successfully logged out.");
                    System.out.print("Press Enter to continue...");
                    scanner.nextLine();
                    return;  // Return to login screen
                }
            }

        } catch (Exception e) {
            System.out.println("⚠️  Registration failed: " + e.getMessage());
        }

        pause(scanner);
    }

    private static String readPassword(Scanner scanner) {
        // For IDEs: fallback to visible input
        System.out.print("Password: ");
        return scanner.nextLine();
    }

    private static void pause(Scanner scanner) {
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
}
