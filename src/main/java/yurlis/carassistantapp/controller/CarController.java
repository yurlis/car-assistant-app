package yurlis.carassistantapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import yurlis.carassistantapp.dto.car.CarPhotoResponseDto;
import yurlis.carassistantapp.dto.car.CarWithoutPhotosResponseDto;
import yurlis.carassistantapp.dto.car.CreateCarWithoutPhotosRequestDto;
import yurlis.carassistantapp.dto.car.UpdateCarWithoutPhotosRequestDto;
import yurlis.carassistantapp.model.Car;
import yurlis.carassistantapp.model.CarPhoto;
import yurlis.carassistantapp.model.User;
import yurlis.carassistantapp.service.CarPhotoService;
import yurlis.carassistantapp.service.CarService;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/cars")
@Tag(
        name = "Car Management",
        description = "Endpoints for managing cars within the application."
)
@SecurityRequirement(name = "bearerAuth")
public class CarController {
    private final CarService carService;
    private final CarPhotoService carPhotoService;

    @Operation(
            summary = "Create a new car for the current user (USER)",
            description = """
                Adds a new car for the currently authenticated user to the CarAssistant system.
                The user must be authenticated and authorized as 'ROLE_USER'.
                
                Example request body:
                {
                    "brand": "Toyota",
                    "model": "Corolla",
                    "yearOfManufacture": 2022,
                    "price": 20000.00
                }
            """
    )
    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public CarWithoutPhotosResponseDto createCar(Authentication authentication,
                                                 @RequestBody @Valid CreateCarWithoutPhotosRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return carService.save(user.getId(), requestDto);
    }

    @Operation(
            summary = "Get all cars for the current user without photos (USER)",
            description = "Retrieve a list of all cars associated with the currently authenticated user, excluding photos."
    )
    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<CarWithoutPhotosResponseDto> getAllCarsForCurrentUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return carService.findAllByUserId(user.getId());
    }

    @Operation(
            summary = "Delete a car (USER)",
            description = """
                Allows the currently authenticated user to delete a specific car by its unique ID.
                The car must belong to the user.
                Returns no content (204) upon successful deletion.
                
                If the car does not exist or does not belong to the authenticated user, an error will be returned.
            """
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCar(@PathVariable Long id) {
        carService.deleteById(id);
    }

    @Operation(
            summary = "Update a car (USER)",
            description = """
        Allows the currently authenticated user to update the details of a specific car by its unique ID.
        Accepts an UpdateCarWithoutPhotosRequestDto object and returns an updated CarWithoutPhotosDto object.
        Only the fields provided in the request will be updated; others will retain their current values.
        The car must belong to the user.

        Example of partial update:
        {
            "brand": "Toyota",
            "model": "Corolla",
            "yearOfManufacture": 2015
        }

        If the car does not exist or does not belong to the user, an error will be returned.
        """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Car successfully updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CarWithoutPhotosResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Car not found or does not belong to the user"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input or validation error"
            )
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<CarWithoutPhotosResponseDto> updateCar(
            @PathVariable(name = "id") Long id,
            @RequestBody @Valid UpdateCarWithoutPhotosRequestDto updateRequestDto) {
        CarWithoutPhotosResponseDto updatedCar = carService.update(id, updateRequestDto);
        return ResponseEntity.ok(updatedCar);
    }

    @Operation(
            summary = "Upload a car photo (USER)",
            description = """
        Allows the currently authenticated user to upload a photo (JPG) for a specific car by its unique ID.
        The file must be a valid JPG image, and the car must belong to the user.

        Example:
        - Path variable `id`: 123
        - File: photo.jpg

        If the car does not exist or does not belong to the user, an error will be returned.
        """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Photo successfully uploaded",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CarPhotoResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Car not found or does not belong to the user"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input, validation error, or unsupported file format"
            )
    })
    @PostMapping("/{id}/photo")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<CarPhotoResponseDto> uploadCarPhoto(
            @PathVariable(name = "id") Long carId,
            @RequestParam("photo") MultipartFile photo) {
        if (!photo.getContentType().equals("image/jpeg")) {
            throw new IllegalArgumentException("Only JPG files are allowed.");
        }

        CarPhotoResponseDto responseDto = carService.uploadPhoto(carId, photo);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
            summary = "Delete a car photo (USER)",
            description = """
            Deletes a photo by its unique ID. This operation removes the photo from both the storage (e.g., Cloudinary) and the database.
            
            Example:
            - Path variable `photoId`: 456
            
            The user must have the necessary permissions to delete the photo. If the photo ID is invalid, an error will be returned.
            """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Photo successfully deleted"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Photo not found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied",
                    content = @Content
            )
    })
    @DeleteMapping("/{photoId}/photo")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Void> deletePhoto(@PathVariable Long photoId) {
        carService.deletePhotoById(photoId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get all photos of a car (USER)",
            description = """
            Retrieves all photos associated with a car by its unique ID. This operation fetches a list of all car photos, including URLs and other related information.
            
            Example:
            - Path variable `carId`: 123
            
            The user must be authenticated and authorized to access the photos of this car. If the car does not exist or is not owned by the authenticated user, an error will be returned.
            """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of car photos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CarPhotoResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Car not found or does not belong to the user"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied",
                    content = @Content
            )
    })
    @GetMapping("/{carId}/photos")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<CarPhotoResponseDto>> getAllCarPhotos(@PathVariable Long carId) {
        List<CarPhotoResponseDto> carPhotos = carPhotoService.getAllPhotosForCar(carId);
        return ResponseEntity.ok(carPhotos);
    }
}
