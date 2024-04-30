package com.fisa.woorionebank.config;

import com.fisa.woorionebank.security.TokenProvider;
import com.fisa.woorionebank.security.filter.JwtAuthenticationFilter;
import com.fisa.woorionebank.security.filter.JwtAuthorizationFilter;
import com.fisa.woorionebank.security.service.JwtDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.Filter;

@EnableWebSecurity
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CorsConfig corsConfig;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private JwtDetailsService jwtDetailsService;

    private AuthenticationManager authenticationManager;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf()
                .disable()
                .httpBasic()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/swagger-ui/**","/swagger-resources/**","/auth/**", "/api/**", "/swagger-ui.html", "/v3/api-docs/**",
                        "/swagger/**", "/api-docs/**", "/api/member/signup", "/actuator/prometheus").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .addFilter(corsConfig.corsFilter())
                .addFilter(jwtAuthenticationFilter())
                .addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    private Filter jwtAuthorizationFilter() throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(tokenProvider, authenticationManager);
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager());
        return jwtAuthenticationFilter;
    }

    private Filter jwtAuthenticationFilter() throws Exception {
        JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(authenticationManager(), tokenProvider, jwtDetailsService);
        return jwtAuthorizationFilter;
    }
}