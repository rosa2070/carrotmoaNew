package carrotmoa.carrotmoa.model.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FullCalendarResponse {
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}
