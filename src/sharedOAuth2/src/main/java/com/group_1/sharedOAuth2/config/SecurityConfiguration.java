package com.group_1.sharedOAuth2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * com.group_1.authenticationService.config
 * Created by NhatLinh - 19127652
 * Date 3/27/2023 - 9:24 PM
 * Description: ...
 */
@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .and()
                .authorizeRequests()
                .antMatchers("/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer().jwt();
        return http.build();
    }
}
