import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AppointmentSystem implements Serializable {
    private Map<Professional, Map<LocalDate, boolean[]>> prosAvailability;
    private List<Appointment> appointments;
    private static final int WORKING_HOUR_START = 9;
    private static final int WORKING_HOUR_END = 17;

    public AppointmentSystem() {
        this.prosAvailability = new HashMap<>();
        this.appointments = new ArrayList<>();
    }

    /* 
    // Шинэ мэргэжилтэн (үйлчилгээ үзүүлэгч) нэмнэ.
    public void addProfessional(Professional professional) {
        prosAvailability.put(professional, new HashMap<>());
    }

    // Тухайн мэргэжилтний бүх цагийг чөлөөтэй гэж тэмдэглэнэ
    public void initializeDay(Professional professional, LocalDate date) {

    }

    // Өгсөн мэргэжилтэн, огноо, цагт захиалах боломжтой эсэхийг шалгана
    public boolean isAvailable(Professional professional, LocalDate date, int hour) {

    }

    // Мэргэжилтний тодорхой өдрийн бүх чөлөөт цагийг жагсаалтаар буцаана.
    public List<Integer> getAvailableHours(Professional professional, LocalDate date) {

    }

    // Шинэ захиалга үүсгэнэ
    public Appointment bookAppointment(Client client, Professional professional, 
                                     Service service, LocalDate date, 
                                     int startHour, int duration,
                                     boolean isOnline, String location) {

    }

    // Захиалгыг цуцлана
    public void cancelAppointment(Appointment appointment) {

    }

    private void checkHourValid(int hour) {

    }

    // Тухайн хэрэглэгчийн бүх захиалгыг буцаана
    public List<Appointment> getClientAppointments(Client client) {

    }

    // Мэргэжилтний бүх захиалгыг буцаана
    public List<Appointment> getProsAppointments(Professional professional) {

    }

    */
}