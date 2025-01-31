package yurlis.carassistantapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
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
import yurlis.carassistantapp.dto.carrefuel.CarRefuelResponseDto;
import yurlis.carassistantapp.dto.pagination.CustomPage;
import yurlis.carassistantapp.model.User;
import yurlis.carassistantapp.service.car.CarPhotoService;
import yurlis.carassistantapp.service.car.CarService;
import yurlis.carassistantapp.service.carrefuel.CarRefuelService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

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
    private final CarRefuelService carRefuelService;

    @Operation(
            summary = "Create a new car (USER)",
            description = """
        Allows the authenticated user to create a new car. 
        The user must have the 'ROLE_USER' role.

        Example request body:
        {
            "brand": "Toyota",
            "model": "Corolla",
            "yearOfManufacture": 2022,
            "price": 20000.00
        }
    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Car successfully created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CarWithoutPhotosResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input or validation error"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied"
            )
    })
    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public CarWithoutPhotosResponseDto createCar(Authentication authentication,
                                                 @RequestBody @Valid CreateCarWithoutPhotosRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return carService.save(user.getId(), requestDto);
    }

    @Operation(
            summary = "Get all cars for the current user (USER)",
            description = """
        Retrieves a list of all cars owned by the authenticated user.
        The list excludes photos of the cars.
    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved the list of cars",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CarWithoutPhotosResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied"
            )
    })
    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<CarWithoutPhotosResponseDto> getAllCarsForCurrentUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return carService.findAllByUserId(user.getId());
    }

    @Operation(
            summary = "Delete a car (USER)",
            description = """
        Deletes a car owned by the authenticated user, identified by its ID.
        The car must belong to the user.

        Example:
        - Path variable `id`: 123

        If the car does not exist or is not owned by the user, an error will be returned.
    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Car successfully deleted"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Car not found or does not belong to the user"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied"
            )
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCar(@PathVariable Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        carService.deleteById(id, user.getId());
    }

    @Operation(
            summary = "Update a car (USER)",
            description = """
        Updates the details of a car owned by the authenticated user.
        The car must belong to the user.

        Example:
        {
            "brand": "Toyota",
            "model": "Corolla",
            "yearOfManufacture": 2015
        }
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
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied"
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
        Uploads a photo for a car owned by the authenticated user.
        The photo must be in JPG format, and the car must belong to the user.

        Example:
        - Path variable `id`: 123
        - File: photo.jpg
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
                    description = "Invalid input or unsupported file format"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied"
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
        Deletes a photo for a car owned by the authenticated user.
        The photo must belong to the user's car.

        Example:
        - Path variable `photoId`: 456
    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Photo successfully deleted"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Photo not found or does not belong to the user's car"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied"
            )
    })
    @DeleteMapping("/{photoId}/photo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Void> deletePhoto(@PathVariable Long photoId) {
        carService.deletePhotoById(photoId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get all photos of a car (USER)",
            description = """
        Retrieves all photos associated with a car owned by the authenticated user.

        Example:
        - Path variable `carId`: 123
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
                    description = "Access denied"
            )
    })
    @GetMapping("/{carId}/photos")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<CarPhotoResponseDto>> getAllCarPhotos(@PathVariable Long carId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<CarPhotoResponseDto> carPhotos = carPhotoService.getAllPhotosForCar(carId, user.getId());
        return ResponseEntity.ok(carPhotos);
    }

    @Operation(
            summary = "Get all refuels for a specific car",
            description = """
                    Retrieves a paginated list of refuel records for the specified car.
                    Supports optional filtering by date range and fuel type.
                
                    Example request:
                    GET /cars/{carId}/refuels?page=0&size=10&sort=date,desc&startDate=2024-01-01&endDate=2024-12-31&fuelType=DIESEL
                """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List of refuel records successfully retrieved",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CarRefuelResponseDto.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request parameters"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Car not found"
            )
    })
    @GetMapping("/{carId}/refuels")
    @ResponseStatus(HttpStatus.OK)
    public CustomPage<CarRefuelResponseDto> getRefuelsForCar(
            @PathVariable Long carId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) Long fuelType,
            @ParameterObject Pageable pageable
    ) {
        if (startDate == null) {
            startDate = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
        }
        if (endDate == null) {
            endDate = LocalDateTime.now();
        }

        return carRefuelService.getRefuelsForCar(carId, Timestamp.valueOf(startDate), Timestamp.valueOf(endDate), fuelType, pageable);
    }
}
