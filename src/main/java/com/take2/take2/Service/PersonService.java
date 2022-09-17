package com.take2.take2.Service;

import java.util.List;

import com.take2.take2.Model.Person;
import com.take2.take2.Model.Role;

public interface PersonService {
    Person savePerson(Person person);
    Person getPerson(String userName);
    List<Person> findAll();
    List<Role> addRoleToPerson(String userName, Role role);
    List<Role> getPersonRoles(String userName);
}
