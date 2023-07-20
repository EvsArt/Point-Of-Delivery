package com.pointofdelivery.authorizationserver.config;

import com.pointofdelivery.authorizationserver.config.jwt.AuthEntryPointJwt;
import com.pointofdelivery.authorizationserver.config.jwt.AuthJwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    private final AuthEntryPointJwt authEntryPointJwt;
    private final AuthJwtTokenFilter authJwtTokenFilter;

    public WebSecurityConfig(AuthEntryPointJwt authEntryPointJwt,
                             AuthJwtTokenFilter authJwtTokenFilter){
        this.authEntryPointJwt = authEntryPointJwt;
        this.authJwtTokenFilter = authJwtTokenFilter;
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
            throws Exception {
        http
//                .headers((headers)->headers
//                        .httpStrictTransportSecurity((hstsConfig)->hstsConfig.
//                                maxAgeInSeconds(0)
//                                .includeSubDomains(true))
//                )
                .headers((header)->header.httpStrictTransportSecurity((hsts) -> hsts.disable()))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .exceptionHandling((handlingConfigurer) -> handlingConfigurer.authenticationEntryPoint(authEntryPointJwt))
                .sessionManagement((sesManagementConfig) -> sesManagementConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/test/**").permitAll()
                        .anyRequest().authenticated()
                );

        http.addFilterBefore(authJwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
