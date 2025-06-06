package service;

import model.Reservation;
import model.Movie;
import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReservationService {
    private static final String RESERVATIONS_DIR = "files/reservations/";
    private static final String RESERVATIONS_FILE = RESERVATIONS_DIR + "reservations.txt";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    static {
        new File(RESERVATIONS_DIR).mkdirs();
    }

    public static String createReservation(String userId, String movieId, int numberOfSeats) {
        Movie movie = MovieService.getMovieById(movieId);
        if (movie == null) {
            System.out.println("Movie not found!");
            return null;
        }

        if (movie.getAvailableSeats() < numberOfSeats) {
            System.out.println("Not enough seats available!");
            return null;
        }

        // Get all reservations to determine next ID
        List<Reservation> existingReservations = getAllReservations();
        int nextNumber = existingReservations.size() + 1;
        String reservationId = String.format("E%d", nextNumber);
        double totalPrice = movie.getPrice() * numberOfSeats;
        String bookingTime = LocalDateTime.now().format(formatter);

        Reservation reservation = new Reservation(
            reservationId,
            userId,
            movieId,
            numberOfSeats,
            totalPrice,
            bookingTime
        );

        try (FileWriter writer = new FileWriter(RESERVATIONS_FILE, true)) {
            String reservationData = String.format("%s,%s,%s,%d,%.2f,%s\n",
                reservation.getId(),
                reservation.getUserId(),
                reservation.getMovieId(),
                reservation.getNumberOfSeats(),
                reservation.getTotalPrice(),
                reservation.getBookingTime());
            writer.write(reservationData);

            // Update available seats
            MovieService.updateMovieSeats(movieId, movie.getAvailableSeats() - numberOfSeats);
            System.out.println("Reservation created successfully!");
            return reservationId;
        } catch (IOException e) {
            System.out.println("Error creating reservation: " + e.getMessage());
            return null;
        }
    }

    public static List<Reservation> getUserReservations(String userId) {
        List<Reservation> reservations = new ArrayList<>();
        File file = new File(RESERVATIONS_FILE);

        if (!file.exists()) {
            return reservations;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[1].equals(userId)) {
                    Reservation reservation = new Reservation(
                        data[0], // id
                        data[1], // userId
                        data[2], // movieId
                        Integer.parseInt(data[3]), // numberOfSeats
                        Double.parseDouble(data[4]), // totalPrice
                        data[5] // bookingTime
                    );
                    reservations.add(reservation);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading reservations: " + e.getMessage());
        }
        return reservations;
    }

    public static void cancelReservation(String reservationId) {
        List<Reservation> allReservations = getAllReservations();
        List<String> remainingReservations = new ArrayList<>();
        Reservation canceledReservation = null;

        for (Reservation reservation : allReservations) {
            if (reservation.getId().equals(reservationId)) {
                canceledReservation = reservation;
            } else {
                remainingReservations.add(String.format("%s,%s,%s,%d,%.2f,%s",
                    reservation.getId(),
                    reservation.getUserId(),
                    reservation.getMovieId(),
                    reservation.getNumberOfSeats(),
                    reservation.getTotalPrice(),
                    reservation.getBookingTime()));
            }
        }

        if (canceledReservation != null) {
            try (FileWriter writer = new FileWriter(RESERVATIONS_FILE)) {
                for (String reservationData : remainingReservations) {
                    writer.write(reservationData + "\n");
                }

                // Return seats to movie's available seats
                Movie movie = MovieService.getMovieById(canceledReservation.getMovieId());
                if (movie != null) {
                    MovieService.updateMovieSeats(
                        movie.getId(),
                        movie.getAvailableSeats() + canceledReservation.getNumberOfSeats()
                    );
                }
                System.out.println("Reservation canceled successfully!");
            } catch (IOException e) {
                System.out.println("Error canceling reservation: " + e.getMessage());
            }
        } else {
            System.out.println("Reservation not found!");
        }
    }

    private static List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        File file = new File(RESERVATIONS_FILE);

        if (!file.exists()) {
            return reservations;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                Reservation reservation = new Reservation(
                    data[0], // id
                    data[1], // userId
                    data[2], // movieId
                    Integer.parseInt(data[3]), // numberOfSeats
                    Double.parseDouble(data[4]), // totalPrice
                    data[5] // bookingTime
                );
                reservations.add(reservation);
            }
        } catch (IOException e) {
            System.out.println("Error reading reservations: " + e.getMessage());
        }
        return reservations;
    }
}