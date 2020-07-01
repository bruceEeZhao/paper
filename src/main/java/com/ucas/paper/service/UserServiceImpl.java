package com.ucas.paper.service;

import com.ucas.paper.dao.UserRespository;
import com.ucas.paper.entities.User;
import com.ucas.paper.handler.PasswordHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRespository userRespository;

    @Transactional
    @Override
    public User checkUser(String username, String password) {
        String md5;
        try {
            md5 = PasswordHandler.md5(password);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        User user = userRespository.findByUsernameAndPassword(username, md5);
        return user;
    }

    @Transactional
    @Override
    public Boolean upDateUser(String name, String opwd, String password) {
        String md5;
        String old_md5;
        try {
            md5 = PasswordHandler.md5(password);
            old_md5 = PasswordHandler.md5(opwd);
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
        User user = userRespository.findByUsernameAndPassword(name, old_md5);
        user.setPassword(md5);
        if(userRespository.save(user) != null) {
            return true;
        } else {
            return false;
        }
    }
}
