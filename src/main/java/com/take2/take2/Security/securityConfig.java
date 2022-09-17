package com.take2.take2.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.take2.take2.Service.PersonServiceImpl;
import com.auth0.jwt.JWT;
import lombok.RequiredArgsConstructor;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
@Configuration @EnableWebSecurity @RequiredArgsConstructor
public class securityConfig {

    @Autowired
    private PersonServiceImpl personServiceImpl;

    @Autowired
    private PasswordEncoder oPasswordEncoder;


    @Autowired
    private TokenUtil tokenUtil;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authManager(), tokenUtil);
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeRequests( auth -> {
                    auth.antMatchers("/users/save").permitAll();
                    auth.antMatchers("/token").permitAll();
                    auth.antMatchers("/hello").hasAuthority("User");
                    auth.antMatchers("/api/tokens/refresh").permitAll();
                    auth.anyRequest().authenticated();
                })
                .addFilter(customAuthenticationFilter)
                .addFilterBefore(new CustomAuthorizationFilter(tokenUtil), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authManager(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(personServiceImpl);
        daoAuthenticationProvider.setPasswordEncoder(oPasswordEncoder);
        return new ProviderManager(daoAuthenticationProvider);
    }
    @Bean
    public Algorithm algorithm(){ return Algorithm.HMAC256("secret".getBytes());}


    @Bean
    public JWTVerifier verifier(){
        return JWT.require(algorithm()).build();
    }
    
}
