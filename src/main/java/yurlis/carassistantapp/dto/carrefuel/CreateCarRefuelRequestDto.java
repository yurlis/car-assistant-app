package yurlis.carassistantapp.dto.carrefuel;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.experimental.Accessors;
import yurlis.carassistantapp.model.CarRefuel;
import yurlis.carassistantapp.validator.enumvalidator.EnumValue;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Accessors(chain = true)
public class CreateCarRefuelRequestDto {
    @PastOrPresent(message = "Refuel Time cannot be in the future")
    @NotNull(message = "Refuel Time cannot be null")
    private Timestamp refuelTime;

    @NotNull
    private String gasStationName;

    @Positive(message = "Fuel quantity must be positive")
    @NotNull(message = "Fuel quantity cannot be null")
    private BigDecimal fuelQuantity;

    @Positive(message = "Cost per liter must be positive")
    @NotNull(message = "Cost per liter cannot be null")
    private BigDecimal costPerLiter;

    @EnumValue(enumClass = CarRefuel.MovementType.class, message = "Invalid movement type")
    @NotNull(message = "Movement type cannot be null")
    private String movementType;

    @NotNull
    private Long fuelType;
}

