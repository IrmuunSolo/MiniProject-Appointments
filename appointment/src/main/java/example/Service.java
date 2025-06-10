package example;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Үйлчилгээний мэдээллийг хадгалах класс
 */

public class Service {

    Logger logger = LogManager.getLogger(Service.class);

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    private int id;
    private String name;
    private String description;
    private List<Professional> professionals;
    private int defaultDurationHours; // in hours

    public Service(int id, String name, String description, 
                  Professional[] professionals, int defaultDurationHours) {
        try {
            // ID эерэг байх
            if (id <= 0) {
                throw new IllegalArgumentException("ID must be a positive number");
            }
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Service name cannot be null or empty");
            }
            // description хоосон байж болно, null байж болохгүй
            if (description == null) {
                throw new IllegalArgumentException("Description cannot be null");
            }
            // professionals массивт null утга байгааг шалгах
            for (Professional p : professionals) {
                if (p == null) {
                    throw new IllegalArgumentException("Professionals array cannot contain null elements");
                }
            }
            // duration 1-12 цагийн хооронд байх
            if (defaultDurationHours <= 0 || defaultDurationHours > 12) {
                throw new IllegalArgumentException("Default duration must be between 1 and 12 hours");
            }

            this.id = id;
            this.name = name;
            this.description = description;
            this.professionals = new ArrayList<>(Arrays.asList(professionals));
            this.defaultDurationHours = defaultDurationHours;
            
            logger.info("Created new service: {}", this);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to create service: {}", e.getMessage());
            throw e;
        }
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

    /**
     * Мэргэжилтнийг нэмнэ
     * @param professional Мэргэжилтэн
     * @throws IllegalArgumentException professional null байвал
     */

    public String addProfessional(Professional pro) {
        try {
            if (pro == null) {
                throw new IllegalArgumentException("Professional cannot be null");
            }
            if (professionals.contains(pro)) {
                String msg = pro.getName() + " is already associated with this service";
                logger.warn(msg);
                return msg;
            }
            professionals.add(pro);
            String msg = pro.getName() + " added professional to service";
            logger.info(msg);
            return msg;
        } catch (IllegalArgumentException e) {
            logger.error("Failed to add professional: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Мэргэжилтнийг бүртгэлээс хасах
     * @param professional Мэргэжилтэн
     * @throws IllegalArgumentException professional бүртгэлгүй байвал
     * @return мэргэжилтийн хассан бол true, чадаагүй бол false буцаана
     */
    public String removeProfessional(Professional pro) {
        try{
            if (pro == null) {
                throw new IllegalArgumentException("Professional cannot be null");
            }
            if (!professionals.contains(pro)) {
                String msg = pro.getName() + " is not associated with this service";
                logger.warn(msg);                
                return msg;
            }
            if (professionals.size() <= 1) {
                throw new IllegalStateException("Service must have at least one professional");
            }
            professionals.remove(pro);
            String msg = pro.getName() + " removed professional to service";
            logger.info(msg);
            return msg;
        }catch (IllegalArgumentException e) {
            logger.error("Failed to remove professional: {}", e.getMessage());
            throw e;
        }
    }

    // Энэ үйлчилгээнд тухайн мэргэжилтэн байгааг шалгана
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
