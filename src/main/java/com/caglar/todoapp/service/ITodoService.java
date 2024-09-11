package com.caglar.todoapp.service;

import com.caglar.todoapp.entity.TodoItem;
import com.caglar.todoapp.request.TodoItemRequest;
import java.util.List;

public interface ITodoService {

    TodoItem getTodoItem(Long todoItemId);

    List<TodoItem> getTodoItemListForUser();

    TodoItem addTodoItem(TodoItemRequest todoItemRequest);

    TodoItem updateTodoItem(Long todoItemId, TodoItemRequest todoItemRequest);

    void deleteTodoItem(Long todoItemId);
}
