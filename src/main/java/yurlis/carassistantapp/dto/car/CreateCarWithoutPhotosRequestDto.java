package yurlis.carassistantapp.dto.car;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
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
import yurlis.carassistantapp.validator.yearofmanufacture.ValidYearOfManufacture;

import java.sql.Timestamp;
import java.util.Set;

@Data
@Accessors(chain = true)
public class CreateCarWithoutPhotosRequestDto {
    @NotBlank(message = "Brand cannot be empty")
    private String brand;

    @NotBlank(message = "Model cannot be empty")
    private String model;

    @NotNull(message = "Year of manufacture cannot be null")
    @Positive(message = "Year of manufacture must be positive")
    @ValidYearOfManufacture(message = "Year of manufacture cannot be in the future")
    private Integer yearOfManufacture;

    @Pattern(regexp = "^[A-HJ-NPR-Z0-9]{17}$",
            message = "VIN must be 17 characters long and consist of uppercase letters and digits")
    private String vinCode;

    @Schema(description = "The purchase date, either in ISO 8601 format (e.g., \"2025-01-05T06:11:40.000+00:00\") " +
            "or as milliseconds since 1970-01-01.")
    @PastOrPresent(message = "Purchase date cannot be in the future")
    //@JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp purchaseDate;

    @PositiveOrZero(message = "Mileage cannot be negative")
    private Long mileage;

    private String colorCode;

    @Size(max = 2, message = "Fuel types cannot exceed 2 items")
    @NotEmpty(message = "Fuel types cannot be empty")
    private Set<@Positive(message = "Fuel type IDs must be positive") Long> fuelTypes;
}
