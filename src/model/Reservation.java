package model;

public class Reservation {
    private String id;
    private String userId;
    private String movieId;
    private int numberOfSeats;
    private double totalPrice;
    private String bookingTime;

    public Reservation(String id, String userId, String movieId, int numberOfSeats, double totalPrice, String bookingTime) {
        this.id = id;
        this.userId = userId;
        this.movieId = movieId;
        this.numberOfSeats = numberOfSeats;
        this.totalPrice = totalPrice;
        this.bookingTime = bookingTime;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getMovieId() {
        return movieId;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getBookingTime() {
        return bookingTime;
    }
}