package yurlis.carassistantapp.dto.car;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
public class CarWithoutPhotosResponseDto {
    private Long id;
    private Long userId;
    private String brand;
    private String model;
    private Integer yearOfManufacture;
    private String vinCode;
    private Long purchaseDate;
    private Long mileage;
    private String colorCode;
    private Set<Long> fuelTypesIds;
}
