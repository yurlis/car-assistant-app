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
import yurlis.carassistantapp.dto.carphoto.CarPhotoDto;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Data
@Accessors(chain = true)
public class UpdateCarRequestDto {
    private Optional<String> brand = Optional.empty();
    private Optional<String> model = Optional.empty();
    private Optional<Integer> yearOfManufacture = Optional.empty();
    private Optional<String> vinCode = Optional.empty();
    private Optional<String> colorCode = Optional.empty();
    private Optional<Long> mileage = Optional.empty();
    private Optional<Set<Long>> fuelTypes = Optional.empty();
    private Optional<Set<CarPhotoDto>> photos = Optional.empty();
}

/* update date for car

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