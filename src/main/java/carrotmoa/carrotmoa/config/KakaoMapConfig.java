package carrotmoa.carrotmoa.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KakaoMapConfig {

    @Value("${kakao.maps.sdk.url}")
    private String sdkUrl;

    public String getSdkUrl() {
        return sdkUrl;
    }
}
