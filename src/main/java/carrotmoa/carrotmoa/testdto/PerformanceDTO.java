package carrotmoa.carrotmoa.testdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PerformanceDTO {
    private String name;
    private String venue;
    private String date;
    private double latitude;
    private double longitude;
}
