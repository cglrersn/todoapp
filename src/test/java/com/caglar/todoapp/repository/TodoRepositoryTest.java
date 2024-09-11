package com.caglar.todoapp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.caglar.todoapp.entity.TodoItem;
import com.caglar.todoapp.entity.User;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class TodoRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private TodoRepository todoRepository;

    private Long userId1 = 0L;
    private Long userId2 = 1L;

    @BeforeEach
    public void setup() {
        User user1 = testEntityManager.persist(new User().setEmail("user1@email.com").setPassword("pass1")
                .setFirstName("first").setLastName("first"));
        User user2 = testEntityManager.persist(new User().setEmail("user2@email.com").setPassword("pass2")
                .setFirstName("second").setLastName("second"));
        userId1 = user1.getId();
        userId2 = user2.getId();
        testEntityManager.persist(new TodoItem().setFinished(false).setDescription("todo item 1")
                .setUser(user1));
        testEntityManager.persist(new TodoItem().setFinished(true).setDescription("todo item 2")
                .setUser(user2));
    }

    @Test
    public void testFindByDescriptionAndUserId_passWithFound() {
        String description = "todo item 1";

        TodoItem todoItem = todoRepository.findByDescriptionAndUserId(description, userId1).get();

        assertEquals(description, todoItem.getDescription());
        assertFalse(todoItem.isFinished());
        assertEquals(userId1, todoItem.getUser().getId());
    }

    @Test
    public void testFindByDescriptionAndUserId_passWithNotFound() {
        String description = "todo item 899";

        assertFalse(todoRepository.findByDescriptionAndUserId(description, userId1).isPresent());
    }

    @Test
    public void testFindByUserId_passWithFound() {
        List<TodoItem> todoItems = todoRepository.findByUserId(userId2);

        assertEquals(1, todoItems.size());
        TodoItem todoItem = todoItems.get(0);
        assertEquals("todo item 2", todoItem.getDescription());
        assertTrue(todoItem.isFinished());
        assertEquals(userId2, todoItem.getUser().getId());
    }

    @Test
    public void testFindByUserId_passWithNotFound() {
        assertEquals(0, todoRepository.findByUserId(0L).size());
    }

}