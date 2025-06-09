package example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Service классын үйл ажиллагааг шалгах тестийн класс
 */
public class ServiceTest {

    private Professional testProfessional1;
    private Professional testProfessional2;
    private Service testService;
    private Company testCompany;

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
}