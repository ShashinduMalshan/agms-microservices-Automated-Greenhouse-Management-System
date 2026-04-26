package lk.ijse.zonemanagement.client;

import lk.ijse.zonemanagement.dto.AuthRequest;
import lk.ijse.zonemanagement.dto.AuthResponse;
import lk.ijse.zonemanagement.dto.DeviceRegistrationRequest;
import lk.ijse.zonemanagement.dto.DeviceRegistrationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "external-iot-service", url = "http://104.211.95.241:8080/api")
public interface ExternalIoTClient {

    @PostMapping("/auth/login")
    AuthResponse login(@RequestBody AuthRequest request);

    @PostMapping("/devices")
    DeviceRegistrationResponse registerDevice(
            @RequestHeader("Authorization") String token,
            @RequestBody DeviceRegistrationRequest request
    );
}