package example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * AppointmentSystem классын unit test
 */

public class AppointmentSystemTest {

    private AppointmentSystem system;
    private Professional testProfessional;
    private Client testClient;
    private Service testService;
    private LocalDate testDate;

    /**
     * Туршилт ажиллуулахаас өмнөх тохиргоо
     */

    @BeforeEach
    public void setUp(){

        system = new AppointmentSystem();

        Company testCompany = new Company(1, "Health Center", "UB", "99998888", 
        "health@gmail.com");

        testProfessional = new Professional(1, "DR. Smith", "98986545", 
        "smith@gmail.com", "Psychologist", 4.8, 50000, testCompany);

        testService = new Service(1, "Therapy", "Counseling session", 
        new Professional[]{testProfessional}, 1);

        testClient = new Client(1, "John Doe", "99222222", "john7@gmail.com");

        testDate = LocalDate.now().plusDays(1);

        // Системд мэргэжилтэн бүртгэх
        system.registerProfessional(testProfessional);
        system.initializeDay(testProfessional, testDate);
    }

    /**
     * Мэргэжилтэн бүртгэх тест
     * @result Мэргэжилтэн амжилттай бүртгэгдэнэ
     */

    @Test
    public void testRegisterProfessional() {
        Professional newProfessional = new Professional(
            2, "Dr. Jones", "112233", "jones@example.com",
            "Therapist", 4.5, 45000, testProfessional.getCompany()
        );
        
        system.registerProfessional(newProfessional);
        
        // Шинэ мэргэжилтэн бүртгэгдсэн эсэхийг шалгах
        assertDoesNotThrow(() -> system.initializeDay(newProfessional, testDate));
    }

    /**
     * Өдрийн цагийг эхлүүлэх тест
     * @result Өдрийн бүх цаг чөлөөтэй болно
     */

    @Test
    public void testInitializeDay() {
        LocalDate Date = testDate.plusDays(1);
        system.initializeDay(testProfessional, Date);
        
        // Бүх цаг чөлөөтэй эсэхийг шалгах
        for (int hour = 9; hour <= 17; hour++) {
            assertTrue(system.isAvailable(testProfessional, Date, hour));
        }
    }

    /**
     * Захиалга үүсгэх тест
     * @result Захиалга амжилттай үүсч, цаг завгүй болно
     */

    @Test
    public void testBookAppointment() {
        Appointment appointment = system.bookAppointment(
            testClient,
            testProfessional,
            testService,
            testDate,
            14, // 2 PM
            1,
            false,
            true,
            "First session"
        );
        
        assertNotNull(appointment);
        assertEquals(1, system.getClientAppointments(testClient).size());
        assertFalse(system.isAvailable(testProfessional, testDate, 14));
    }
 
    /**
     * Захиалга амжилттай цуцлагдаж байгааг шалгах тест
     * @result Захиалсан цаг цуцлагдаж, бүртгэлээс хасна
     */
    @Test
    public void testCancelAppointment() {
        Appointment appointment = system.bookAppointment(
            testClient,
            testProfessional,
            testService,
            testDate,
            15,
            1,
            true,
            false,
            "Online session"
        );
        
        system.cancelAppointment(appointment);
        
        assertEquals(0, system.getClientAppointments(testClient).size());
        assertTrue(system.isAvailable(testProfessional, testDate, 15));
    }

    // 
    /**
     * Чөлөөт цагуудыг зөв буцааж байгаа эсэхийг шалгана
     * @result мэргэжилтний боломжтой цагыг буцаана
     */
    @Test
    public void testGetAvailableHours() {
        // Захиалга үүсгэх
        system.bookAppointment(testClient, testProfessional, testService, testDate,
            10, 2, false, true, "Morning session");
        
        List<Integer> availableHours = system.getAvailableHours(testProfessional, testDate);
        
        // 10-11 цаг завгүй тул жагсаалтад байхгүй эсэхийг шалгах
        assertFalse(availableHours.contains(10));
        assertFalse(availableHours.contains(11));

        // Бусад цаг чөлөөтэй эсэхийг шалгах
        assertTrue(availableHours.contains(9));
        assertTrue(availableHours.contains(12));
    }

    // 
    /**
     * Нэг цагт хоёр захиалга хийх боломжгүй эсэхийг шалгана
     * @result алдаа буцаана
     */
    
    @Test
    public void testDoubleBooking() {
        system.bookAppointment( testClient, testProfessional, testService,
            testDate,16, 1, false, true, "First booking");

        Client anotherClient = new Client(2, "Jane Doe", "654321", "jane@example.com");
        
        assertThrows(IllegalStateException.class, () -> {
            system.bookAppointment(anotherClient, testProfessional, testService, testDate,
                16, 1, true, true, "Second booking");
        });
    }

    /**
     * Ажиллах цагийн бус захиалга хийх боломжгүй эсэхийг шалгана
     * @result алдаа буцаана
     */
    @Test
    public void testInvalidHourBooking() {
        assertThrows(IllegalArgumentException.class, () -> {
            system.bookAppointment(testClient, testProfessional, testService, testDate, 8, // Invalid hour
                1, false, true, "Invalid hour test"
            );
        });
    }

    // 
    /**
     * Хэрэглэгчийн захиалгуудыг зөв буцааж байгаа эсэхийг шалгана
     * @result хэрэглэгч 2 өөр цаг авна
     */
    @Test
    public void testGetClientAppointments() {

         system.initializeDay(testProfessional, testDate.plusDays(1)); // Шинэ өдрийг эхлүүлэх

        system.bookAppointment(
            testClient,
            testProfessional,
            testService,
            testDate,
            9,
            1,
            false,
            false,
            "First"
        );
        
        system.bookAppointment(
            testClient,
            testProfessional,
            testService,
            testDate.plusDays(1),
            10,
            1,
            true,
            true,
            "Second"
        );
        
        List<Appointment> clientAppointments = system.getClientAppointments(testClient);
        assertEquals(2, clientAppointments.size());
    }

    /**
     * Мэргэжилтний захиалгуудыг зөв буцааж байгаа эсэхийг шалгана
     * @result хэрэглэгч 2 өөр цаг авна
     */
    @Test
    public void testGetProfessionalAppointments() {
        Client client2 = new Client(2, "Alice", "111222", "alice@example.com");
        
        system.bookAppointment(testClient, testProfessional, testService, testDate,
            11, 1, false, true, "From client1"
        );
        
        system.bookAppointment(client2, testProfessional, testService, testDate,
            13, 1, true, true, "From client2"
        );
        
        List<Appointment> professionalAppointments = system.getProfessionalAppointments(testProfessional);
        assertEquals(2, professionalAppointments.size());
    }

    
}
