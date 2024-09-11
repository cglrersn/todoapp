package com.caglar.todoapp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.caglar.todoapp.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        testEntityManager.persist(new User().setEmail("user1@email.com").setPassword("pass1")
                .setFirstName("first").setLastName("last"));
    }

    @Test
    public void testFindByEmail_passWithFound() {
        String email = "user1@email.com";

        User user = userRepository.findByEmail(email).get();

        assertEquals(email, user.getEmail());
        assertEquals("pass1", user.getPassword());
        assertEquals("first", user.getFirstName());
        assertEquals("last", user.getLastName());
    }

    @Test
    public void testFindByEmail_passWithNotFound() {
        assertFalse(userRepository.findByEmail("notfound@email.com").isPresent());
    }

}