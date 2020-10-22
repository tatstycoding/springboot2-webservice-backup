package me.yisak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


/*@EnableJpaAuditing
* 1.JPA Auditing 활성화
* 2.사용하기 위해선 최소 하나의 @Entity 클래스가 필요
* 3.@SpringBootApplication과 함께 있다보니 @WebMvcTest 에서도 스캔하게 됨.
*/
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
