package hello.servlet.domain.member;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Member {

    private long id;
    private String username;
    private int age;

    public Member(String useranme, int age){
        this.username = useranme;
        this.age = age;
    }

}
