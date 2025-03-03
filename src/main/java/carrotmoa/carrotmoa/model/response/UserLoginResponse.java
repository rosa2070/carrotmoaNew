package carrotmoa.carrotmoa.model.response;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserLoginResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String email;
    private String password;
    private String name;
    private Boolean isWithdrawal;
    private Long authorityId;
    private int state;


}
