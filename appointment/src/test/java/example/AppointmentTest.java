package example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import org.apache.logging.log4j.Logger;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.*;

/**
 * Appointment классын unit test
 */

public class AppointmentTest {

    private Client client;
    private Professional professional;
    private Service service;
    private Company company;
    private LocalDate testDate;

    private Logger mockLogger;

    /**
     * Туршилт ажиллуулахаас өмнөх тохиргоо
     */

    @BeforeEach 
    void setup(){

        company = new Company(1, "Health Center", "UB", "12340056", "health@example.com");

        professional = new Professional(1, "Dr. Smith", "98700654", "smith@example.com", 
                    "Psychologist", 3, 50000, company);

        service = new Service(1, "Therapy", "Counseling session", new Professional[]{professional}, 1);

        client = new Client(1, "John Doe", "12003456", "john@example.com");

        testDate = LocalDate.now().plusDays(1);

        // log4j logger-г mock хийж оноох
        mockLogger = mock(Logger.class);
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

    @Test
    public void testNullClientThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Appointment(
                1,
                null, // null client
                professional,
                service,
                testDate,
                14,
                1,
                false,
                true,
                "Test"
            );
        });
    }

    @Test
    public void testNullProfessionalThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Appointment(
                1,
                client,
                null, // null professional
                service,
                testDate,
                14,
                1,
                false,
                true,
                "Test"
            );
        });
    }

    @Test
    public void testNullServiceThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Appointment(
                1,
                client,
                professional,
                null, // null service
                testDate,
                14,
                1,
                false,
                true,
                "Test"
            );
        });
    }

    @Test
    public void testPastDateThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Appointment(
                1,
                client,
                professional,
                service,
                LocalDate.now().minusDays(1), // past date
                14,
                1,
                false,
                true,
                "Test"
            );
        });
    }

    @Test
    public void testInvalidStartHourThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Appointment(
                1,
                client,
                professional,
                service,
                testDate,
                8, // invalid hour (before 9)
                1,
                false,
                true,
                "Test"
            );
        });
    
        assertThrows(IllegalArgumentException.class, () -> {
            new Appointment(
                1,
                client,
                professional,
                service,
                testDate,
                18, // invalid hour (after 17)
                1,
                false,
                true,
                "Test"
            );
        });
    }

    @Test
    public void testProfessionalNotOfferingService() {
        Professional otherProfessional = new Professional(
            2, "Dr. Jones", "11223300", "jones@example.com",
            "Therapist", 4, 45000, company
        );
    
        assertThrows(IllegalArgumentException.class, () -> {
            new Appointment(
                1,
                client,
                otherProfessional, // doesn't offer this service
                service,
                testDate,
                14,
                1,
                false,
                true,
                "Test"
            );
        });
    }

    // Info log test: Байгуулагч функц
    @Test
    public void testConstructorLogsInfo() {
        Appointment appointment = new Appointment(1, client, professional, service, testDate, 
            10, 1, true, false, "Note", mockLogger);

        // log бичигдсэн эсэх шалгах
        verify(mockLogger, atLeastOnce()).info(startsWith("Created new appointment:"), eq(appointment));
    }

    // Info error test: Байгуулагч функц
    @Test
    public void testConstructorLogsError() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            new Appointment(1, null, professional, service, testDate, 
                10, 1, true, false, "Note", mockLogger);
        });

        verify(mockLogger, atLeastOnce()).error(anyString(), eq(thrown.getMessage()));
    }

}