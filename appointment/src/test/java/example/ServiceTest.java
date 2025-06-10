package example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.apache.logging.log4j.Logger;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Service классын үйл ажиллагааг шалгах тестийн класс
 */
public class ServiceTest {

    private Professional testProfessional1;
    private Professional testProfessional2;
    private Service testService;
    private Company testCompany;

    private Logger mockLogger;

    /**
     * Тест эхлэхэд бэлдэх үйлдлүүд
     * @throws Exception Алдаа гарвал
     */
    @BeforeEach
    public void setUp() throws Exception {
        testCompany = new Company(1, "Health center", "suhbaatar duureg", "12345678", "info@erm.mn");
        testProfessional1 = new Professional(1, "Dr. A", "99119911", "dr.a@erm.mn", 
                                          "Psychologist", 1, 50000, testCompany);
        testProfessional2 = new Professional(2, "Dr. B", "99229922", "dr.b@erm.mn", 
                                          "Doctor", 2, 45000, testCompany);
        testService = new Service(1, "Therapy", 
                                "Counseling session",
                                new Professional[]{testProfessional1}, 1);
        mockLogger = mock(Logger.class);
        testService.setLogger(mockLogger); 
    }

    /**
     * Үйлчилгээнд мэргэжилтэн нэмэх тест
     * @result Мэргэжилтэн амжилттай нэмэгдэнэ
     */
    @Test
    public void testAddProfessional() {
        String result = testService.addProfessional(testProfessional2);
        assertEquals("Dr. B added professional to service", result);
        assertEquals(2, testService.getProfessionals().length);
    }

    /**
     * Үйлчилгээнээс мэргэжилтэн хасах тест
     * @result Мэргэжилтэн амжилттай хасгагдана
     */
    @Test
    public void testRemoveProfessional() {
        testService.addProfessional(testProfessional2);
        String result = testService.removeProfessional(testProfessional1);
        assertEquals("Dr. A removed professional to service", result);
        assertEquals(1, testService.getProfessionals().length);
    }

    /**
     * Сүүлийн мэргэжилтнийг хасах оролдлого
     * @result Алдаа үүсгэх (үйлчилгээнд дор хаяж 1 мэргэжилтэн байх ёстой)
     */
    @Test
    public void testRemoveLastProfessional() {
        assertThrows(IllegalStateException.class, () -> {
            testService.removeProfessional(testProfessional1);
        });
    }

    /**
     * Мэргэжилтэн үйлчилгээ үзүүлэх эсэхийг шалгах тест
     * @result Мэргэжилтэн үйлчилгээ үзүүлж байгаа эсэхийг буцаана
     */
    @Test
    public void testIsOfferedBy() {
        assertTrue(testService.isOfferedBy(testProfessional1));
        assertFalse(testService.isOfferedBy(testProfessional2));
    }

    /**
     * Үйлчилгээний мэдээллийг хэвлэх тест
     * @result Зөв форматтай мэдээлэл буцаана
     */
    @Test
    public void testToString() {
        String expected = "Service{id=1, name='Therapy', " +
                         "description='Counseling session', " +
                         "professionals=1, defaultDurationHours=1}";
        assertEquals(expected, testService.toString());
    }

    @Test
    public void testAddRemoveNullProfessional() {
        assertThrows(IllegalArgumentException.class, () -> {
            testService.addProfessional(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
        testService.removeProfessional(null);
        });
    }

    @Test
    public void testServiceWithNullNameProfessionals() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Service(
                1,
                null, // null name
                "Description", new Professional[]{testProfessional1}, 1
            );
        });
    
        assertThrows(IllegalArgumentException.class, () -> {
            new Service(
                1,
                "", // empty name
                "Description", new Professional[]{testProfessional1}, 1
            );
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Service(
                1, "Test", "Description",
                new Professional[]{testProfessional1, null}, // null professionals array
                1
            );
        });
    }

    @Test
    public void testInvalidDurationInConstructor() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Service(
                1,
                "Test",
                "Description",
                new Professional[]{testProfessional1},
                0 // wrong defaultDuration
            );
        });
    
        assertThrows(IllegalArgumentException.class, () -> {
            new Service(
                1,
                "Test",
                "Description",
                new Professional[]{testProfessional1},
                13 // wrong defaultDuration > 12
            );
        });
    }

    // Info log, warn test: AddProfessional функц
    @Test
    public void testAddProfessionalLogsInfo_Warn() {
        String result = testService.addProfessional(testProfessional2);

        verify(mockLogger, atLeastOnce()).info(eq("Dr. B added professional to service"));

        String result2 = testService.addProfessional(testProfessional1);

        verify(mockLogger, atLeastOnce()).warn(eq("Dr. A is already associated with this service"));
    }

    // Info log, warn test: emoveProfessional функц
    @Test
    public void testRemoveProfessionalLogsInfo_Warn() {
        testService.addProfessional(testProfessional2);
        String result = testService.removeProfessional(testProfessional2);

        verify(mockLogger, atLeastOnce()).info(eq("Dr. B removed professional to service"));

        String result2 = testService.removeProfessional(testProfessional2);

        verify(mockLogger, atLeastOnce()).warn(eq("Dr. B is not associated with this service"));
    }

}