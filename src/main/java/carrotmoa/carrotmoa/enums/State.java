package carrotmoa.carrotmoa.enums;


import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public enum State {
    NORMAL(1),
    DELETE(2),

    ;
    private static final Map<Integer, State> statusMap = new HashMap<>();

    static {
        for (State state : State.values()) {
            statusMap.put(state.getStatus(), state);
        }
    }

    private final Integer status;


    State(Integer status) {
        this.status = status;
    }

    public static String getStateName(Integer status) {
        State state = statusMap.get(status);
        return state != null ? state.name() : null;
    }
}
