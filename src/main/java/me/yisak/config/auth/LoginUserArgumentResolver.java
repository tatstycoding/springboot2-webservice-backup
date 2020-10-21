package me.yisak.config.auth;

import lombok.RequiredArgsConstructor;
import me.yisak.config.auth.dto.SessionUser;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
/*
이 어노테이션은 초기화 되지않은 final 필드나,
@NonNull 이 붙은 필드에 대해 생성자를 생성해 줍니다.
주로 의존성 주입(Dependency Injection) 편의성을 위해서 사용되곤 합니다.
*/
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    /*
     *  LoginUserArgumentResolver 클래스
     *  1. HandlerMethodArgumentResolver인터페이스를 구현한 클래스
     *  2. HandlerMethodArgumentResolver는 한가지 기능을 지원
     *  3. 그 기능 : 조건에 맞는 경우 메소드가 있다면 HandlerMethodArgumentResolver의
     *     구현체가 지정한 값으로 해당 메소드의 파라미터로 넘길수 있음.
     */

    private final HttpSession httpSession;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
    /*          supportsParameter()
    *  1. 컨트롤러 메서드의 특정 파라미터를 지원하는지 판단
    *  2. 여기선느 파라미터에 @LoginUser 어노테이션이 붙어 있고, 파라미터 클래스 타입이
    *  SessionUser.class 인 경우 true를 반환.
    * */


       boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null;
       boolean isUserClass = SessionUser.class.equals(parameter.getParameterType());

        return isLoginUserAnnotation && isUserClass;
    }

    @Override

    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    /*          resolveArgument()
     *  1. 파라미터에 전달할 객체를 생성
     *  2. 여기서는 세션에서 객체를 가져옴
     * */
        return httpSession.getAttribute("user");
    }






}
