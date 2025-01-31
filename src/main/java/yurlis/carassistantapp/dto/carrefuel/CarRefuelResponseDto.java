package yurlis.carassistantapp.dto.carrefuel;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class CarRefuelResponseDto {
    private Long id;
    private Long refuelTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String formattedRefuelTime;
    private String gasStationName;
    private Long carId;
    private BigDecimal fuelQuantity;
    private BigDecimal costPerLiter;
    private String movementType;
}
