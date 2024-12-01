package carrotmoa.carrotmoa.model.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccommodationImageListResponse {
    private Long accommodationId;
    private List<String> imageUrl;
}
