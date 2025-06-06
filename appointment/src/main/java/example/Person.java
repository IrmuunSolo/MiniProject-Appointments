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

