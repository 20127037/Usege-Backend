package config;

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
//@Configuration
//public class SecurityConfiguration {
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf()
//                .and()
//                .authorizeRequests(auth -> auth
////                        .antMatchers("/")
////                        .permitAll()
//                        .anyRequest()
//                        .authenticated())
//                .oauth2Login();
//        return http.build();
//    }
//}
