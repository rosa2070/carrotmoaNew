package carrotmoa.carrotmoa.model.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class AccommodationAvailableResponse {
    private Long accommodationId;
    private String title;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String lotAddress;
    private String imageUrl;
    private Integer room;
    private Integer bath;
    private Integer living;
    private Integer kitchen;


    public static AccommodationAvailableResponse fromData(Object[] data) {

        return AccommodationAvailableResponse.builder()
                .accommodationId((Long) data[0])
                .title((String) data[1])
                .latitude((BigDecimal) data[2])
                .longitude((BigDecimal) data[3])
                .lotAddress((String) data[4])
                .imageUrl((String) data[5])
                .room((Integer) data[6])
                .bath((Integer) data[7])
                .living((Integer) data[8])
                .kitchen((Integer) data[9])
                .build();
    }
}
