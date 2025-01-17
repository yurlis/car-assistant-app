package yurlis.carassistantapp.dto.car;

public record CarPhotoResponseDto(
        Long carId,
        Long photoId,
        String photoUrl
) {
}
