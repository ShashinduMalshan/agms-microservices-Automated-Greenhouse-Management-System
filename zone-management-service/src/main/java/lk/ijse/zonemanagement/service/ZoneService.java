package lk.ijse.zonemanagement.service;

import lk.ijse.zonemanagement.client.ExternalIoTClient;
import lk.ijse.zonemanagement.dto.AuthRequest;
import lk.ijse.zonemanagement.dto.AuthResponse;
import lk.ijse.zonemanagement.dto.DeviceRegistrationRequest;
import lk.ijse.zonemanagement.dto.DeviceRegistrationResponse;
import lk.ijse.zonemanagement.model.Zone;
import lk.ijse.zonemanagement.repository.ZoneRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ZoneService {

    private final ZoneRepository zoneRepository;
    private final ExternalIoTClient ioTClient;

    // Securely inject the hidden credentials!
    @Value("${external.iot.username}")
    private String iotUsername;

    @Value("${external.iot.password}")
    private String iotPassword;

    public ZoneService(ZoneRepository zoneRepository, ExternalIoTClient ioTClient) {
        this.zoneRepository = zoneRepository;
        this.ioTClient = ioTClient;
    }

    @Transactional
    public Zone createZone(Zone zone) {
        Zone savedZone = zoneRepository.save(zone);

        // Automatically Login using the injected environment variables
        AuthRequest authRequest = new AuthRequest(iotUsername, iotPassword);
        AuthResponse authResponse = ioTClient.login(authRequest);
        String token = authResponse.getAccessToken();

        // Prepare the payload for the External IoT API
        DeviceRegistrationRequest request = new DeviceRegistrationRequest(
                savedZone.getZoneName() + "-Sensor",
                savedZone.getId()
        );

        // Call the External API to register the device
        DeviceRegistrationResponse response = ioTClient.registerDevice("Bearer " + token, request);

        // Update a local database with the external device ID
        savedZone.setExternalDeviceId(response.getDeviceId());
        return zoneRepository.save(savedZone);
    }

    public Zone getZoneById(String id) {
        return zoneRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Zone with ID " + id + " not found."));
    }

    @Transactional
    public Zone updateZoneThresholds(String id, Zone updatedData) {
        Zone existingZone = zoneRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Zone with ID " + id + " not found."));

        existingZone.setMinTemp(updatedData.getMinTemp());
        existingZone.setMaxTemp(updatedData.getMaxTemp());

        return zoneRepository.save(existingZone);
    }

    @Transactional
    public void deleteZone(String id) {
        if (!zoneRepository.existsById(id)) {
            throw new IllegalArgumentException("Zone with ID " + id + " not found.");
        }
        zoneRepository.deleteById(id);
    }
}