package yurlis.carassistantapp.service.photoservice;

import org.springframework.web.multipart.MultipartFile;

public interface PhotoStorageService {
    String uploadPhoto(MultipartFile file);

    void deletePhoto(String publicId);

    void deletePhotoByUrl(String url);
}
