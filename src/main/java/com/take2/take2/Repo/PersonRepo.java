package com.take2.take2.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.take2.take2.Model.Person;

@Repository
@Component
public interface PersonRepo extends JpaRepository<Person,Integer> {
    Person findByUserName(String userName);

}
