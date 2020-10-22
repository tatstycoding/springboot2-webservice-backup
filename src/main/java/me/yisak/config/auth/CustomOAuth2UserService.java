package me.yisak.config.auth;

import lombok.RequiredArgsConstructor;
import me.yisak.domain.user.UserRepository;
import me.yisak.config.auth.dto.OAuthAttributes;
import me.yisak.config.auth.dto.SessionUser;
import me.yisak.domain.user.User;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;


@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
//구글 로그인 이후 가져온 사용자의 정보(email, name, picture등) 들을 기반으로 가입 및 정보수정, 세션 저장등의 기능을 지원

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // registrationId
        // 1. 현재 로그인 잰행중인 서비스를 구분하는 코드
        // 2.지금은 구글만 사용하는 불필요한 값이지만, 이후 네이버 로그인 연동 시에 네이버로그인인지, 구글 로그인인지 구분하기 위해 사용

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();
       /* userNameAttributeName
        * 1. OAuth2 로그인 진행 시 키가 되는 필드값을 이야기함. Primary Key와 같은 의미
        * 2. 구글의 경우 기본적으로 코드를 지원하지만, 네이버 카카오 등은 기본 지원하지 않음. 구글 기본코드는 "sub"
        *  3. 이후 네이버 로그인과 구글 로그인을 동시 지원할때 사용 됨.*/

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        /* OAuthAttributes
        *  1. OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담을 클래스
        *  2. 이후 네이버 등 다른 소셜 로그인도 이 클래스를 사용함.
        *  3. 바로 아래에서 이 클래스의 코드가 나오니 차례로 생성.
        */


        User user = saveOrUpdate(attributes);

        httpSession.setAttribute("user", new SessionUser(user));
        /* SessionUser
        *  1. 세션에 사용자 정보를 저장하기 위한 Dto 클래스
        *  2. 왜 User 클래스를 쓰지 않고 새로 만들어서 쓸까?
        *   -> SessionUser 클래스 참조.
        */

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}
