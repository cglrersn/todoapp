package com.caglar.todoapp.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import com.caglar.todoapp.entity.User;
import com.caglar.todoapp.request.LoginUserRequest;
import com.caglar.todoapp.request.SignUpUserRequest;
import com.caglar.todoapp.service.IUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private IUserService userService;

    @Test
    public void testSignUp() {
        authService.signUp(new SignUpUserRequest().setEmail("signup@email.com").setPassword("pass").setLastName("last")
                .setFirstName("first"));

        verify(userService).saveUser(any(User.class));
    }

    @Test
    public void testAuthenticate() {
        String email = "user@email.com";
        String password = "pass";
        ArgumentCaptor<UsernamePasswordAuthenticationToken> argumentCaptor = ArgumentCaptor.forClass(
                UsernamePasswordAuthenticationToken.class);

        authService.authenticate(new LoginUserRequest().setEmail(email).setPassword(password));

        verify(authenticationManager).authenticate(argumentCaptor.capture());
        assertEquals(email, argumentCaptor.getValue().getPrincipal());
        assertEquals(password, argumentCaptor.getValue().getCredentials());
    }
}