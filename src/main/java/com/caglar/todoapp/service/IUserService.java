package com.caglar.todoapp.service;

import com.caglar.todoapp.entity.User;

public interface IUserService {

    User saveUser(User user);

    User findUserByEmail(String email);

    User getCurrentUser();
}
