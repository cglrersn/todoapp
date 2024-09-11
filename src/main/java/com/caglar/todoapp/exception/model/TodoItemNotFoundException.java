package com.caglar.todoapp.exception.model;

public class TodoItemNotFoundException extends RuntimeException {

    public TodoItemNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
