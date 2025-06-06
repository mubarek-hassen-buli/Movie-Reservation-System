package utils;

import model.User;

public class AuthService {
    // Use Windows-style path separators for Windows OS
    private static final String USERS_DIR = "files\\users\\";
    private static final String USERS_FILE = USERS_DIR + "users.txt";
    private static java.util.List<User> users = new java.util.ArrayList<>();

    static {
        // Create users directory if it doesn't exist
        java.io.File dir = new java.io.File(USERS_DIR);
        dir.mkdirs();
        
        // Load existing users from file
        loadUsers();
        
        // Add default admin if no users exist
        if (users.isEmpty()) {
            users.add(new User("admin", "admin123", "admin"));
            saveUsers();
        }
    }

    private static void loadUsers() {
        java.io.File file = new java.io.File(USERS_FILE);
        if (!file.exists()) return;

        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) { // Make sure we have all required fields
                    users.add(new User(data[0], data[1], data[2]));
                }
            }
        } catch (java.io.IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }

    private static void saveUsers() {
        try {
            // Make sure directory exists
            java.io.File dir = new java.io.File(USERS_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            // Use FileOutputStream to ensure file is created if it doesn't exist
            java.io.File file = new java.io.File(USERS_FILE);
            java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(file));
            
            for (User user : users) {
                writer.println(String.format("%s,%s,%s",
                    user.getUsername(),
                    user.getPassword(),
                    user.getRole()));
            }
            writer.close();
            System.out.println("Users saved to: " + USERS_FILE);
        } catch (java.io.IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
            e.printStackTrace(); // Add stack trace for better debugging
        }
    }

    public static User login(String username, String password) throws UserNotFoundException, IncorrectPasswordException {
        // Reload users to ensure up-to-date data
        loadUsers();
    
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                if (user.getPassword().equals(password)) {
                    return user;
                } else {
                    throw new IncorrectPasswordException("Incorrect password. Please try again.");
                }
            }
        }
        throw new UserNotFoundException("Username not found. Please register before you try to login or try again.");
    }
    

    public static void register(String username, String password, String role) throws Exception {
        // Validate username
        if (username == null || username.trim().isEmpty()) {
            throw new Exception("Username cannot be empty.");
        }

        // Check if username already exists
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                throw new Exception("Username already exists. Please choose a different username.");
            }
        }

        // Validate password
        if (password == null || password.length() < 6) {
            throw new Exception("Password must be at least 6 characters long.");
        }

        // Validate role
        if (!role.equals("admin") && !role.equals("client")) {
            throw new Exception("Invalid role. Role must be either 'admin' or 'client'.");
        }

        // Create and add new user
        users.add(new User(username, password, role));
        // Save users to file
        saveUsers();
        System.out.println("User registered and saved successfully!");
    }
}
