package example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Цаг захиалгын системийг удирдах үндсэн класс
 */

public class AppointmentSystem {

    private Logger logger = LogManager.getLogger(AppointmentSystem.class);

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    private Map<Professional, Map<LocalDate, boolean[]>> schedules;
    private List<Appointment> appointments;                         // Бүх авсан цагуудыг хадгалах
    private static final int WORKING_HOUR_START = 9;
    private static final int WORKING_HOUR_END = 17;

    /**
     * Байгуулагч функц
     */
    public AppointmentSystem() {
        try{
            this.schedules = new HashMap<>();
            this.appointments = new ArrayList<>();
            logger.info("Created new appointment Sytem: {}", this);
        }catch (IllegalArgumentException e) {
            logger.error("Failed to create appointment system: {}", e.getMessage());
            throw e;
        } 
    }

    /**
     * Шинэ мэргэжилтэн бүртгэх
     * @param professional Бүртгэх мэргэжилтэн
     * @throws IllegalArgumentException professional null байвал
     */
    public void registerProfessional(Professional professional) {
        try {
            if (professional == null) {
                throw new IllegalArgumentException("Professional cannot be null");
            }
            schedules.put(professional, new HashMap<>());
            logger.info("Registered professional: {}", professional.getName());
        } catch (IllegalArgumentException e) {
            logger.error("Failed to register professional: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * мэргэжилтэн ажиллах өдрөө хуваарьт нэмэх
     * @param professional Мэргэжилтэн
     * @param date Өдөр
     * @throws IllegalArgumentException professional бүртгэлгүй байвал
     */
    public void initializeDay(Professional professional, LocalDate date) {
        try {
            validateProfessional(professional);

            boolean[] hours = new boolean[WORKING_HOUR_END - WORKING_HOUR_START + 1];
            schedules.get(professional).put(date, hours);
            logger.info("Initialized day {} for professional {}", date, professional.getName());
        } catch (IllegalArgumentException e) {
            logger.error("Failed to initialize day: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Мэргэжилтнийг баталгаажуулна
     * @param professional Мэргэжилтэн
     * @throws IllegalArgumentException professional бүртгэлгүй байвал
     */
    private void validateProfessional(Professional professional) {
        if (!schedules.containsKey(professional)) {
            throw new IllegalArgumentException("Professional not registered in the schedule");
        }
    }

    /**
     * Цагийг баталгаажуулна
     * @param hour цаг
     * @throws IllegalArgumentException String цаг 9-17 хооронд биш байвал
     */
    private void validateHour(int hour) {
        if (hour < WORKING_HOUR_START || hour > WORKING_HOUR_END) {
            throw new IllegalArgumentException(
                String.format("Hour must be between %d and %d", WORKING_HOUR_START, WORKING_HOUR_END)
            );
        }
    }

    /**
     *  Мэргэжилтэн тухайн өдөр-цагт ажиллах хуваарь байгаа үгүйг шалгана
     * @param professional Мэргэжилтэн 
     * @param date Өдөр
     * @param hour цаг
     * @throws IllegalArgumentException professional бүртгэлгүй байвал
     * @return боломжтой бол true, үгүй бол false
     */
    public boolean isAvailable(Professional professional, LocalDate date, int hour) {
        validateProfessional(professional);
        validateHour(hour);
        
        Map<LocalDate, boolean[]> professionalSchedule = schedules.get(professional);
        if (!professionalSchedule.containsKey(date)) {
            return false;
        }

        return !professionalSchedule.get(date)[hour - WORKING_HOUR_START];
    }

    /**
     * Мэргэжилтний тодорхой өдрийн бүх чөлөөт цагийг жагсаалтаар буцаана.
     * @param professional Мэргэжилтэн 
     * @param date Өдөр
     * @throws IllegalArgumentException professional бүртгэлгүй байвал
     * @return боломжтой цагийг жагсаалт List
     */
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

    /**
     * Шинэ захиалга үүсгэнэ     
     * @param Client
     * @param Professional ...
     * @throws IllegalArgumentException professional бүртгэлгүй байвал
     * @return авсан захиалга
     */
    public Appointment bookAppointment(Client client, Professional professional, 
                                     Service service, LocalDate date, 
                                     int startHour, int durationHours,
                                     boolean isOnline, boolean payByHour, String notes) {
        try {
            validateProfessional(professional);
            validateHour(startHour);
            
            if (durationHours < 1) {
                throw new IllegalArgumentException("Duration must be at least 1 hour");
            }

            for (int i = 0; i < durationHours; i++) {
                if (!isAvailable(professional, date, startHour + i)) {
                    throw new IllegalStateException("unavailable hours or uninitialized day");
                }
            }

            for (int i = 0; i < durationHours; i++) {
                schedules.get(professional).get(date)[startHour + i - WORKING_HOUR_START] = true;
            }

            Appointment appointment = new Appointment(
                appointments.size() + 1, client, professional, service,
                date, startHour, durationHours, isOnline, payByHour, notes
            );

            appointments.add(appointment);
            logger.info("Booked appointment: {}", appointment);
            return appointment;
        } catch (IllegalArgumentException | IllegalStateException e) {
            logger.error("Failed to book appointment: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Захиалсан уулзалтыг цуцлана    
     * @param Appointment
     * @throws IllegalArgumentException professional бүртгэлгүй байвал
     */
    public void cancelAppointment(Appointment appointment) {

        try{
            if (appointment == null) {
            throw new IllegalArgumentException("Appointment cannot be null");
            }

            Professional professional = appointment.getProfessional();
            validateProfessional(professional);
        
            LocalDate date = appointment.getDate();
            int startHour = appointment.getStartHour();
            int durationHours = appointment.getDurationHours();

            // Mark hours as available
            for (int i = 0; i < durationHours; i++) {
                schedules.get(professional).get(date)[startHour + i - WORKING_HOUR_START] = false;
            }

            if(!appointments.contains(appointment)){
                throw new IllegalArgumentException("Already there is not this appointment");
            }

            appointments.remove(appointment);
            logger.info("Cancelled appointment: {}", appointment);
        } catch (IllegalArgumentException | IllegalStateException e) {
            logger.error("Failed to cancel appointment: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Тухайн хэрэглэгчийн бүх захиалгыг буцаана     
     * @param Client
     * @return хэрэглэгчийн хийсэн захиалгын жагсаалт
     */
    public List<Appointment> getClientAppointments(Client client) {
        if (client == null) {
        throw new IllegalArgumentException("Client cannot be null");
        }

        List<Appointment> result = new ArrayList<>();
        for (Appointment appt : appointments) {
            if (appt.getClient().equals(client)) {
                result.add(appt);
            }
        }
        return result;
    }
 
    /**
     * Мэргэжилтний бүх захиалгыг буцаана   
     * @param Professional
     * @throws IllegalArgumentException professional бүртгэлгүй байвал
     * @return мэргэжилтний авсан захиалгын жагсаалт
     */
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
