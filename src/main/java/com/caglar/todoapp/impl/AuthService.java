package com.caglar.todoapp.impl;

import com.caglar.todoapp.request.LoginUserRequest;
import com.caglar.todoapp.request.SignUpUserRequest;
import com.caglar.todoapp.entity.User;
import com.caglar.todoapp.service.IAuthService;
import com.caglar.todoapp.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthService implements IAuthService {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final IUserService userService;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager,
                       PasswordEncoder passwordEncoder,
                       IUserService userService) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Override
    public User signUp(SignUpUserRequest signUpUserRequest) {
        User user = new User().setFirstName(signUpUserRequest.getFirstName()).setLastName(
                        signUpUserRequest.getLastName())
                .setEmail(signUpUserRequest.getEmail()).setPassword(passwordEncoder.encode(
                        signUpUserRequest.getPassword()));
        User userSignedUp = userService.saveUser(user);
        log.debug("User signed up with userId={}", user.getId());
        return userSignedUp;
    }

    @Override
    public User authenticate(LoginUserRequest loginUserRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUserRequest.getEmail(),
                loginUserRequest.getPassword()));
        return userService.findUserByEmail(loginUserRequest.getEmail());
    }
}
