package carrotmoa.carrotmoa.model.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class AccommodationReviewResponse {
    private String comment;
    private LocalDate checkOutDate;
    private String nickname;
}
