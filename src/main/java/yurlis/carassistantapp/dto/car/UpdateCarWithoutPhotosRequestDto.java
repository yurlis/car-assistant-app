package yurlis.carassistantapp.dto.car;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.experimental.Accessors;
import yurlis.carassistantapp.validator.yearofmanufacture.ValidYearOfManufacture;

import java.sql.Timestamp;
import java.util.Set;

@Data
@Accessors(chain = true)
public class UpdateCarWithoutPhotosRequestDto {

    private String brand;

    private String model;

    @Positive(message = "Year of manufacture must be positive")
    @ValidYearOfManufacture
    private Integer yearOfManufacture;

    @Pattern(
            regexp = "^[A-HJ-NPR-Z0-9]{17}$",
            message = "VIN must be 17 characters long and consist of uppercase letters and digits"
    )
    private String vinCode;

    @PastOrPresent(message = "Purchase date cannot be in the future")
    private Timestamp purchaseDate;

    @PositiveOrZero(message = "Mileage cannot be negative")
    private Long mileage;

    private String colorCode;

    @Size(max = 2, message = "Fuel types cannot exceed 2 items")
    private Set<
            @Positive(message = "Fuel type IDs must be positive")
                    Long
            > fuelTypes;
}

/* update date for car
    private Optional<Set<CarPhotoDto>> photos = Optional.empty();

{
  "brand": "Toyota",
  "model": "Corolla",
  "yearOfManufacture": 2020,
  "vinCode": "JTDBU4EE9B9123456",
  "colorCode": "White",
  "mileage": 50000,
  "fuelTypes": [1, 2],
  "photos": [
    {
      "id": 101,
      "photoUrl": "https://example.com/photo1.jpg"
    },
    {
      "id": 102,
      "photoUrl": "https://example.com/photo2.jpg"
    }
  ]
}

*/