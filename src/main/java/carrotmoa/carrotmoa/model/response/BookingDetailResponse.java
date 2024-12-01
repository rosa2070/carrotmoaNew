package carrotmoa.carrotmoa.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingDetailResponse {
    private Long id;
    private String lotAddress;
    private String detailAddress;
    private int floor;
    private String title;
    private String nickname;
}
