package example;

/**
 * Person эцэг класс. Person үндсэн мэдээллийг хадгална.
 */

public class Person {
    protected int id;
    protected String name;
    protected String phone;
    protected String email;

    public Person(int id, String name, String phone, String email) {
        // эерэг ID
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be a positive number");
        }

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("Name cannot exceed 100 characters");
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
        this.phone = phone;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail(){
        return email;
    }
}

