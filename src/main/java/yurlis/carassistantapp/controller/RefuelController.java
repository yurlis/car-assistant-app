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
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import yurlis.carassistantapp.dto.carrefuel.CarRefuelResponseDto;
import yurlis.carassistantapp.dto.carrefuel.CreateCarRefuelRequestDto;
import yurlis.carassistantapp.dto.carrefuel.MovementTypeDto;
import yurlis.carassistantapp.model.CarRefuel;
import yurlis.carassistantapp.service.carrefuel.CarRefuelService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/refuels")
@Tag(
        name = "Car Refuel Management",
        description = "Endpoints for managing car refuels within the application."
)
@SecurityRequirement(name = "bearerAuth")
public class RefuelController {
    private final CarRefuelService carRefuelService;

    @Operation(
            summary = "Create a new car refuel record (USER)",
            description = """
                    Allows an authenticated user to create a new car refuel record.
                    The user must have the 'ROLE_USER' role.
                    
                    Example request body:
                    {
                        "liters": 50.5,
                        "pricePerLiter": 1.85,
                        "totalCost": 93.43,
                        "odometer": 15000,
                        "refuelDate": "2024-01-29",
                        "movementType": "REFUEL"
                    }
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Refuel record successfully created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CarRefuelResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input or validation error"
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
    @PostMapping("/{carId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public CarRefuelResponseDto createRefuel(@PathVariable Long carId,
                                             @RequestBody @Valid CreateCarRefuelRequestDto refuelRequestDto) {
        return carRefuelService.createRefuel(carId, refuelRequestDto);
    }

    @Operation(
            summary = "Get list of movement types",
            description = "Retrieves a list of all available movement types for car refuels."
    )
    @ApiResponse(
            responseCode = "200",
            description = "List of movement types successfully retrieved",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = MovementTypeDto.class)))
    )
    @GetMapping("/movement-types")
    public List<MovementTypeDto> getMovementTypes() {
        return Arrays.stream(CarRefuel.MovementType.values())
                .map(movementType -> new MovementTypeDto()
                        .setCode(movementType.name())
                        .setDescription(movementType.getDescription()))
                .collect(Collectors.toList());
    }

    @Operation(
            summary = "Update an existing refuel record",
            description = """
                        Allows an authenticated user to update an existing refuel record for a car.
                        The user must have the 'ROLE_USER' role.
                    
                        Example request body:
                        {
                            "fuelAmount": 50.5,
                            "pricePerLiter": 1.20,
                            "odometer": 123456
                        }
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Refuel record successfully updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CarRefuelResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input or validation error"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Refuel record not found"
            )
    })
    @PutMapping("/{refuelId}")
    @ResponseStatus(HttpStatus.OK)
    public CarRefuelResponseDto updateRefuel(@PathVariable Long refuelId,
                                             @RequestBody @Valid CreateCarRefuelRequestDto refuelRequestDto) {
        return carRefuelService.updateRefuel(refuelId, refuelRequestDto);
    }

    @Operation(
            summary = "Delete a refuel record",
            description = """
                        Allows an authenticated user to delete a refuel record by ID.
                        The user must have the 'ROLE_USER' role.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Refuel record successfully deleted"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Refuel record not found"
            )
    })
    @DeleteMapping("/{refuelId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRefuel(@PathVariable Long refuelId) {
        carRefuelService.deleteCarRefuel(refuelId);
    }
}
