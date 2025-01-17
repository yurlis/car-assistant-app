package yurlis.carassistantapp.listener;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import yurlis.carassistantapp.event.CarPhotoDeletedEvent;
import yurlis.carassistantapp.model.CarPhoto;
import yurlis.carassistantapp.service.photoservice.PhotoStorageService;

public class CarPhotoEventListener {

    private final PhotoStorageService photoStorageService;

    public CarPhotoEventListener(PhotoStorageService photoStorageService) {
        this.photoStorageService = photoStorageService;
    }

    @EventListener
    public void handleCarPhotoDeletedEvent(CarPhotoDeletedEvent event) {
        CarPhoto carPhoto = event.getCarPhoto();
        if (carPhoto.isDeleted()) {
            deletePhotoAsync(carPhoto);
        }
    }

    @Async
    public void deletePhotoAsync(CarPhoto carPhoto) {
        photoStorageService.deletePhotoByUrl(carPhoto.getPhotoUrl());
    }
}
