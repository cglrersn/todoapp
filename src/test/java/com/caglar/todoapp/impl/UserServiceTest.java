package com.caglar.todoapp.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.caglar.todoapp.entity.User;
import com.caglar.todoapp.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    UserRepository userRepository;

    private final String userEmail = "user@email.com";
    private final User user = new User().setId(1L).setFirstName("first").setLastName("last").setPassword("pass")
            .setEmail(userEmail);

    @Test
    public void testSaveUser() {
        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.saveUser(user);

        verify(userRepository).save(argumentCaptor.capture());
        User userSaved = argumentCaptor.getValue();
        verifyUserInformation(userSaved);
    }

    @Test
    public void testFindUserByEmail_passWithFound() {
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));

        User userFound = userService.findUserByEmail(userEmail);

        verifyUserInformation(userFound);
    }

    @Test
    public void testFindUserByEmail_failWithNotFound() {
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> userService.findUserByEmail(userEmail));
    }

    private void verifyUserInformation(User userToVerify) {
        assertEquals(user.getId(), userToVerify.getId());
        assertEquals(user.getFirstName(), userToVerify.getFirstName());
        assertEquals(user.getLastName(), userToVerify.getLastName());
        assertEquals(user.getPassword(), userToVerify.getPassword());
        assertEquals(user.getEmail(), userToVerify.getEmail());
    }

}