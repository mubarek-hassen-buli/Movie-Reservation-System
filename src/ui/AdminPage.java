package ui;

import model.Movie;
import model.User;
import service.MovieService;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.io.IOException;

public class AdminPage {
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
    private static Scanner scanner = new Scanner(System.in);

    public static void showAdminPage(User admin) {
        while (true) {
            clearScreen();
            System.out.println("==== ADMIN DASHBOARD ====");
            System.out.println("1. Add New Movie");
            System.out.println("2. View All Movies");
            System.out.println("3. Delete Movie");
            System.out.println("4. Update Movie Seats");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addNewMovie();
                    break;
                case "2":
                    viewAllMovies();
                    break;
                case "3":
                    deleteMovie();
                    break;
                case "4":
                    updateMovieSeats();
                    break;
                case "5":
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private static void addNewMovie() {
        System.out.println("\n==== ADD NEW MOVIE ====");
        
        System.out.print("Enter movie title: ");
        String title = scanner.nextLine();
        
        System.out.print("Enter duration (e.g., 2h 30m): ");
        String duration = scanner.nextLine();
        
        System.out.print("Enter showtime (e.g., 2024-01-20 18:30): ");
        String showtime = scanner.nextLine();
        
        System.out.print("Enter number of available seats: ");
        int seats = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Enter ticket price: ");
        double price = Double.parseDouble(scanner.nextLine());

        // Get all movies to determine next ID
        List<Movie> existingMovies = MovieService.getAllMovies();
        int nextNumber = existingMovies.size() + 1;
        String movieId = String.format("mov%d", nextNumber);
        Movie movie = new Movie(movieId, title, duration, showtime, seats, price);
        MovieService.addMovie(movie);
    }

    private static void viewAllMovies() {
        clearScreen();
        System.out.println("==== ALL MOVIES ====");
        List<Movie> movies = MovieService.getAllMovies();

        if (movies.isEmpty()) {
            System.out.println("No movies available.");
            System.out.print("\nPress Enter to return to menu...");
            scanner.nextLine();
            return;
        }

        for (Movie movie : movies) {
            System.out.println("\nID: " + movie.getId());
            System.out.println("Title: " + movie.getTitle());
            System.out.println("Duration: " + movie.getDuration());
            System.out.println("Showtime: " + movie.getShowtime());
            System.out.println("Available Seats: " + movie.getAvailableSeats());
            System.out.println("Price: $" + movie.getPrice());
            System.out.println("------------------------");
        }
        
        System.out.print("\nPress Enter to return to menu...");
        scanner.nextLine();
    }

    private static void deleteMovie() {
        System.out.println("\n==== DELETE MOVIE ====");
        
        while (true) {
            System.out.print("\nEnter movie ID to delete (or press Enter to return to menu): ");
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                return; // Return to menu if user presses Enter
            }
            
            Movie movie = MovieService.getMovieById(input);
            if (movie != null) {
                MovieService.deleteMovie(input);
                System.out.println("Movie deleted successfully!");
                return;
            } else {
                System.out.println("Error: Movie with ID " + input + " does not exist!");
            }
        }
    }

    private static void updateMovieSeats() {
        System.out.println("\n==== UPDATE MOVIE SEATS ====");
        
        while (true) {
            System.out.print("\nEnter movie ID to update (or press Enter to return to menu): ");
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                return; // Return to menu if user presses Enter
            }
            
            Movie movie = MovieService.getMovieById(input);
            if (movie != null) {
                System.out.println("Current available seats: " + movie.getAvailableSeats());
                System.out.print("Enter new number of available seats: ");
                try {
                    int newSeats = Integer.parseInt(scanner.nextLine().trim());
                    if (newSeats < 0) {
                        System.out.println("Error: Number of seats cannot be negative!");
                        continue;
                    }
                    MovieService.updateMovieSeats(input, newSeats);
                    System.out.println("Seats updated successfully!");
                    return;
                } catch (NumberFormatException e) {
                    System.out.println("Error: Please enter a valid number!");
                }
            } else {
                System.out.println("Error: Movie with ID " + input + " does not exist!");
            }
        }
    }
}
