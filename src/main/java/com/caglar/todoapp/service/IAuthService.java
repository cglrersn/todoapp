package com.caglar.todoapp.service;

import com.caglar.todoapp.request.LoginUserRequest;
import com.caglar.todoapp.request.SignUpUserRequest;
import com.caglar.todoapp.entity.User;

public interface IAuthService {

    User signUp(SignUpUserRequest signUpUserRequest);

    User authenticate(LoginUserRequest loginUserRequest);
}
