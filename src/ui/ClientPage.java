package ui;

import model.Movie;
import model.User;
import model.Reservation;
import service.MovieService;
import service.ReservationService;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;

public class ClientPage {
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

    public static void showClientPage(User client) {
        while (true) {
            clearScreen();
            System.out.println("==== CLIENT DASHBOARD ====");
            System.out.println("Welcome, " + client.getUsername());
            System.out.println("1. View Available Movies");
            System.out.println("2. Make Reservation");
            System.out.println("3. View My Reservations");
            System.out.println("4. Cancel Reservation");
            System.out.println("5. About Us");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    viewAvailableMovies();
                    break;
                case "2":
                    makeReservation(client);
                    break;
                case "3":
                    viewMyReservations(client);
                    break;
                case "4":
                    cancelReservation(client);
                    break;
                case "5":
                    AboutPage.show();
                    break;
                case "6":
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private static void viewAvailableMovies() {
        clearScreen();
        System.out.println("==== AVAILABLE MOVIES ====");
        List<Movie> movies = MovieService.getAllMovies();

        if (movies.isEmpty()) {
            System.out.println("No movies available.");
            System.out.print("\nPress Enter to return to menu...");
            scanner.nextLine();
            return;
        }

        boolean hasAvailableMovies = false;
        for (Movie movie : movies) {
            if (movie.getAvailableSeats() > 0) {
                hasAvailableMovies = true;
                System.out.println("\nMovie ID: " + movie.getId());
                System.out.println("Title: " + movie.getTitle());
                System.out.println("Duration: " + movie.getDuration());
                System.out.println("Showtime: " + movie.getShowtime());
                System.out.println("Available Seats: " + movie.getAvailableSeats());
                System.out.println("Price per Ticket: $" + movie.getPrice());
                System.out.println("------------------------");
            }
        }
        
        if (!hasAvailableMovies) {
            System.out.println("No movies with available seats found.");
        }
        
        System.out.print("\nPress Enter to return to menu...");
        scanner.nextLine();
    }

    private static void makeReservation(User client) {
        System.out.println("\n==== MAKE RESERVATION ====");
        viewAvailableMovies();

        System.out.print("\nEnter movie ID to book: ");
        String movieId = scanner.nextLine();

        Movie movie = MovieService.getMovieById(movieId);
        if (movie == null) {
            System.out.println("Movie not found!");
            return;
        }

        if (movie.getAvailableSeats() == 0) {
            System.out.println("Sorry, no seats available for this movie!");
            return;
        }

        System.out.println("\nMovie: " + movie.getTitle());
        System.out.println("Available seats: " + movie.getAvailableSeats());
        System.out.println("Price per ticket: $" + movie.getPrice());

        System.out.print("Enter number of seats to book: ");
        int numberOfSeats = Integer.parseInt(scanner.nextLine());

        if (numberOfSeats <= 0) {
            System.out.println("Invalid number of seats!");
            return;
        }

        if (numberOfSeats > movie.getAvailableSeats()) {
            System.out.println("Not enough seats available!");
            return;
        }

        double totalPrice = movie.getPrice() * numberOfSeats;
        System.out.println("\nTotal price: $" + totalPrice);
        System.out.print("Confirm booking? (y/n): ");

        if (scanner.nextLine().equalsIgnoreCase("y")) {
            String reservationId = ReservationService.createReservation(client.getUsername(), movieId, numberOfSeats);
            if (reservationId != null) {
                System.out.println("\nReservation successful!");
                System.out.println("Reservation ID: " + reservationId);
                System.out.println("Please keep this ID for your records.");
            }
        } else {
            System.out.println("Reservation cancelled.");
        }
    }

    private static void viewMyReservations(User client) {
        clearScreen();
        System.out.println("==== MY RESERVATIONS ====");
        List<Reservation> reservations = ReservationService.getUserReservations(client.getUsername());

        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
            System.out.print("\nPress Enter to return to menu...");
            scanner.nextLine();
            return;
        }

        for (Reservation reservation : reservations) {
            Movie movie = MovieService.getMovieById(reservation.getMovieId());
            System.out.println("\nReservation ID: " + reservation.getId());
            System.out.println("Movie: " + (movie != null ? movie.getTitle() : "[Movie Removed]"));
            System.out.println("Number of Seats: " + reservation.getNumberOfSeats());
            System.out.println("Total Price: $" + reservation.getTotalPrice());
            System.out.println("Booking Time: " + reservation.getBookingTime());
            System.out.println("------------------------");
        }
        System.out.print("\nPress Enter to return to menu...");
        scanner.nextLine();
    }

    private static void cancelReservation(User client) {
        System.out.println("\n==== CANCEL RESERVATION ====");
        viewMyReservations(client);

        System.out.print("\nEnter reservation ID to cancel: ");
        String reservationId = scanner.nextLine();

        System.out.print("Are you sure you want to cancel this reservation? (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            ReservationService.cancelReservation(reservationId);
        } else {
            System.out.println("Cancellation aborted.");
        }
    }
}
