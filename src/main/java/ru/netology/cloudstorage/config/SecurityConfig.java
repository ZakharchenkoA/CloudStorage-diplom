package ru.netology.cloudstorage.config;

import ru.netology.cloudstorage.security.TokenAuthenticationEntryPoint;
import ru.netology.cloudstorage.security.TokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@EnableWebMvc
@RequiredArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {

    private final TokenFilter tokenFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .logout().disable()
                .cors()
                .and()
                .authorizeHttpRequests(urlConfig -> urlConfig
                        .antMatchers("/cloud/login").permitAll()
                        .antMatchers("/cloud/file").hasAuthority("ROLE_USER")
                        .anyRequest().authenticated()
                        .and()
                        .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)
                )
                .exceptionHandling(eh -> eh.authenticationEntryPoint(new TokenAuthenticationEntryPoint()))
                .sessionManagement(sm -> sm.sessionCreationPolicy(STATELESS))
                .build();
    }
}