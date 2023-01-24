package com.take2.take2.Info;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


@Repository
@Component
public interface repo extends JpaRepository<customUser,Integer> {
}
