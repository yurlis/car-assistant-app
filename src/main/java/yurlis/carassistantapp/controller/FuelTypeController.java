package yurlis.carassistantapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yurlis.carassistantapp.dto.fueltypes.FuelTypeDto;
import yurlis.carassistantapp.service.FuelTypeService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/fuel-types")
@Tag(
        name = "Fuel Type Management",
        description = "Endpoints for managing fuel types within the application."
)
@SecurityRequirement(name = "bearerAuth")
public class FuelTypeController {

    private final FuelTypeService fuelTypeService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(
            summary = "Get all fuel types",
            description = "Retrieve a list of all available fuel types. This endpoint is accessible only to users with the 'USER' role. It returns a list of fuel types such as 'Petrol', 'Diesel', 'Gas LPG', etc.",
            tags = {"Fuel Type Management"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of fuel types retrieved successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access, token required"),
                    @ApiResponse(responseCode = "403", description = "Forbidden, user does not have the required role")
            }
    )
    public List<FuelTypeDto> getAllFuelTypes() {
        return fuelTypeService.getAllFuelTypes();
    }
}