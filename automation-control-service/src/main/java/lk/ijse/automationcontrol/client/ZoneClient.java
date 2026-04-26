package lk.ijse.automationcontrol.client;

import lk.ijse.automationcontrol.dto.ZoneDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "zone-management-service")
public interface ZoneClient {

    @GetMapping("/api/zones/{id}")
    ZoneDTO getZoneById(@PathVariable("id") String id);
}