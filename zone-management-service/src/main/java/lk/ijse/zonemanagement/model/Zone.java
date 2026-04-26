package lk.ijse.zonemanagement.model;

import jakarta.persistence.*;

@Entity
@Table(name = "zones")
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String zoneName;
    private String cropType;

    //hold the ID returned from the External IoT API later!
    private String externalDeviceId;

    private double minTemp;
    private double maxTemp;

    public Zone() {}

    @PrePersist
    @PreUpdate
    private void validateTemperatures() {
        if (minTemp >= maxTemp) {
            throw new IllegalArgumentException("Minimum temperature must be strictly less than maximum temperature.");
        }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getZoneName() { return zoneName; }
    public void setZoneName(String zoneName) { this.zoneName = zoneName; }

    public String getCropType() { return cropType; }
    public void setCropType(String cropType) { this.cropType = cropType; }

    public String getExternalDeviceId() { return externalDeviceId; }
    public void setExternalDeviceId(String externalDeviceId) { this.externalDeviceId = externalDeviceId; }

    public double getMinTemp() { return minTemp; }
    public void setMinTemp(double minTemp) { this.minTemp = minTemp; }

    public double getMaxTemp() { return maxTemp; }
    public void setMaxTemp(double maxTemp) { this.maxTemp = maxTemp; }
}