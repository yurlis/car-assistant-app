package yurlis.carassistantapp.event;

import org.springframework.context.ApplicationEvent;
import yurlis.carassistantapp.model.CarPhoto;

public class CarPhotoDeletedEvent extends ApplicationEvent {

    private final CarPhoto carPhoto;

    public CarPhotoDeletedEvent(Object source, CarPhoto carPhoto) {
        super(source);
        this.carPhoto = carPhoto;
    }

    public CarPhoto getCarPhoto() {
        return carPhoto;
    }
}
