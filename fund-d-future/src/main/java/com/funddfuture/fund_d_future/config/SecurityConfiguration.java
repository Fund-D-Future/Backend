package com.funddfuture.fund_d_future.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import static com.funddfuture.fund_d_future.user.Permission.*;
import static com.funddfuture.fund_d_future.user.Role.*;
import static org.springframework.aot.generate.ValueCodeGenerator.withDefaults;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private static final String[] WHITE_LIST_URL = {"/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/api/v1/users",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/api/v1/payments/**",
            "/api/v1/donations/**",
            "/api/v1/donations",
            "/api/v1/rewards/**",
            "/api/v1/rewards",
            "/api/v1/payments",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html",
            "/oauth2/google",
            "/api/v1/users/reset-password",
            "/api/v1/users/forgot-password",
            "/oauth2/google/callback",};
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL)
                                .permitAll()
                                .requestMatchers(GET, "/api/v1/funding/**").hasAnyRole(ADMIN.name(), FUNDER.name(), USER.name())
                                .requestMatchers(POST, "/api/v1/funding/**").hasAnyRole(ADMIN.name(), FUNDER.name(), USER.name())
                                .requestMatchers(PUT, "/api/v1/funding/**").hasAnyRole(ADMIN.name(), FUNDER.name(), USER.name())
                                .requestMatchers(PATCH, "/api/v1/funding/**").hasAnyRole(ADMIN.name(), FUNDER.name(), USER.name())
                                .requestMatchers(DELETE, "/api/v1/funding/**").hasAnyRole(ADMIN.name(), FUNDER.name(), USER.name())

                                .requestMatchers(GET, "/api/v1/funding/**").hasAnyAuthority(ADMIN_READ.name(), FUNDER_READ.name(), USER_READ.name())
                                .requestMatchers(POST, "/api/v1/funding/**").hasAnyAuthority(ADMIN_CREATE.name(), FUNDER_CREATE.name(), USER_CREATE.name())
                                .requestMatchers(PUT, "/api/v1/funding/**").hasAnyAuthority(ADMIN_UPDATE.name(), FUNDER_UPDATE.name(), USER_UPDATE.name())
                                .requestMatchers(DELETE, "/api/v1/funding/**").hasAnyAuthority(ADMIN_DELETE.name(), FUNDER_DELETE.name(), USER_DELETE.name())
                                .requestMatchers(PATCH, "/api/v1/funding/**").hasAnyAuthority(ADMIN_UPDATE.name(), FUNDER_UPDATE.name(), USER_UPDATE.name())


                                .requestMatchers(GET, "/api/v1/campaigns/**").hasAnyRole(USER.name(), FUNDER.name(), ADMIN.name())
                                .requestMatchers(POST, "/api/v1/campaigns/**").hasAnyRole(USER.name(), FUNDER.name(), ADMIN.name())
                                .requestMatchers(PUT, "/api/v1/campaigns/**").hasAnyRole(USER.name(), FUNDER.name(), ADMIN.name())
                                .requestMatchers(PATCH, "/api/v1/campaigns/**").hasAnyRole(USER.name(), FUNDER.name(), ADMIN.name())
                                .requestMatchers(DELETE, "/api/v1/campaigns/**").hasAnyRole(USER.name(), FUNDER.name(), ADMIN.name())

                                .requestMatchers(GET, "/api/v1/campaigns/**").hasAnyAuthority(ADMIN_READ.name(), FUNDER_READ.name(), USER_READ.name())
                                .requestMatchers(POST, "/api/v1/campaigns/**").hasAnyAuthority(ADMIN_CREATE.name(), FUNDER_CREATE.name(), USER_CREATE.name())
                                .requestMatchers(PUT, "/api/v1/campaigns/**").hasAnyAuthority(ADMIN_UPDATE.name(), FUNDER_UPDATE.name(), USER_UPDATE.name())
                                .requestMatchers(DELETE, "/api/v1/campaigns/**").hasAnyAuthority(ADMIN_DELETE.name(), FUNDER_DELETE.name(), USER_DELETE.name())
                                .requestMatchers(PATCH, "/api/v1/campaigns/**").hasAnyAuthority(ADMIN_UPDATE.name(), FUNDER_UPDATE.name(), USER_UPDATE.name())

                                .requestMatchers(GET, "/api/v1/users/**").hasAnyRole(ADMIN.name(), FUNDER.name(), USER.name())
                                .requestMatchers(DELETE, "/api/v1/users/**").hasAnyRole(ADMIN.name(), FUNDER.name(), USER.name())
                                .requestMatchers(PATCH, "/api/v1/users/**").hasAnyRole(ADMIN.name(), FUNDER.name(), USER.name())
                                .requestMatchers(POST, "/api/v1/users/**").hasAnyRole(ADMIN.name(), FUNDER.name(), USER.name())
                                .requestMatchers(PUT, "/api/v1/users/**").hasAnyRole(ADMIN.name(), FUNDER.name(), USER.name())

                                .requestMatchers(DELETE, "/api/v1/users/**").hasAnyAuthority(ADMIN_DELETE.name(), FUNDER_DELETE.name(), USER_DELETE.name())
                                .requestMatchers(GET, "/api/v1/users/**").hasAnyAuthority(ADMIN_READ.name(), FUNDER_READ.name(), USER_READ.name())
                                .requestMatchers(POST, "/api/v1/users/**").hasAnyAuthority(ADMIN_CREATE.name(), FUNDER_CREATE.name(), USER_CREATE.name())
                                .requestMatchers(PUT, "/api/v1/users/**").hasAnyAuthority(ADMIN_UPDATE.name(), FUNDER_UPDATE.name(), USER_UPDATE.name())
                                .requestMatchers(PATCH, "/api/v1/users/**").hasAnyAuthority(ADMIN_UPDATE.name(), FUNDER_UPDATE.name(), USER_UPDATE.name())

//                                .anyRequest()
//                                .authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/oauth2/google/callback")
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout ->
                        logout.logoutUrl("/api/v1/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                )
        ;

        return http.build();
    }
}
