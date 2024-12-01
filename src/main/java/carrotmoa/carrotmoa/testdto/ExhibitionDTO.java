package carrotmoa.carrotmoa.testdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExhibitionDTO {
    private String name;
    private String location;
    private String theme;
    private double latitude;
    private double longitude;
}
