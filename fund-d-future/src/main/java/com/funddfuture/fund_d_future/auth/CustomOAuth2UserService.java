package com.funddfuture.fund_d_future.auth;

import com.funddfuture.fund_d_future.config.JwtService;
import com.funddfuture.fund_d_future.token.Token;
import com.funddfuture.fund_d_future.token.TokenRepository;
import com.funddfuture.fund_d_future.token.TokenType;
import com.funddfuture.fund_d_future.user.Role;
import com.funddfuture.fund_d_future.user.User;
import com.funddfuture.fund_d_future.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;

    public OAuth2User loadUser(OAuth2User userRequest) {
//        OAuth2User oAuth2User = super.loadUser(userRequest);
//        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = userRequest.getAttributes().get("email").toString();

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setFirstname(userRequest.getAttributes().get("givenName").toString());
                    newUser.setLastname(userRequest.getAttributes().get("familyName").toString());
                    newUser.setRole(Role.USER);
                    var savedUser = userRepository.save(newUser);
                    var jwtToken = jwtService.generateToken(savedUser);
                    saveUserToken(savedUser, jwtToken);
                    return savedUser;

                });
        String jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(user, jwtToken);
        return (OAuth2User) user;
//        return (OAuth2User) AuthenticationResponse.builder()
//                .accessToken(jwtToken)
//                .refreshToken(refreshToken)
//                .build();
//        return new DefaultOAuth2User(
//                Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())),
//                attributes,
//                "email"


    }
    public void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }
}