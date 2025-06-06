package example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointmentSystem {
    private Map<Professional, Map<LocalDate, boolean[]>> schedules;
    private List<Appointment> appointments;
    private static final int WORKING_HOUR_START = 9;
    private static final int WORKING_HOUR_END = 17;

    public AppointmentSystem() {
        this.schedules = new HashMap<>();
        this.appointments = new ArrayList<>();
    }

    // Шинэ мэргэжилтэн (үйлчилгээ үзүүлэгч) нэмнэ.
    public void registerProfessional(Professional professional) {
        if (professional == null) {
            throw new IllegalArgumentException("Professional cannot be null");
        }
        schedules.put(professional, new HashMap<>());
    }

    // Тухайн мэргэжилтний оруулсан өдрийн бүх цагийг чөлөөтэй гэж үүсгэнэ
    public void initializeDay(Professional professional, LocalDate date) {
        validateProfessional(professional);
        
        boolean[] hours = new boolean[WORKING_HOUR_END - WORKING_HOUR_START + 1];
        schedules.get(professional).put(date, hours);
    }

    private void validateProfessional(Professional professional) {
        if (!schedules.containsKey(professional)) {
            throw new IllegalArgumentException("Professional not registered in the schedule");
        }
    }

    private void validateHour(int hour) {
        if (hour < WORKING_HOUR_START || hour > WORKING_HOUR_END) {
            throw new IllegalArgumentException(
                String.format("Hour must be between %d and %d", WORKING_HOUR_START, WORKING_HOUR_END)
            );
        }
    }

    // Өгсөн мэргэжилтэн, огноо, цагт захиалах боломжтой эсэхийг шалгана
    public boolean isAvailable(Professional professional, LocalDate date, int hour) {
        validateProfessional(professional);
        validateHour(hour);
        
        Map<LocalDate, boolean[]> professionalSchedule = schedules.get(professional);
        if (!professionalSchedule.containsKey(date)) {
            return false;
        }

        return !professionalSchedule.get(date)[hour - WORKING_HOUR_START];
    }

    // Мэргэжилтний тодорхой өдрийн бүх чөлөөт цагийг жагсаалтаар буцаана.
    public List<Integer> getAvailableHours(Professional professional, LocalDate date) {
        validateProfessional(professional);
        
        List<Integer> availableHours = new ArrayList<>();
        Map<LocalDate, boolean[]> professionalSchedule = schedules.get(professional);
        
        if (!professionalSchedule.containsKey(date)) {
            return availableHours;
        }

        boolean[] hours = professionalSchedule.get(date);
        for (int i = 0; i < hours.length; i++) {
            if (!hours[i]) {
                availableHours.add(WORKING_HOUR_START + i);
            }
        }
        return availableHours;
    } 

    // Шинэ захиалга үүсгэнэ
    public Appointment bookAppointment(Client client, Professional professional, 
                                     Service service, LocalDate date, 
                                     int startHour, int durationHours,
                                     boolean isOnline, boolean payByHour, String notes) {
        validateProfessional(professional);
        validateHour(startHour);
        
        if (durationHours < 1) {
            throw new IllegalArgumentException("Duration must be at least 1 hour");
        }

        // Бүх шаардлагатай цагуудын боломжтойг шалгах
        for (int i = 0; i < durationHours; i++) {
            if (!isAvailable(professional, date, startHour + i)) {
                throw new IllegalStateException("Not all hours are available");
            }
        }

        // Цагуудын захиалснаар тэмдэглэх
        for (int i = 0; i < durationHours; i++) {
            schedules.get(professional).get(date)[startHour + i - WORKING_HOUR_START] = true;
        }

        Appointment appointment = new Appointment( appointments.size() + 1, client, professional, service,
            date, startHour, durationHours, isOnline, payByHour, notes
        );

        appointments.add(appointment);
        return appointment;
    }

    // Захиалгыг цуцлана
    public void cancelAppointment(Appointment appointment) {
        Professional professional = appointment.getProfessional();
        validateProfessional(professional);
        
        LocalDate date = appointment.getDate();
        int startHour = appointment.getStartHour();
        int durationHours = appointment.getDurationHours();

        // Mark hours as available
        for (int i = 0; i < durationHours; i++) {
            schedules.get(professional).get(date)[startHour + i - WORKING_HOUR_START] = false;
        }

        appointments.remove(appointment);
    }

    // Тухайн хэрэглэгчийн бүх захиалгыг буцаана
    public List<Appointment> getClientAppointments(Client client) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment appt : appointments) {
            if (appt.getClient().equals(client)) {
                result.add(appt);
            }
        }
        return result;
    }

    // Мэргэжилтний бүх захиалгыг буцаана
    public List<Appointment> getProfessionalAppointments(Professional professional) {
        validateProfessional(professional);
        
        List<Appointment> result = new ArrayList<>();
        for (Appointment appt : appointments) {
            if (appt.getProfessional().equals(professional)) {
                result.add(appt);
            }
        }
        return result;
    }
}
