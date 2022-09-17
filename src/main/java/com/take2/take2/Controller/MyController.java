package com.take2.take2.Controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.userdetails.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.take2.take2.Model.Person;
import com.take2.take2.Security.TokenUtil;
import com.take2.take2.Service.PersonServiceImpl;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@RestController
public class MyController {

    @Autowired
    private PersonServiceImpl personService;

    @Autowired
    private TokenUtil tokenUtil;

    @GetMapping("/tokens/refresh")
    public void refreshTokens(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authHeader = request.getHeader(AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")){
            try {
                String requestUrl = request.getRequestURL().toString();
                Map<String, String> newTokens = tokenUtil.renewTokens(authHeader, requestUrl);
                if (newTokens != null){
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), newTokens);
                } else throw new Exception("Provided Token could not be verified");

            } catch (Exception e) {
                response.setStatus(FORBIDDEN.value());
                Map<String, String> body = new HashMap<>();
                body.put("TokenException", e.getMessage());
                body.put("error", "token refresh failed");
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), body);
            }
        }else throw new RuntimeException("Refresh token error");
    }

    @PostMapping("/token")
    public Map<String,String> gettoken(@RequestHeader String username){
        User user = (User) personService.loadUserByUsername(username);
        return tokenUtil.generateTokens("shubham", user);
    }

    

    @PostMapping("/users/save")
    public ResponseEntity<Person> saveUser(@RequestBody Person person){
        return ResponseEntity.ok().body(personService.savePerson(person));
    }



    @GetMapping("/users")
    public ResponseEntity<List<Person>> findAll(){
        return ResponseEntity.ok().body(personService.findAll());
    }

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
    
}
