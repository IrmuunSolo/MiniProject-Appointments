package example;

public class Client extends Person{

    public Client(int id, String name, String phone, String email) {
        super(id, name, phone, email);
    }

    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}