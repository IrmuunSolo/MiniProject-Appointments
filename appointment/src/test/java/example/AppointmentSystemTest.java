package example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.apache.logging.log4j.Logger;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * AppointmentSystem классын unit test
 */

public class AppointmentSystemTest {

    private AppointmentSystem system;
    private Professional testProfessional;
    private Client testClient;
    private Service testService;
    private LocalDate testDate;

    private Logger mockLogger;

    /**
     * Туршилт ажиллуулахаас өмнөх тохиргоо
     */

    @BeforeEach
    public void setUp(){

        system = new AppointmentSystem();

        Company testCompany = new Company(1, "Health Center", "UB", "99998888", 
        "health@gmail.com");

        testProfessional = new Professional(1, "DR. Smith", "98986545", 
        "smith@gmail.com", "Psychologist", 4, 50000, testCompany);

        testService = new Service(1, "Therapy", "Counseling session", 
        new Professional[]{testProfessional}, 1);

        testClient = new Client(1, "John Doe", "99222222", "john7@gmail.com");

        testDate = LocalDate.now().plusDays(1);

        // Системд мэргэжилтэн бүртгэх
        system.registerProfessional(testProfessional);
        system.initializeDay(testProfessional, testDate);

        // log4j logger-г mock хийж оноох
        mockLogger = mock(Logger.class);
        system.setLogger(mockLogger); 
    }


    /**
     * Бүртгэлгүй мэргэжилтийн шалгах тест
     * @throws IllegalArgumentException Professional 
     */
    @Test
    public void testBookAppointmentWithUnregisteredProfessional() {
        Professional unregistered = new Professional(
            3, "Unregistered", "99880000", "un@test.com",
            "Test", 1, 10000, testProfessional.getCompany()
        );

        testService.addProfessional(unregistered);
    
        assertThrows(IllegalArgumentException.class, () -> {
            system.bookAppointment(
                testClient,
                unregistered,
                testService,
                testDate,
                14,
                1,
                false,
                true,
                "Test"
            );
        });
    }

    /**
     * мэргэжилтний хуваарь тавиагүй өдөрт цаг авах үед алдаа шидэх
     * @throws IllegalArgumentException "unavailable hours or uninitialized day"
     */
    @Test
    public void testBookAppointmentWithUninitializedDate() {
        LocalDate uninitializedDate = testDate.plusDays(2);
    
        assertThrows(IllegalStateException.class, () -> {
            system.bookAppointment(
                testClient,
                testProfessional,
                testService,
                uninitializedDate,
                14,
                1,
                false,
                true,
                "Test"
            );
        });
    }

        /**
     * огт байхгүй цагийг захиалгын цагуудаас хасах үед алдаа шидэх
     * @throws IllegalArgumentException "already there is not this appointment"
     */
    @Test
    public void testCancelNonExistentAppointment() {
        Appointment fakeAppointment = new Appointment(
            999,
            testClient,
            testProfessional,
            testService,
            testDate,
            14,
            1,
            false,
            true,
            "Fake"
        );
    
        assertThrows(IllegalArgumentException.class, () -> {
            system.cancelAppointment(fakeAppointment);
        });
    }

    /**
     * null хэрэглэгч өгөхөд захиалсан цагыг буцаалгүй, алдаа шидэх
     * @throws IllegalArgumentException "already there is not this appointment"
     */
    @Test
    public void testGetAppointmentsForNullClient() {
        assertThrows(IllegalArgumentException.class, () -> {
            system.getClientAppointments(null);
        });
    }

    /**
     * Мэргэжилтэн бүртгэх тест
     * @result Мэргэжилтэн амжилттай бүртгэгдэнэ
     */

    @Test
    public void testRegisterProfessional() {
        Professional newProfessional = new Professional(
            2, "Dr. Jones", "11112233", "jones@example.com",
            "Therapist", 4, 45000, testProfessional.getCompany()
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

        Client anotherClient = new Client(2, "Jane Doe", "65004321", "jane@example.com");
        
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
        Client client2 = new Client(2, "Alice", "11124422", "alice@example.com");
        
        system.bookAppointment(testClient, testProfessional, testService, testDate,
            11, 1, false, true, "From client1"
        );
        
        system.bookAppointment(client2, testProfessional, testService, testDate,
            13, 1, true, true, "From client2"
        );
        
        List<Appointment> professionalAppointments = system.getProfessionalAppointments(testProfessional);
        assertEquals(2, professionalAppointments.size());
    }

    // Info Log test: BookAppointment функц
    @Test
    public void testBookAppointmentLogsInfo() {
        Appointment appointment = system.bookAppointment(testClient, testProfessional, testService, testDate, 10, 1, true, false, "Test");

        verify(mockLogger, atLeastOnce()).info(anyString(), eq(appointment));
    }  
    
    // Info Log test: BookAppointment функц
    @Test
    public void testBookAppointmentLogsError() {
        IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class, () -> {
            system.bookAppointment(testClient, testProfessional, testService, testDate, 7, 1, true, false, "Test");
        });
        verify(mockLogger, atLeastOnce()).error(anyString(), eq(thrownException.getMessage()));
    }   

    // Info, error log test: RegisterProfessional функц
    @Test
    public void testRegisterProfessionalLogsInfoAndError() {
        // Амжилттай бүртгэхэд info лог
        system.registerProfessional(testProfessional);
        verify(mockLogger).info(eq("Registered professional: {}"), eq(testProfessional.getName()));

        // Алдаатай бүртгэхэд error лог
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            system.registerProfessional(null);
        });
        verify(mockLogger).error(eq("Failed to register professional: {}"), eq(ex.getMessage()));
    }

    // Info, error log test: InitializeDay функц
    @Test
    public void testInitializeDayLogsInfoAndError() {
        // Өгөгдсөн мэргэжилтэн, өдрөөр амжилттай ажиллахад info лог
        system.registerProfessional(testProfessional); // заавал бүртгэх шаардлагатай
        system.initializeDay(testProfessional, testDate);
        verify(mockLogger).info(eq("Initialized day {} for professional {}"), eq(testDate), eq(testProfessional.getName()));

        // Алдаатай initialize хийхэд error лог
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            system.initializeDay(null, testDate);
        });
        verify(mockLogger).error(eq("Failed to initialize day: {}"), eq(ex.getMessage()));
    }

    // Info, error log test: InitializeDay функц
    @Test
    public void testCancelAppointmentLogsInfoAndError() {
        // Буруу оролт (null) дээр error лог
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> {
            system.cancelAppointment(null);
        });
        verify(mockLogger).error(eq("Failed to cancel appointment: {}"), eq(ex1.getMessage()));

        // Амжилттай цуцлахад info лог бичигдэнэ
        Appointment appointment = system.bookAppointment(testClient, testProfessional, testService, testDate, 15, 1,
            true, false, "Online session" );

        system.cancelAppointment(appointment);
        verify(mockLogger).info(eq("Cancelled appointment: {}"), eq(appointment));
    }

}
