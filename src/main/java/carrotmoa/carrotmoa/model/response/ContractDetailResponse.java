package carrotmoa.carrotmoa.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@Builder
public class ContractDetailResponse {
    private String title;
    private String lotAddress;
    private String detailAddress;
    private int floor;
    private String nickname;

    public static ContractDetailResponse fromData(Object[] data) {
        return ContractDetailResponse.builder()
            .title((String) data[0])
            .lotAddress((String) data[1])
            .detailAddress((String) data[2])
            .floor((int) data[3])
            .nickname((String) data[4])
            .build();
    }
}
