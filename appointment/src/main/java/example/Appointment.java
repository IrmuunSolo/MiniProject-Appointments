import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Appointment implements Serializable {
    private int id;
    private Client client;
    private Professional professional;
    private Service service;
    private LocalDate date;
    private int startHour;
    private int duration;
    private boolean isOnline;
    private String location;
    private String notes;

    public Appointment(int id, Client client, Professional professional, Service service, 
                      LocalDate date, int startHour, int duration, boolean isOnline, 
                      String location, String notes) {
        this.id = id;
        this.client = client;
        this.professional = professional;
        this.service = service;
        this.date = date;
        this.startHour = startHour;
        this.duration = duration;
        this.isOnline = isOnline;
        this.location = location;
        this.notes = notes;
    }

    // Getters
    public int getId() { return id; }
    public Client getClient() { return client; }
    public Professional getProfessional() { return professional; }
    public Service getService() { return service; }
    public LocalDate getDate() { return date; }
    public int getStartHour() { return startHour; }
    public int getDuration() { return duration; }
    public boolean isOnline() { return isOnline; }
    public String getLocation() { return location; }
    public String getNotes() { return notes; }

    // Setters 
    public void setNotes(String notes) { this.notes = notes; }
    public void setLocation(String location) { this.location = location; }
    public void setOnline(boolean online) { isOnline = online; }

    @Override
    public String toString() {
        return String.format(
            "Appointment #%d: %s with %s on %s at %d:00 for %d hour(s) (%s)",
            id,
            service.getName(),
            professional.getName(),
            date.toString(),
            startHour,
            duration,
            isOnline ? "Online" : "In-person at " + location
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}