package carrotmoa.carrotmoa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "accommodation_like")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccommodationLike extends BaseEntity {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "accommodation_id")
    private Long accommodationId;

}
