package com.ucas.paper.service;

import com.ucas.paper.dao.UserRespository;
import com.ucas.paper.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRespository userRespository;

    @Transactional
    @Override
    public User checkUser(String username, String password) {
        User user = userRespository.findByUsernameAndPassword(username, password);
        return user;
    }
}
