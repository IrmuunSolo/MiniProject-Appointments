package example;

import java.time.LocalDate;

/**
 * Классын тодорхойлолт энд бичигдэнэ.
 * 
 * @author Л.Ирмүүн B232270021
 * @version 1.0
 */

public class Appointment{
    private int id;
    private Client client;
    private Professional professional;
    private Service service;
    private LocalDate date;
    private int startHour;
    private int durationHours = 1; // hour
    private boolean isOnline; // false - meet in person, true - meet in online
    private boolean payByHour;
    private String notes;

    public Appointment(int id, Client client, Professional professional, Service service, 
                      LocalDate date, int startHour, int durationHours, boolean isOnline, 
                      boolean payByHour, String notes) {
        // Алдаа шалгах
        if (client == null) {
            throw new IllegalArgumentException("Client can not be null");
        }
        if (professional == null) {
            throw new IllegalArgumentException("Professional can not be null");
        }
        if (service == null) {
            throw new IllegalArgumentException("Service can not be null");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date can not be null");
        }
        //хугацаа нь өнгөрсөн цагийг шалгах
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("The date has expired.");
        }
        if (startHour < 9 || startHour > 17) {
            throw new IllegalArgumentException("Start hour must be between 9 and 17");
        }
        if (durationHours < 1 || durationHours > 8) {
            throw new IllegalArgumentException("Duration hour must be between 1 and 8");
        }
        if (startHour + durationHours > 18) {
            throw new IllegalArgumentException("exceed work time limit");
        }

        if (!service.isOfferedBy(professional)) {
            throw new IllegalArgumentException(
                professional.getName() + " do not provide this service.");
        }

        this.id = id;
        this.client = client;
        this.professional = professional;
        this.service = service;
        this.date = date;
        this.startHour = startHour;
        this.durationHours = durationHours;
        this.isOnline = isOnline;
        this.payByHour = payByHour;
        this.notes = notes;
    }

    // Getters
    public int getId() { return id; }
    public Client getClient() { return client; }
    public Professional getProfessional() { return professional; }
    public Service getService() { return service; }
    public LocalDate getDate() { return date; }
    public int getStartHour() { return startHour; }
    public int getDurationHours() { return durationHours; }
    public boolean isOnline() { return isOnline; }
    public String getLocation() {
        return isOnline ? "Online" : professional.getCompany().getAddress();
    }    public String getNotes() { return notes; }

    // Setters 
    public void setNotes(String notes) { this.notes = notes; }
    public void setOnline(boolean isOnline) { this.isOnline = isOnline; }

    // төлбөрийг тооцох функцүүд
    public double calculateFee() {
        if(payByHour){
            return durationHours * professional.getPricePerHour();
        } else 
        return professional.getPricePerHour();
    }

    @Override
    public String toString() {
        return String.format(
            "Appointment #%d: %s with %s on %s at %d:00 for %d hour(s) (%s) - Notes: %s",
            id,
            service.getName(),
            professional.getName(),
            date.toString(),
            startHour,
            durationHours,
            isOnline ? "Online" : "In-person at " + getLocation(),
            notes
        );
    }
}
