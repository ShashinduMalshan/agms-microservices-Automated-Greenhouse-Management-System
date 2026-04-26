package lk.ijse.zonemanagement.controller;

import lk.ijse.zonemanagement.model.Zone;
import lk.ijse.zonemanagement.service.ZoneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/zones")
public class ZoneController {

    private final ZoneService zoneService;

    public ZoneController(ZoneService zoneService) {
        this.zoneService = zoneService;
    }

    // POST- /api/zones (Create zone + register device)
    @PostMapping
    public ResponseEntity<Zone> createZone(@RequestBody Zone zone) {
        Zone createdZone = zoneService.createZone(zone);
        return new ResponseEntity<>(createdZone, HttpStatus.CREATED);
    }

    // GET- /api/zones/{id} (Fetch specific zone details)
    @GetMapping("/{id}")
    public ResponseEntity<Zone> getZone(@PathVariable String id) {
        Zone zone = zoneService.getZoneById(id);
        return new ResponseEntity<>(zone, HttpStatus.OK);
    }

    // PUT- /api/zones/{id} (Update thresholds)
    @PutMapping("/{id}")
    public ResponseEntity<Zone> updateZone(@PathVariable String id, @RequestBody Zone zone) {
        Zone updatedZone = zoneService.updateZoneThresholds(id, zone);
        return new ResponseEntity<>(updatedZone, HttpStatus.OK);
    }

    // DELETE- /api/zones/{id} (Remove zone)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteZone(@PathVariable String id) {
        zoneService.deleteZone(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}