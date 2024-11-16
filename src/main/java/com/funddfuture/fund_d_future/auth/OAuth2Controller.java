package com.funddfuture.fund_d_future.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
public class OAuth2Controller {

    private final CustomOAuth2UserService customOAuth2UserService;

    @GetMapping("/google")
    public void googleAuth(HttpServletRequest request, HttpServletResponse response) {
        // This endpoint will be used to initiate the Google OAuth2 login
    }
    @GetMapping("/google/callback")
    public AuthenticationResponse googleAuthRedirect(@AuthenticationPrincipal OAuth2User user) {
        return (AuthenticationResponse) customOAuth2UserService.loadUser(user);

    }

}