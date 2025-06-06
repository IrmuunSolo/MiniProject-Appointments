package example;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class Service {

    private int id;
    private String name;
    private String description;
    private List<Professional> professionals;
    private int defaultDurationHours; // in hours

    public Service(int id, String name, String description, 
                  Professional[] professionals, int defaultDurationHours) {
        if (professionals == null || professionals.length == 0) {
            throw new IllegalArgumentException("Service must have at least one professional");
        }
        this.id = id;
        this.name = name;
        this.description = description;
        this.professionals = new ArrayList<>(Arrays.asList(professionals));
        this.defaultDurationHours = defaultDurationHours;
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
        return professionals.toArray(new Professional[0]); 
    }
    public int getDefaultDurationHours() { 
        return defaultDurationHours; 
    }

    public String addProfessional(Professional pro) {
        if (pro == null) {
            throw new IllegalArgumentException("Professional cannot be null");
        }
        if (professionals.contains(pro)) {
            return pro.getName() + " is already associated with this service";
        }
        professionals.add(pro);
        return pro.getName() + " added professional to service"; // changed to english
    }

    public String removeProfessional(Professional pro) {
        if (pro == null) {
            throw new IllegalArgumentException("Professional cannot be null");
        }
        if (!professionals.contains(pro)) {
            return pro.getName() + " is not associated with this service";
        }
        if (professionals.size() <= 1) {
            throw new IllegalStateException("Service must have at least one professional");
        }
        professionals.remove(pro);
        return pro.getName() + " removed professional from service"; // changed to english
    }

    public boolean isOfferedBy(Professional professional) {
        return professionals.contains(professional);
    }

        @Override
    public String toString() {
        return "Service{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", professionals=" + professionals.size() +
                ", defaultDurationHours=" + defaultDurationHours +
                '}';
    }
}
