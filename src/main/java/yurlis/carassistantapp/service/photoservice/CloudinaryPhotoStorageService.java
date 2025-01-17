package yurlis.carassistantapp.service.photoservice;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryPhotoStorageService implements PhotoStorageService {

    private final Cloudinary cloudinary;

    @Override
    @SuppressWarnings("rawtypes")
    public String uploadPhoto(MultipartFile file) {
        try {
//            Map uploadParams = ObjectUtils.asMap(
//                    "use_filename", true,
//                    "unique_filename", false,
//                    "overwrite", true
//            );
            Map uploadParams = ObjectUtils.emptyMap(); // Налаштування для Cloudinary
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadParams);
            return (String) uploadResult.get("url");
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to Cloudinary", e);
        }
    }

    @Override
    public void deletePhoto(String publicId) {
        try {
            Map destroy = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            System.out.println(destroy.toString());
        } catch (IOException e) {
            System.err.println("Error details: " + e.getMessage());
            throw new RuntimeException("Failed to delete file from Cloudinary", e);
        }
    }

    @Override
    public void deletePhotoByUrl(String url) {
        String publicId = extractPublicIdFromUrl(url);
        deletePhoto(publicId);
    }

    private String extractPublicIdFromUrl(String url) {
        String[] parts = url.split("/");
        String fileName = parts[parts.length - 1];
        return fileName.split("\\.")[0];
    }
}
