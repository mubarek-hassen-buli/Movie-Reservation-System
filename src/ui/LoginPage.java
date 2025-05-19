package ui;

import model.User;
import utils.AuthService;
import java.util.Scanner;

public class LoginPage {
    public static void showLogin() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("==== LOGIN PAGE ====");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        User user = AuthService.login(username, password);

        if (user == null) {
            System.out.println("Invalid credentials. Try again.");
        } else {
            System.out.println("Login successful! Welcome, " + user.getUsername() + ".");
            if (user.getRole().equals("admin")) {
                AdminPage.showAdminPage(user);
            } else {
                ClientPage.showClientPage(user);
            }
        }
    }
}
