package me.yisak.config;

import lombok.RequiredArgsConstructor;
import me.yisak.config.auth.LoginUserArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {
// LoginUserArgumentResolver가 스프링에서 인식 될 수 있도록 WebMvcConfigurer에 추가.

    private final LoginUserArgumentResolver loginUserArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
            argumentResolvers.add(loginUserArgumentResolver);
    }



}
