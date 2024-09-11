package com.caglar.todoapp.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.caglar.todoapp.entity.TodoItem;
import com.caglar.todoapp.entity.User;
import com.caglar.todoapp.exception.model.TodoItemBadRequestException;
import com.caglar.todoapp.exception.model.TodoItemNotFoundException;
import com.caglar.todoapp.repository.TodoRepository;
import com.caglar.todoapp.request.TodoItemRequest;
import com.caglar.todoapp.service.IUserService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @InjectMocks
    private TodoService todoService;

    @Mock
    private IUserService userService;

    @Mock
    private TodoRepository todoRepository;

    private User currentUser;

    private TodoItem existingTodoItem;

    @BeforeEach
    public void setup() {
        currentUser = new User().setId(1L).setPassword("pass").setEmail("email")
                .setFirstName("first").setLastName("last");
        existingTodoItem = new TodoItem().setDescription("todo item").setUser(currentUser)
                .setFinished(false).setId(1L);
        when(userService.getCurrentUser()).thenReturn(currentUser);
    }

    @Test
    public void testGetTodoItem_passWithFound() {
        when(todoRepository.findById(1L)).thenReturn(Optional.of(existingTodoItem));

        TodoItem todoItem = todoService.getTodoItem(1L);

        assertEquals("todo item", todoItem.getDescription());
        assertFalse(todoItem.isFinished());
    }

    @Test
    public void testGetTodoItem_failWithNotFound() {
        when(todoRepository.findById(1L)).thenReturn(Optional.empty());

        TodoItemNotFoundException ex = assertThrows(TodoItemNotFoundException.class,
                () -> todoService.getTodoItem(1L));

        assertEquals("Todo item does not exist", ex.getMessage());
    }

    @Test
    public void testGetTodoItem_failWithAnotherUsersTodoItem() {
        when(userService.getCurrentUser()).thenReturn(new User().setId(2L).setPassword("pass2").setEmail("email2")
                .setFirstName("first2").setLastName("last2"));
        when(todoRepository.findById(1L)).thenReturn(Optional.of(existingTodoItem));

        TodoItemNotFoundException ex = assertThrows(TodoItemNotFoundException.class,
                () -> todoService.getTodoItem(1L));
        assertEquals("Todo item does not exist", ex.getMessage());
    }

    @Test
    public void testGetTodoItemListForUser() {
        todoService.getTodoItemListForUser();

        verify(todoRepository).findByUserId(currentUser.getId());
    }

    @Test
    public void testAddTodoItem_passWithAddingTheItem() {
        TodoItemRequest todoItemRequest = new TodoItemRequest().setDescription("todo item").setIsFinished(false);
        when(todoRepository.findByDescriptionAndUserId(todoItemRequest.getDescription(), currentUser.getId()))
                .thenReturn(Optional.empty());
        ArgumentCaptor<TodoItem> argumentCaptor = ArgumentCaptor.forClass(TodoItem.class);

        todoService.addTodoItem(todoItemRequest);

        verify(todoRepository).save(argumentCaptor.capture());
        TodoItem todoItemAdded = argumentCaptor.getValue();
        assertEquals(todoItemRequest.getDescription(), todoItemAdded.getDescription());
        assertEquals(todoItemRequest.getIsFinished(), todoItemAdded.isFinished());
        assertEquals(currentUser.getId(), todoItemAdded.getUser().getId());
    }

    @Test
    public void testAddTodoItem_failWithDuplicateItem() {
        TodoItemRequest todoItemRequest = new TodoItemRequest().setDescription("todo item").setIsFinished(false);
        when(todoRepository.findByDescriptionAndUserId(todoItemRequest.getDescription(), currentUser.getId()))
                .thenReturn(Optional.of(existingTodoItem));

        TodoItemBadRequestException ex = assertThrows(TodoItemBadRequestException.class,
                () -> todoService.addTodoItem(todoItemRequest));
        assertEquals("Todo item with same description exists", ex.getMessage());
    }

    @Test
    public void testUpdateTodoItem() {
        String newDescription = "todo item changed";
        TodoItemRequest todoItemRequest = new TodoItemRequest().setDescription(newDescription)
                .setIsFinished(true);
        when(todoRepository.findById(1L)).thenReturn(Optional.of(existingTodoItem));
        ArgumentCaptor<TodoItem> argumentCaptor = ArgumentCaptor.forClass(TodoItem.class);

        todoService.updateTodoItem(1L, todoItemRequest);

        verify(todoRepository).save(argumentCaptor.capture());
        TodoItem todoItemUpdated = argumentCaptor.getValue();
        assertEquals(newDescription, todoItemUpdated.getDescription());
        assertTrue(todoItemUpdated.isFinished());
    }

    @Test
    public void testDeleteTodoItem() {
        when(todoRepository.findById(1L)).thenReturn(Optional.of(existingTodoItem));

        todoService.deleteTodoItem(1L);

        verify(todoRepository).deleteById(1L);
    }

}