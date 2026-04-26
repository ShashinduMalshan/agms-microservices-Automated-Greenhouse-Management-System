package lk.ijse.cropinventory.controller;

import lk.ijse.cropinventory.model.Crop;
import lk.ijse.cropinventory.model.CropStatus;
import lk.ijse.cropinventory.repository.CropRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/crops")
public class CropController {

    @Autowired
    private CropRepository cropRepository;

    //Register new batch (Defaults SEEDLING)
    @PostMapping
    public ResponseEntity<Crop> registerCropBatch(@RequestBody Crop crop) {
        crop.setStatus(CropStatus.SEEDLING);
        Crop savedCrop = cropRepository.save(crop);
        return ResponseEntity.ok(savedCrop);
    }

    //View current inventory
    @GetMapping
    public ResponseEntity<List<Crop>> getAllCrops() {
        return ResponseEntity.ok(cropRepository.findAll());
    }

    //Update lifecycle stage
    @PutMapping("/{id}/status")
    public ResponseEntity<Crop> updateCropStatus(@PathVariable String id, @RequestParam CropStatus status) {
        Optional<Crop> optionalCrop = cropRepository.findById(id);

        if (optionalCrop.isPresent()) {
            Crop crop = optionalCrop.get();
            crop.setStatus(status);
            return ResponseEntity.ok(cropRepository.save(crop));
        }

        return ResponseEntity.notFound().build();
    }
}