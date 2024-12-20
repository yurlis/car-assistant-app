package yurlis.carassistantapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fuel_types")
@NoArgsConstructor
@Getter
@Setter
public class FuelType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fuel_type", nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private FuelTypeEnum fuelType;

    public enum FuelTypeEnum {
        PETROL,
        DIESEL,
        GAS_LPG,
        GAS_CNG,
        HYBRID,
        ELECTRO
    }
}