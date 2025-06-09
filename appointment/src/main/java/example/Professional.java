package example;

/**
 * Үйлчилгээ үзүүлэгч мэргэжилтний класс
 */

;
public class Professional extends Person { // үйлчилгээ үзүүлэгч
    private String specialty;
    private int totalRating;
    private int totalRater;
    private double pricePerHour;
    private Company company;

    public Professional(int id, String name, String phone, String email, String specialty, int rating, double pricePerHour, Company company) {
        super(id, name, phone, email);
    
        // email '@' тэмдэгт агуулж байгааг шалгах
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email must contain '@' symbol");
        }
    
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
    
        // утасны дугаар 8 оронтой байна
        if (phone == null || phone.trim().isEmpty() || phone.length() != 8) {
            throw new IllegalArgumentException("Phone cannot be null or empty and must be 8 digits");
        }
    
        if (specialty == null || specialty.trim().isEmpty()) {
            throw new IllegalArgumentException("Specialty cannot be null or empty");
        }
    
        // rating 0-5 хооронд байх
        if (rating < 0 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 0 and 5");
        }
    
        // эерэг утга шалгах
        if (pricePerHour < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
    
        this.specialty = specialty;
        this.giveRating(rating);
        this.pricePerHour = pricePerHour;
        this.company = company;
    }

    public String getSpecialty() {
        return specialty;
    }
    public double getRating() {
        if (totalRater == 0) {
            return 0.0;  
        }
        return (double) totalRating/totalRater;
    }
    public double getPricePerHour() {
        return pricePerHour;
    }
    public Company getCompany() { 
        return company; 
    }

    public void giveRating(int rating){
        totalRating += rating;
        totalRater++;
    }

    @Override
    public String toString() {
        return "Professional{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", specialty='" + specialty + '\'' +
                ", rating=" + getRating() +
                ", price=" + pricePerHour +
                '}';
    }

}