package example;

public class Company {
    private int id;
    private String name;
    private String address;
    private String phone;
    private String email;

    public Company(int id, String name, String address, String phone, String email) {
        // эерэг ID
        if (id <= 0) {
            throw new IllegalArgumentException("Company ID must be a positive integer");
        }

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Company name cannot be null or empty");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("Company name cannot exceed 100 characters");
        }

        // address 200 илүүгүй үсэгтэй
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be null or empty");
        }
        if (address.length() > 200) {
            throw new IllegalArgumentException("Address cannot exceed 200 characters");
        }

        // утасны дугаар 8 оронтой байна
        if (phone == null || phone.trim().isEmpty() || phone.length() != 8) {
            throw new IllegalArgumentException("Phone cannot be null or empty and must be 8 digits");
        }

        // email '@' тэмдэгт агуулж байгааг шалгах
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email must contain '@' symbol and not null");
        }
        if (email.length() > 255) {
            throw new IllegalArgumentException("Email cannot exceed 255 characters");
        }

        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}