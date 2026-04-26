package lk.ijse.sensortelemetry.dto;

public class SensorData {
    private String zoneId;
    private String deviceId;
    private double temperature;

    public SensorData(String zoneId, String deviceId, double temperature) {
        this.zoneId = zoneId;
        this.deviceId = deviceId;
        this.temperature = temperature;
    }

    public String getZoneId() { return zoneId; }
    public void setZoneId(String zoneId) { this.zoneId = zoneId; }
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }
}