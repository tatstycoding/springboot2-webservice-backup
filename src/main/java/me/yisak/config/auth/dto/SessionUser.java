package me.yisak.config.auth.dto;

import lombok.Getter;
import me.yisak.domain.user.User;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
//SessionUser에는 인증된 사용자 정보만 필요. 그외 정보는 필요없으니 name, email, picture만 필드로 선언.

    private String name;
    private String email;
    private String picture;

    public SessionUser(User user){
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
    /*
    *  User클래스 대신 SessionUser를 만들어 쓰는 이유.
    *
    *  1. User클래스를 그대로 사용하면 다음과 같은 에러 발생
    *  Failed to convert from type [java.lang.Object] to type [byte[]] for value 'me.yisak.domain.user.User@4123'
    *
    *  이는 세션에 저장하기 위해 User클래스를 세션에 저장하려고 하니, User클래스에 직렬화를 구현하지 않았다는 의미의 에러.
    *
    *  2. 그렇다고 User클래스에 직렬화 코드를 넣으면 많은 자식 엔티티를 가지고 있을경우 그 자식 엔티티까지 직렬화 대상에 포함되서
    *       성능 이슈, 부수 효과가 발생할 확률 매우높음
    *
    *  3. 그래서 직렬화 기능을 가진 세션 Dto를 하나 추가로 만드는 것이 유지보수에 많은 도움됨.
    * */










}
