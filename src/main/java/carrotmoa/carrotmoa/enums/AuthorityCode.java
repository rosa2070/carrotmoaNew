package carrotmoa.carrotmoa.enums;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public enum AuthorityCode {
    SUPER_ADMIN(1L),
    ADMIN(2L),
    USER(3L),
    PRISON_WARDEN(4L),
    TEST(5L),
    HOST(6L);

    private static final Map<Long, AuthorityCode> map = new HashMap<>();

    static {
        for (AuthorityCode code : AuthorityCode.values()) {
            map.put(code.getId(), code);
        }
    }

    private final Long id;

    AuthorityCode(long id) {
        this.id = id;
    }

    //User의 권한 id를 받아서 권한 이름을 반환
    public static String getAuthorityCodeName(long id) {
        AuthorityCode code = map.get(id);
        return code != null ? code.name() : null;
    }
}

