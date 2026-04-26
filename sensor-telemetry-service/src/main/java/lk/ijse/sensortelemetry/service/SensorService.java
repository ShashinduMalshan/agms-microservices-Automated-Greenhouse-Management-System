package lk.ijse.sensortelemetry.service;

import lk.ijse.sensortelemetry.client.AutomationClient;
import lk.ijse.sensortelemetry.client.ExternalIoTClient;
import lk.ijse.sensortelemetry.dto.AuthRequest;
import lk.ijse.sensortelemetry.dto.Device;
import lk.ijse.sensortelemetry.dto.SensorData;
import lk.ijse.sensortelemetry.dto.TelemetryResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorService {

    private final ExternalIoTClient ioTClient;
    private final AutomationClient automationClient;

    private TelemetryResponse latestReading;

    @Value("${external.iot.username}")
    private String iotUsername;

    @Value("${external.iot.password}")
    private String iotPassword;

    public SensorService(ExternalIoTClient ioTClient, AutomationClient automationClient) {
        this.ioTClient = ioTClient;
        this.automationClient = automationClient;
    }

    @Scheduled(fixedRate = 10000)
    public void fetchAndPushTelemetry() {
        try {
            String token = ioTClient.login(new AuthRequest(iotUsername, iotPassword)).getAccessToken();
            String bearerToken = "Bearer " + token;

            List<Device> devices = ioTClient.getAllDevices(bearerToken);

            for (Device device : devices) {
                TelemetryResponse telemetry = ioTClient.getTelemetry(bearerToken, device.getDeviceId());
                this.latestReading = telemetry;

                System.out.println("Sensor fetched data for Zone: " + device.getZoneId() +
                        " | Temp: " + telemetry.getValue().getTemperature() + "°C");

                SensorData dataToPush = new SensorData(
                        telemetry.getZoneId(),
                        telemetry.getDeviceId(),
                        telemetry.getValue().getTemperature()
                );
                automationClient.sendSensorData(dataToPush);
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch/push telemetry: " + e.getMessage());
        }
    }

    public TelemetryResponse getLatestReading() {
        return this.latestReading;
    }
}