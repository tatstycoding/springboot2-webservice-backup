package me.yisak.domain.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.yisak.domain.BaseTimeEntity.BaseTimeEntity;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;

@Getter // 롬복 어노테이션 // 클래스 내 모든 필드의 Getter 메소드 자동생성
@NoArgsConstructor // 기본생성자 추가. //롬복 어노테이션
@Entity// 테이블과 링크될 클래스임을 나타냄 // 카멜케이스 이름을 언더스코어 네이밍으로 테이블이름을 매칭
        //@Entity 는 JPA의 어노테이션
public class Posts extends BaseTimeEntity {
    /*
    * 본 클래스는 Entity클래스임
    */
    // 고로 Request/Response 클래스로 사용해서는 안됨. 왜냐하면 데이터베이스와 맞닿은 핵심 클래스여서.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //PK생성규칙 나타냄 // GenerationType.IDENTITY = auto_increment 가 됨(스프링부트2.0에서)
    private Long id;

    @Column(length =500, nullable = false)  //해당 클래스 필드는 모두 칼럼이됨
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String author;

    @Builder // 해당 클래스의 빌더 패턴 클래스를 생성 // 생성자 상단에 선언 시 생성자에 포함된 필드만 빌더에 포함.
            // 보다 싶이 id 필드는 빠져있다.
    public Posts(String title, String content, String author){
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }

}
