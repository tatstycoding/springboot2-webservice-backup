package me.yisak.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
// User 의 CRUD를 책임질 UserRepository 생성.


    Optional<User> findByEmail(String email); // 소셜 로그인으로 반환되는 값중 email을 통해 이미 생성된 사용자인지
                                             //처음 가입하는 사용자인지 판단하기 위한 메소드.

    /*
    Optional<T> 클래스는 Integer나 Double 클래스처럼 'T'타입의 객체를 포장해 주는 래퍼 클래스(Wrapper class)입니다.
    따라서 Optional 인스턴스는 모든 타입의 참조 변수를 저장할 수 있습니다.
     */


}
