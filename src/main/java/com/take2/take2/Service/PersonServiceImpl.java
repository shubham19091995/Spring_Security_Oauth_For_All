package com.take2.take2.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.take2.take2.Model.Person;
import com.take2.take2.Model.Role;
import com.take2.take2.Repo.PersonRepo;

@Component
public class PersonServiceImpl implements PersonService,UserDetailsService {

    @Autowired
    private PersonRepo personRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = personRepo.findByUserName(username);
        if (person == null){
            throw new UsernameNotFoundException("User not Found");
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        for(Role rolesss:person.getRoles()){
            authorities.add(new SimpleGrantedAuthority(rolesss.getRoleName()));
        }
        return new User(person.getUserName(), person.getPassword(), authorities);
    }

    @Override
    public Person savePerson(Person person) {

        person.setPassword(passwordEncoder.encode(person.getPassword()));
        return personRepo.save(person);
    }

    @Override
    public Person getPerson(String userName) {
        return personRepo.findByUserName(userName);
    }

    @Override
    public List<Person> findAll() {
        return personRepo.findAll();
    }

    @Override
    public List<Role> addRoleToPerson(String userName, Role role) {
        Person byUserName = personRepo.findByUserName(userName);
        byUserName.getRoles().add(role);
        return byUserName.getRoles();
    }

    @Override
    public List<Role> getPersonRoles(String userName) {
        Person byUserName = personRepo.findByUserName(userName);
        return byUserName.getRoles();
    }


    @Bean
    public PasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder(); }
    
}
