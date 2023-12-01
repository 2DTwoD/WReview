package org.study.wreview.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.
                authorizeHttpRequests(request -> request
                        .requestMatchers("/persons", "persons/*/block").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/persons/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/persons/*").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PATCH, "/persons/*").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "persons/*").permitAll()
                        .requestMatchers( "/persons/**", "/reviews/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/", "/login", "/logout", "/registration",
                                        "/css/*", "/js/*", "/img/*", "/favicon.ico").permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/process_login")
                        .defaultSuccessUrl("/", true)
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"));
        return http.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(14);
    }
}
