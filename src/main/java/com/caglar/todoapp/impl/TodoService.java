package com.caglar.todoapp.impl;

import com.caglar.todoapp.entity.TodoItem;
import com.caglar.todoapp.entity.User;
import com.caglar.todoapp.exception.model.TodoItemBadRequestException;
import com.caglar.todoapp.exception.model.TodoItemNotFoundException;
import com.caglar.todoapp.repository.TodoRepository;
import com.caglar.todoapp.request.TodoItemRequest;
import com.caglar.todoapp.service.ITodoService;
import com.caglar.todoapp.service.IUserService;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TodoService implements ITodoService {

    private final IUserService userService;

    private final TodoRepository todoRepository;

    @Autowired
    public TodoService(IUserService userService, TodoRepository todoRepository) {
        this.userService = userService;
        this.todoRepository = todoRepository;
    }

    @Override
    public TodoItem getTodoItem(Long todoItemId) {
        Optional<TodoItem> optionalTodoItem = todoRepository.findById(todoItemId);
        validateTodoItemExistingAndBelongToUser(optionalTodoItem);

        return optionalTodoItem.get();
    }

    @Override
    public List<TodoItem> getTodoItemListForUser() {
        User currentUser = userService.getCurrentUser();
        return todoRepository.findByUserId(currentUser.getId());
    }

    @Override
    public TodoItem addTodoItem(TodoItemRequest todoItemRequest) {
        User user = userService.getCurrentUser();
        if (todoRepository.findByDescriptionAndUserId(todoItemRequest.getDescription(), user.getId()).isPresent()) {
            throw new TodoItemBadRequestException("Todo item with same description exists");
        }

        TodoItem todoItemToAdd = new TodoItem().setDescription(todoItemRequest.getDescription())
                .setFinished(todoItemRequest.getIsFinished()).setUser(user);
        return todoRepository.save(todoItemToAdd);
    }

    @Override
    public TodoItem updateTodoItem(Long todoItemId, TodoItemRequest todoItemRequest) {
        TodoItem todoItemToUpdate = getTodoItem(todoItemId);
        todoItemToUpdate.setDescription(todoItemRequest.getDescription());
        todoItemToUpdate.setFinished(todoItemRequest.getIsFinished());
        TodoItem updatedTodoItem = todoRepository.save(todoItemToUpdate);
        log.debug("Todo item is updated with todoItemId={}", todoItemId);
        return  updatedTodoItem;
    }

    @Override
    public void deleteTodoItem(Long todoItemId) {
        getTodoItem(todoItemId);
        todoRepository.deleteById(todoItemId);
        log.debug("Todo item is deleted with todoItemId={}", todoItemId);
    }

    private void validateTodoItemExistingAndBelongToUser(Optional<TodoItem> optionalTodoItem) {
        User currentUser = userService.getCurrentUser();
        if (optionalTodoItem.isEmpty()
                || !Objects.equals(optionalTodoItem.get().getUser().getId(), currentUser.getId())) {
            log.error("Todo item does not exist for userId={}", currentUser.getId());
            throw new TodoItemNotFoundException("Todo item does not exist");
        }
        log.debug("Todo item found with todoItemId={}", optionalTodoItem.get().getId());
    }
}
