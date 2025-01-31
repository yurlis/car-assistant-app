package yurlis.carassistantapp.dto.carrefuel;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MovementTypeDto {
    private String code;
    private String description;
}