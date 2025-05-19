package utils;

import model.User;
import java.util.ArrayList;
import java.util.List;

public class AuthService {
    private static List<User> users = new ArrayList<>();

    static {
        // Hardcoded users
        users.add(new User("admin", "admin123", "admin"));     // Representative
        users.add(new User("student1", "pass1", "client"));    // Classmate
        users.add(new User("student2", "pass2", "client"));    // Classmate
    }

    public static User login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null; // Invalid credentials
    }
}
