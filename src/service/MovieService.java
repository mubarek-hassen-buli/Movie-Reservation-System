package service;

import model.Movie;
import java.io.*;
import java.util.*;

public class MovieService {
    private static final String MOVIES_DIR = "files/movies/";
    private static final String MOVIES_FILE = MOVIES_DIR + "movies.txt";

    static {
        new File(MOVIES_DIR).mkdirs();
    }

    public static void addMovie(Movie movie) {
        try (FileWriter writer = new FileWriter(MOVIES_FILE, true)) {
            String movieData = String.format("%s,%s,%s,%s,%d,%.2f\n",
                movie.getId(),
                movie.getTitle(),
                movie.getDuration(),
                movie.getShowtime(),
                movie.getAvailableSeats(),
                movie.getPrice());
            writer.write(movieData);
            System.out.println("Movie added successfully!");
        } catch (IOException e) {
            System.out.println("Error adding movie: " + e.getMessage());
        }
    }

    public static List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        File file = new File(MOVIES_FILE);

        if (!file.exists()) {
            return movies;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                Movie movie = new Movie(
                    data[0], // id
                    data[1], // title
                    data[2], // duration
                    data[3], // showtime
                    Integer.parseInt(data[4]), // availableSeats
                    Double.parseDouble(data[5]) // price
                );
                movies.add(movie);
            }
        } catch (IOException e) {
            System.out.println("Error reading movies: " + e.getMessage());
        }
        return movies;
    }

    public static Movie getMovieById(String id) {
        List<Movie> movies = getAllMovies();
        return movies.stream()
                    .filter(m -> m.getId().equals(id))
                    .findFirst()
                    .orElse(null);
    }

    public static void updateMovieSeats(String movieId, int newAvailableSeats) {
        List<Movie> movies = getAllMovies();
        List<String> updatedMovies = new ArrayList<>();

        for (Movie movie : movies) {
            if (movie.getId().equals(movieId)) {
                movie.setAvailableSeats(newAvailableSeats);
            }
            updatedMovies.add(String.format("%s,%s,%s,%s,%d,%.2f",
                movie.getId(),
                movie.getTitle(),
                movie.getDuration(),
                movie.getShowtime(),
                movie.getAvailableSeats(),
                movie.getPrice()));
        }

        try (FileWriter writer = new FileWriter(MOVIES_FILE)) {
            for (String movieData : updatedMovies) {
                writer.write(movieData + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error updating movie seats: " + e.getMessage());
        }
    }

    public static void deleteMovie(String movieId) {
        List<Movie> movies = getAllMovies();
        List<String> remainingMovies = new ArrayList<>();

        for (Movie movie : movies) {
            if (!movie.getId().equals(movieId)) {
                remainingMovies.add(String.format("%s,%s,%s,%s,%d,%.2f",
                    movie.getId(),
                    movie.getTitle(),
                    movie.getDuration(),
                    movie.getShowtime(),
                    movie.getAvailableSeats(),
                    movie.getPrice()));
            }
        }

        try (FileWriter writer = new FileWriter(MOVIES_FILE)) {
            for (String movieData : remainingMovies) {
                writer.write(movieData + "\n");
            }
            System.out.println("Movie deleted successfully!");
        } catch (IOException e) {
            System.out.println("Error deleting movie: " + e.getMessage());
        }
    }
}