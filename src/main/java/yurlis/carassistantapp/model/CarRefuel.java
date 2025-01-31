package yurlis.carassistantapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "car_refuels")
@SQLRestriction("is_deleted = FALSE")
@SQLDelete(sql = "UPDATE car_refuels SET is_deleted = TRUE WHERE id = ?")
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class CarRefuel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Timestamp refuelTime;

    @Column(nullable = false)
    private String gasStationName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "car_id", nullable = false)
    @EqualsAndHashCode.Exclude
    private Car car;

    @Column(nullable = false)
    private BigDecimal fuelQuantity;

    @Column(nullable = false)
    private BigDecimal costPerLiter;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MovementType movementType;

    public enum MovementType {
        CITY("По місту"),
        HIGHWAY("Шосе"),
        MIXED("Змішаний");

        private final String description;

        MovementType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    @EqualsAndHashCode.Exclude
    private FuelType fuelType;

    @Column(name = "is_deleted", nullable = false)
    @ColumnDefault("false")
    private boolean isDeleted = false;
}
