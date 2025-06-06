package example;

import java.time.LocalDate;

// run command: java -cp target/appointment-1.0-SNAPSHOT.jar Main

public class Main {
    public static void main(String[] args) {

    
        // Create a Professional
    Professional doctor = new Professional(1, "Dr. Smith", "111", "smith@example.com", 
                                      "doctor", 4.8, 50000, new Company( 12, "company", "vashington", "1984698198", "company@inc.com"));
        
        Client client = new Client(
            101, 
            "John Doe", 
            "123-456-7890", 
            "john.doe@example.com"
        );
        
        // Create a Service
        Service therapy = new Service(
            1, 
            "Therapy Session", 
            "Psychological counseling session", 
            new Professional[]{doctor}, 
            1
        );
        
        // Create an Appointment
        Appointment appointment = new Appointment(
            1001,
            client,
            doctor,
            therapy,
            LocalDate.of(2023, 12, 15),
            14, // 2:00 PM
            1, // 1 hour duration
            false, // in-person
            "Initial consultation"
        );
        
        // Print all objects
        System.out.println("=== Professional Details ===");
        System.out.println(doctor);
        System.out.println("\n=== Client Details ===");
        System.out.println(client);
        System.out.println("\n=== Service Details ===");
        printServiceDetails(therapy);
        System.out.println("\n=== Appointment Details ===");
        System.out.println(appointment);
    }
    
    private static void printServiceDetails(Service service) {
        System.out.println("Service ID: " + service.getId());
        System.out.println("Name: " + service.getName());
        System.out.println("Description: " + service.getDescription());
        System.out.println("Default Duration: " + service.getDefaultDurationHours() + " hours");
        System.out.println("Available Professionals:");
        
        for (Professional pro : service.getProfessionals()) {
            System.out.println("  - " + pro.getName() + " (" + pro.getSpecialty() + ")");
        }
    }
}
