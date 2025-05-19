package ui;

import model.User;
import service.FileService;
import java.util.Scanner;

public class AdminPage {
    public static void showAdminPage(User user) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n==== ADMIN PAGE ====");
            System.out.println("Welcome, " + user.getUsername());
            System.out.println("1. Create File");
            System.out.println("2. View Files");
            System.out.println("3. Delete File");
            System.out.println("4. About Us");
            System.out.println("5. Logout");

            System.out.print("Enter choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> FileService.createFile();
                case 2 -> FileService.viewFiles();
                case 3 -> FileService.deleteFile();
                case 4 -> AboutPage.show();
                case 5 -> System.out.println("Logging out...");
                default -> System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 5);
    }
}
