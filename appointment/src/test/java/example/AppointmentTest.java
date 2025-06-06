package example;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Appointment классын unit test
 */

public class AppointmentTest {

    private Client client;
    private Professional professional;
    private Service service;
    private Company company;
    private LocalDate testDate;

    /**
     * Туршилт ажиллуулахаас өмнөх тохиргоо
     */

    @BeforeEach 
    void setup(){

        company = new Company(1, "Health Center", "UB", "123456", "health@example.com");

        professional = new Professional(1, "Dr. Smith", "987654", "smith@example.com", "Psychologist", 4.8, 50000, company);

        service = new Service(1, "Therapy", "Counseling session", new Professional[]{professional}, 1);

        client = new Client(1, "John Doe", "123456", "john@example.com");

        testDate = LocalDate.now().plusDays(1);
    }

    /**
     * Захиалга үүсгэх тест
     * @result Захиалга үүснэ
     */

    @Test
    public void testCreateAppointment(){
        Appointment appointment = new Appointment(1, client, professional, service, testDate, 
        14, // 2 PM
        1, // 1 hour 
        false, 
        true,
        "First session");

        assertNotNull(appointment);
        assertEquals(client, appointment.getClient());
        assertEquals(professional, appointment.getProfessional());
        assertEquals(14, appointment.getStartHour());
        assertEquals("First session", appointment.getNotes());
    }

    /**
     * Онлайн уулзалтын байршил "Online" гэж буцаагдах эсэхийг шалгана
     * @result online гэсэн байршлыг буцаана
     */

    @Test
    public void testGetLocationOnline(){
        Appointment appointment = new Appointment(2, client, professional, service, testDate,
         15, 1, true, true, "Online session");
        
        assertEquals("Online", appointment.getLocation());
    }

    /**
     * Биечлэн уулзалтын байршил компаний хаягтай ижил эсэхийг шалгана
     * @result компаны байршлыг буцаана
     */
    @Test
    public void testGetLocationInPerson(){
        Appointment appointment = new Appointment(3, client, professional, service, testDate,
        16, 1, false, true, null);

        assertEquals(company.getAddress(), appointment.getLocation());
    }

    /**
     * Хугацаагаар төлбөр тооцоолох зөв ажиллаж байгаа эсэхийг шалгана
     * @result төлбөрийн тооцоог буцаана
     */
    @Test
    public void testCalculateFee(){
        Appointment DurationAppointment = new Appointment(4, client, professional, service, testDate,
        10, 2, false, true, "two hour session");

        Appointment payAppointment = new Appointment(5, client, professional, service, testDate,
        11, 1, true, false, "online session");

        double expectedFee = professional.getPricePerHour() * 2;
        assertEquals(expectedFee, DurationAppointment.calculateFee());
        assertEquals(professional.getPricePerHour(), payAppointment.calculateFee());
    }

    /**
     * testInvalidDuration: Хугацаа 1 цагаас бага үед алдаа өгөх эсэхийг шалгана
     * @result IllegalArgumentException буцаана
     */
    @Test
    public void testInvalidDuration() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Appointment(
                9,
                client,
                professional,
                service,
                testDate,
                11,
                0, // Invalid duration
                true,
                true,
                "Invalid duration test"
            );
        });
    }
}