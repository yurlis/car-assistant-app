package yurlis.carassistantapp.listener;

import jakarta.persistence.PostUpdate;
import jakarta.persistence.PreRemove;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yurlis.carassistantapp.service.photoservice.PhotoStorageService;
import yurlis.carassistantapp.model.CarPhoto;

@NoArgsConstructor
public class CarPhotoListener {

    private PhotoStorageService photoStorageService;

    @Autowired
    public CarPhotoListener(PhotoStorageService photoStorageService) {
        this.photoStorageService = photoStorageService;
    }

//    @PostUpdate
//    @PreRemove
    public void beforeSoftRemove(CarPhoto carPhoto) {
        photoStorageService.deletePhotoByUrl(carPhoto.getPhotoUrl());
    }
}
