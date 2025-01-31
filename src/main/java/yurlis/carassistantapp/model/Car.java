package yurlis.carassistantapp.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cars")
@SQLRestriction("is_deleted = FALSE")
@SQLDelete(sql = "UPDATE cars SET is_deleted = TRUE WHERE id = ?")
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    private Integer yearOfManufacture;

    private String vinCode;

    private Timestamp purchaseDate;

    private Long mileage;

    private String colorCode;

    @ManyToMany
    @JoinTable(
            name = "cars_fuel_types",
            joinColumns = @JoinColumn(name = "car_id"),
            inverseJoinColumns = @JoinColumn(name = "fuel_type_id")
    )
    @EqualsAndHashCode.Exclude
    private Set<FuelType> fuelTypes = new HashSet<>();

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    private Set<CarPhoto> carPhotos = new HashSet<>();

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    public Car(Long id) {
        this.id = id;
    }
}
