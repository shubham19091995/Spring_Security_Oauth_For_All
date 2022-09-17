package com.take2.take2.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.take2.take2.Model.Role;


@Repository
@Component
public interface RoleRepo extends JpaRepository<Role,Integer> {
    
}
