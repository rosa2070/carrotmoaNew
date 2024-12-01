package carrotmoa.carrotmoa.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccommodationSpaceRequest implements RequestDTO {
    private long accommodationId;
    private long spaceId;
    private int count;

}
