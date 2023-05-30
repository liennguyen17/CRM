package com.example.democrm.security;

import com.example.democrm.config.CorsConfigFilter;
import com.example.democrm.service.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    private final UserDetailsService myUserDetailsService;
    private final AuthEntryPointJwt unauthorizedHandler;
    private final JwtTokenProvider jwtTokenProvider;

    private final CorsConfigFilter corsConfigFilter;

    @Autowired
    public WebSecurityConfig(UserDetailsService myUserDetailsService, AuthEntryPointJwt unauthorizedHandler, JwtTokenProvider jwtTokenProvider, CorsConfigFilter corsConfigFilter) {
        this.myUserDetailsService = myUserDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
        this.jwtTokenProvider = jwtTokenProvider;
        this.corsConfigFilter = corsConfigFilter;
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter(jwtTokenProvider);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(myUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/test/**").authenticated()
                .requestMatchers("/customer/**").permitAll()
                .requestMatchers("/user/**").permitAll()
//                .requestMatchers("/user/manager-group").authenticated()
//                .anyRequest().authenticated();

//                .antMatchers("/auth/**").permitAll()
//                .antMatchers("/test/**").authenticated()
                .anyRequest().permitAll();  // tạm thời ko xét quyền để call thử, sau khi xong xét lại quyền như cũ  .anyRequest().authenticated();


        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(corsConfigFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
