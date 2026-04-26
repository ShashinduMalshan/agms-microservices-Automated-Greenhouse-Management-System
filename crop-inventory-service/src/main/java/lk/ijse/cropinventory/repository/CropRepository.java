package lk.ijse.cropinventory.repository;

import lk.ijse.cropinventory.model.Crop;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CropRepository extends MongoRepository<Crop, String> {
}