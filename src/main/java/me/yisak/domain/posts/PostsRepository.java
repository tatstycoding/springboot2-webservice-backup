package me.yisak.domain.posts;

import me.yisak.domain.posts.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

//MyBatis등에서 Dao라고 부리는 DB Layer 접근자.
//JPA에서는 Repository 락 부르며 인터페이스로 생성.
//Entity 클래스는(Posts 클래스) Entity Repository는 함께 위치해야함. 그래서 domain.posts 패키지 안에 같이 존재.
//Entity 클래스는(Posts 클래스) 기본 Repository(PostsRepository 인터페이스) 없니는 제대로 역활 할수가 없다.
public interface PostsRepository extends JpaRepository<Posts, Long> { // Entity클래스 : <Posts> , PK타입 : <Long>

    @Query("SELECT p FROM Posts p ORDER BY p.id DESC") // ORDER BY ~~ DESC : 내림차순으로 정렬 // ASC 올림차순으로 정렬
    List<Posts> findAllDesc();
}
