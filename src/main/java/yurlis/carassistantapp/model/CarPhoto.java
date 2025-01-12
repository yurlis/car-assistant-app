package yurlis.carassistantapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "car_photos")
@SQLRestriction("is_deleted = FALSE")
@SQLDelete(sql = "UPDATE car_photos SET is_deleted = TRUE WHERE id = ?")
@NoArgsConstructor
@Getter
@Setter
public class CarPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    @EqualsAndHashCode.Exclude
    private Car car;

    @Column(name = "photo_url", nullable = false)
    private String photoUrl;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;
}
