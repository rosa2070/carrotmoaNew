package carrotmoa.carrotmoa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "user_profile")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@DynamicUpdate
public class UserProfile extends BaseEntity {

    @Column(name = "user_id")
    private Long userId;

    @Size(max = 10)
    @NotNull
    @Column(name = "nickname")
    private String nickname;

    @Size(max = 255)
    @Column(name = "pic_url")
    private String picUrl;

    @Size(max = 100)
    @Column(name = "bio", length = 100)
    private String bio;

    @Column(name = "address_id")
    private Long addressId;

    @Size(max = 255)
    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "birthday")
    private LocalDate birthday;
}