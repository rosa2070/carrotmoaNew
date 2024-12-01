package carrotmoa.carrotmoa.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccommodationImageResponse {
    //    private List<String> imageUrls;
    private Long accommodationId;
    private String imageUrl;

    public static AccommodationImageResponse fromData(Object[] data) {
//        List<String> imageUrlsList = null;
//        for (int i = 0; i < data.length; i++) {
//            imageUrlsList = Arrays.asList(
//                    (String) data[i]
//            );
//        }
        return AccommodationImageResponse.builder()
            .accommodationId((Long) data[0])
            .imageUrl((String) data[1])
            .build();
    }
}
