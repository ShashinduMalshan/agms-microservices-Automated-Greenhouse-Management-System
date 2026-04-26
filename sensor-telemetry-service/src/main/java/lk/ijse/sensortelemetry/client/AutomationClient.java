package lk.ijse.sensortelemetry.client;

import lk.ijse.sensortelemetry.dto.SensorData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "automation-control-service")
public interface AutomationClient {

    @PostMapping("/api/automation/process")
    void sendSensorData(@RequestBody SensorData sensorData);
}