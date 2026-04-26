package lk.ijse.sensortelemetry.controller;

import lk.ijse.sensortelemetry.dto.TelemetryResponse;
import lk.ijse.sensortelemetry.service.SensorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sensors")
public class SensorController {

    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @GetMapping("/latest")
    public ResponseEntity<TelemetryResponse> getLatestReading() {
        TelemetryResponse latest = sensorService.getLatestReading();

        if (latest == null) {
            return ResponseEntity.noContent().build(); // Return 204 if it hasn't fetched yet
        }

        return ResponseEntity.ok(latest);
    }
}