package com.ucas.paper.dao;

import com.ucas.paper.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRespository extends JpaRepository<User, Long> {
    User findByUsernameAndPassword(String username, String password);
}
