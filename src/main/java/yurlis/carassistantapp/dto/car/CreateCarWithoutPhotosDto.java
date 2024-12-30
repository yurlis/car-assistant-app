package yurlis.carassistantapp.dto.car;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;
import yurlis.carassistantapp.validator.date.ValidDateFormat;

import java.time.LocalDate;
import java.util.Set;

@Data
@Accessors(chain = true)
public class CreateCarWithoutPhotosDto {
    @NotBlank(message = "Brand cannot be empty")
    private String brand;

    @NotBlank(message = "Model cannot be empty")
    private String model;

    @NotNull(message = "Year of manufacture cannot be null")
    @Positive(message = "Year of manufacture must be positive")
    private Integer yearOfManufacture;

    @Pattern(regexp = "^[A-HJ-NPR-Z0-9]{17}$",
            message = "VIN must be 17 characters long and consist of uppercase letters and digits")
    private String vinCode;

    // @ValidDateFormat(message = "Purchase date must be in format dd-MM-yyyy")
  //  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @PastOrPresent(message = "Purchase date cannot be in the future")
    private LocalDate purchaseDate;

    @PositiveOrZero(message = "Mileage cannot be negative")
    private Long mileage;

    private String colorCode;

    @Size(max = 2, message = "Fuel types cannot exceed 2 items")
    @NotEmpty(message = "Fuel types cannot be empty")
    private Set<@Positive(message = "Fuel type IDs must be positive") Long> fuelTypes;
}
