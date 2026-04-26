package lk.ijse.automationcontrol.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "action_logs")
public class ActionLog {

    @Id
    private String id = UUID.randomUUID().toString();
    private String zoneId;
    private String deviceId;
    private double currentTemperature;
    private String actionTaken; //"COOLER_ON", "HEATER_ON", "NORMAL"
    private LocalDateTime timestamp = LocalDateTime.now();

    public ActionLog() {}

    public ActionLog(String zoneId, String deviceId, double currentTemperature, String actionTaken) {
        this.zoneId = zoneId;
        this.deviceId = deviceId;
        this.currentTemperature = currentTemperature;
        this.actionTaken = actionTaken;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getZoneId() { return zoneId; }
    public void setZoneId(String zoneId) { this.zoneId = zoneId; }
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public double getCurrentTemperature() { return currentTemperature; }
    public void setCurrentTemperature(double currentTemperature) { this.currentTemperature = currentTemperature; }
    public String getActionTaken() { return actionTaken; }
    public void setActionTaken(String actionTaken) { this.actionTaken = actionTaken; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}