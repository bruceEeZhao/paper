package com.ucas.paper.service;


import com.ucas.paper.entities.User;

public interface UserService {

    User checkUser(String username, String password);
}
