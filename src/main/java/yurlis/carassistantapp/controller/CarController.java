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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import yurlis.carassistantapp.dto.car.CarWithoutPhotosDto;
import yurlis.carassistantapp.dto.car.CreateCarWithoutPhotosRequestDto;
import yurlis.carassistantapp.dto.car.UpdateCarWithoutPhotosRequestDto;
import yurlis.carassistantapp.model.User;
import yurlis.carassistantapp.service.CarService;

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
    public CarWithoutPhotosDto createCar(Authentication authentication,
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
    public List<CarWithoutPhotosDto> getAllCarsForCurrentUser(Authentication authentication) {
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
                            schema = @Schema(implementation = CarWithoutPhotosDto.class)
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
    public CarWithoutPhotosDto updateCar(
            @PathVariable(name = "id") Long id,
            @RequestBody @Valid UpdateCarWithoutPhotosRequestDto updateRequestDto) {
        return carService.update(id, updateRequestDto);
    }
}
