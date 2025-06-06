package example;

;
public class Professional extends Person { // үйлчилгээ үзүүлэгч
    private String specialty;
    private double rating;
    private double pricePerHour;
    private Company company;

    public Professional(int id, String name, String phone, String email,  String specialty, double rating, double price, Company company) {
        super(id, name, phone, email);
        this.specialty = specialty;
        this.rating = rating;
        this.pricePerHour = pricePerHour;
        this.company = company;
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
    public Company getCompany() { 
        return company; 
    }

    @Override
    public String toString() {
        return "Professional{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", specialty='" + specialty + '\'' +
                ", rating=" + rating +
                ", price=" + pricePerHour +
                '}';
    }

}