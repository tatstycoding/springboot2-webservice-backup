package me.yisak.web;


import com.fasterxml.jackson.databind.ObjectMapper;
import me.yisak.domain.posts.Posts;
import me.yisak.domain.posts.PostsRepository;
import me.yisak.web.dto.PostsSaveRequestDto;
import me.yisak.web.dto.PostsUpdateRequestDto;


import org.junit.Before;
import org.springframework.http.MediaType;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class) // 테스트를 진행할때 JUnit에 내장된 실행자 외에 다른 실행자를 실행시킴
                            //여기서는 SpringRunner라는 스프링 실행자 사용 // 즉 스프링부트테스트와 JUnit사이에 연결자 역활
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    //매번 테스트가 시작되기 전에 MockMvc 인스턴스를 생성.
    public void setup(){
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @After
    public void tearDown() throws Exception{
        postsRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles="USER")
    /*     @WithMockUser(roles="USER")
    *  1. 인증된 모의(가짜) 사용자를 만들어서 사용
    *  2. roles에 권한을 추가할 수 있음.
    *  3. 즉, 이 어노테이션으로 인해 ROLE_USER권한을 가진 사용자가 API를 요청하는 것과 동일한 효과
    *  4. 하지만 @WithMockUser가 MockMvc에서만 작동함.
    */
    public void Posts_등록된다() throws Exception{

        //given
        String title = "title";
        String content = "content";
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                                            .title(title)
                                            .content(content)
                                            .author("author")
                                            .build();

        String url = "http://localhost:" + port + "/api/v1/posts";

        //when
        //ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);
        mvc.perform(post(url)
        /*     mvc.perform(post(url)
         *  1. 생성된 MockMvc를 통해 API를 테스트함
         *  2. 본문(Body) 영역은 문자열로 표현하기 위해 ObjectMapper를 통해 문자열 JSON으로 변환
         */
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
        // assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        // assertThat(responseEntity.getBody()).isGreaterThan(0L);
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }

    @Test
    @WithMockUser(roles="USER")
    public void Posts_수정된다() throws Exception {

        //given
        Posts savedPosts = postsRepository.save(Posts.builder()
                                                        .title("title")
                                                        .content("content")
                                                        .author("author")
                                                        .build()
                                                );

        Long updateId = savedPosts.getId();
        String expectedTitle = "title2";
        String expectedContent = "content2";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                                                                    .title(expectedTitle)
                                                                    .content(expectedContent)
                                                                    .build();

        String url = "http://localhost:" +port + "/api/v1/posts/" + updateId;

        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        //when
        //ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);
        mvc.perform(put(url)
                /*     mvc.perform(post(url)
                 *  1. 생성된 MockMvc를 통해 API를 테스트함
                 *  2. 본문(Body) 영역은 문자열로 표현하기 위해 ObjectMapper를 통해 문자열 JSON으로 변환
                 */
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
        //assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        //assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
    }
}