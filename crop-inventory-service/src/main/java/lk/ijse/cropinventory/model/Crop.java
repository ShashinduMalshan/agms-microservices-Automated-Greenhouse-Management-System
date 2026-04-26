package lk.ijse.cropinventory.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "crops")
public class Crop {

    @Id
    private String id;
    private String cropName;
    private String batchId;
    private CropStatus status;

    public Crop() {}

    public Crop(String cropName, String batchId, CropStatus status) {
        this.cropName = cropName;
        this.batchId = batchId;
        this.status = status;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCropName() { return cropName; }
    public void setCropName(String cropName) { this.cropName = cropName; }

    public String getBatchId() { return batchId; }
    public void setBatchId(String batchId) { this.batchId = batchId; }

    public CropStatus getStatus() { return status; }
    public void setStatus(CropStatus status) { this.status = status; }
}