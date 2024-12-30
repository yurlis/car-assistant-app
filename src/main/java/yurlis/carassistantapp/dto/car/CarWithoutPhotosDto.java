package yurlis.carassistantapp.dto.car;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import yurlis.carassistantapp.dto.carphoto.CarPhotoDto;

import java.time.LocalDate;
import java.util.Set;

@Data
@Accessors(chain = true)
public class CarWithoutPhotosDto {
    private Long id;
    private Long userId;
    private String brand;
    private String model;
    private Integer yearOfManufacture;
    private String vinCode;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate purchaseDate;
    private Long mileage;
    private String colorCode;
    private Set<Long> fuelTypesIds;
}
