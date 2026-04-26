package lk.ijse.sensortelemetry.dto;

public class TelemetryResponse {
    private String deviceId;
    private String zoneId;
    private TelemetryValue value;
    private String capturedAt;

    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public String getZoneId() { return zoneId; }
    public void setZoneId(String zoneId) { this.zoneId = zoneId; }
    public TelemetryValue getValue() { return value; }
    public void setValue(TelemetryValue value) { this.value = value; }
    public String getCapturedAt() { return capturedAt; }
    public void setCapturedAt(String capturedAt) { this.capturedAt = capturedAt; }
}