package carrotmoa.carrotmoa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chat_room", schema = "carrot_moa")
public class ChatRoom extends BaseEntity {
    @Size(max = 255)
    @Column(name = "room_name")
    private String roomName;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "state", nullable = false)
    private int state;

}