package com.take2.take2.Security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter{

    @Autowired
    private TokenUtil tokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
       
                if (request.getServletPath().equals("/api/login") || request.getServletPath().equals("/api/tokens/refresh")){
                    filterChain.doFilter(request, response);
                }else {
                    String authHeader = request.getHeader(AUTHORIZATION);
                    if (authHeader != null && authHeader.startsWith("Bearer ")){
                        try {
        
                            DecodedJWT decodedToken = tokenUtil.getDecodedToken(authHeader);
        
                            String userName = decodedToken.getSubject();
                            String[] roles = decodedToken.getClaim("roles").asArray(String.class);

                            List<SimpleGrantedAuthority> auth112=new ArrayList<>();

                            for(String valuess:roles){
                                auth112.add(new SimpleGrantedAuthority(valuess));
                            }
                            
                           // Collection<SimpleGrantedAuthority> authorities = stream(roles).map(SimpleGrantedAuthority::new).toList();
        
                            UsernamePasswordAuthenticationToken authenticationToken =
                                    new UsernamePasswordAuthenticationToken(userName, null,  auth112);
                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                            filterChain.doFilter(request, response);
                        } catch (Exception e) {
        
                            response.setStatus(FORBIDDEN.value());
                            response.setContentType(APPLICATION_JSON_VALUE);
                            new ObjectMapper().writeValue(response.getOutputStream(), Map.of("TokenNotValid", e.getMessage()));
                        }
                    }else filterChain.doFilter(request, response);
                }
        
    }
    
}
