package me.yisak.web;


import me.yisak.config.auth.SecurityConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class) // 테스트를 진행할때 JUnit에 내장된 실행자 외에 다른 실행자를 실행시킴
//여기서는 SpringRunner라는 스프링 실행자 사용 // 즉 스프링부트테스트와 JUnit사이에 연결자 역활
@WebMvcTest(controllers = HelloController.class,
            excludeFilters = {
            @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
                             } //SecurityConfig제거 과정.
           )
//                  @WebMvcTest
/* 1. @WebMvcTest는 CustomOAuth2UserService를 스캔하지 않는다.
// 2. @SpringBootTest는 스캔함.
// 3. @WebMvcTest는 WebSecirutyConfigurerAdapter, WebMvcConfigurer를 비룻한
// 4. @ControllerAdvice, @Controller를 읽는다.
// 5. 즉 @Repository, @Service, @Component는 스캔 대상이 아니다.
// 6. 고로 SecurityConfig는 읽었지만, SecurityConfig를 생성하기 위한 CustomOAuth2UserService는 읽을수 없어 테스트 에러남.
// 7. 고로 스캔 대상에서 SecurityConfig를 제거해야함.
// 8. @WebMvcTest는 일반적인 @Configuration은 스캔하지 않는다.
*/

public class HelloControllerTest {

    @Autowired
    private MockMvc mvc;


    @WithMockUser(roles = "USER")
    @Test
    public void hello가_리턴된다() throws Exception{
        String hello = "hello";

        mvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string(hello));
    }

    @WithMockUser(roles = "USER")
    @Test
    public void helloDto가_리턴된다() throws Exception{
        String name = "hello";
        int amount = 1000;

        mvc.perform(
                    get("/hello/dto")
                        .param("name", name)
                        .param("amount", String.valueOf(amount)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is(name)))
                    .andExpect(jsonPath("$.amount", is(amount)));
    }
}
