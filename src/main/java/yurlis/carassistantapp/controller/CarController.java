package yurlis.carassistantapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import yurlis.carassistantapp.dto.car.CarWithoutPhotosDto;
import yurlis.carassistantapp.dto.car.CreateCarWithoutPhotosDto;
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
            description = "Add a new car for active User to the CarAssistant")
    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public CarWithoutPhotosDto createCar(Authentication authentication,
                                         @RequestBody @Valid CreateCarWithoutPhotosDto requestDto) {
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
}
