package model;

public class Movie {
    private String id;
    private String title;
    private String duration;
    private String showtime;
    private int availableSeats;
    private double price;

    public Movie(String id, String title, String duration, String showtime, int availableSeats, double price) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.showtime = showtime;
        this.availableSeats = availableSeats;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDuration() {
        return duration;
    }

    public String getShowtime() {
        return showtime;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public double getPrice() {
        return price;
    }
}