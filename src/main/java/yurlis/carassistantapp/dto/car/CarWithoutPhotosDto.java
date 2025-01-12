package yurlis.carassistantapp.dto.car;

import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;
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
    private Timestamp purchaseDate;
    private Long mileage;
    private String colorCode;
    private Set<Long> fuelTypesIds;
}
