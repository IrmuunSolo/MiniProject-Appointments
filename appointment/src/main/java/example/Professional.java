package example;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Professional { // үйлчилгээ үзүүлэгч
    private int id;
    private String name;
    private String specialty;
    private double rating;
    private double pricePerHour;

    public Professional(int id, String name, String specialty, double rating, double pricePerHour) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
        this.rating = rating;
        this.pricePerHour = pricePerHour;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public double getRating() {
        return rating;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    @Override
    public String toString() {
        return "Professional{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", specialty='" + specialty + '\'' +
                ", rating=" + rating +
                ", pricePerHour=" + pricePerHour +
                '}';
    }
}