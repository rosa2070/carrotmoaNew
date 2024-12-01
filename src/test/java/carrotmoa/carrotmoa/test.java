package carrotmoa.carrotmoa;

import carrotmoa.carrotmoa.enums.AuthorityCode;
import carrotmoa.carrotmoa.enums.State;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class test {
    public static void main(String[] args) {
        System.out.println(State.NORMAL);
        System.out.println(State.NORMAL.name());
        int a = 1;

        System.out.println(State.getStateName(1));

    }
}
