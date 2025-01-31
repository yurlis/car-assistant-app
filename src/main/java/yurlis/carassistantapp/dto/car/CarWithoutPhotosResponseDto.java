package yurlis.carassistantapp.dto.car;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String formattedPurchaseDate;
    private Long mileage;
    private String colorCode;
    private Set<Long> fuelTypesIds;
}
