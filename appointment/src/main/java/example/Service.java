package example;

public class Service {

    private int id;
    private String name;
    private String description;
    private Professional[] professionals;
    private int defaultDuration; // in hours

    public Service(int id, String name, String description, Professional[] professionals, int defaultDuration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.professionals = professionals;
        this.defaultDuration = defaultDuration;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Professional[] getProfessionals() {
        return professionals;
    }

    public int getDefaultDuration() {
        return defaultDuration;
    }
}
