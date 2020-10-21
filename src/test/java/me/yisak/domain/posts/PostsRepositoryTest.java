package me.yisak.domain.posts;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    // Junit에서 단위테스트가 끝날 때마다 수행되는 메소드를 지정
    // 보통 배포 전 전체 테스트를 수행할 때 테스트간 데이터 침범을 막기 위해 사용함.
    // 여러 테스트가 동시에 수행되면 테스트용 데이터 베이스인 H2에 데이터가 그대로 남아 있어 다음 테스트 실행 시 테스트가 실패 할 수 있음
   @After
   public void cleanup(){
        postsRepository.deleteAll();
    }

    @Test
    public void 게시글저장_불러오기(){
        //given
        String title="테스트 게시글";
        String content="테스트 본문";

        postsRepository.save(Posts.builder() //postsRepository.save() : 테이블 posts에 insert/update쿼리 실행 // id없으면 insert 있으면 update
                                .title(title)
                                .content(content)
                                .author("yisak@gmail.com")
                                .build()
                            );

        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);
   }


   @Test
    public void BaseTimeEntity_등록(){

       //given
       LocalDateTime now = LocalDateTime.of(2019, 6, 4, 0, 0, 0);
       postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
               .author("author")
               .build());

        //when
       List<Posts> postsList = postsRepository.findAll();

       //then
       Posts posts = postsList.get(0);


       System.out.println(">@>@>@>@>@>@>@>@>@>>>>>>> createDate="
               + posts.getCreatedDate()+", modifiedDate="+posts.getModifiedDate());

       assertThat(posts.getCreatedDate()).isAfter(now);
       assertThat(posts.getModifiedDate()).isAfter(now);





   }



}

