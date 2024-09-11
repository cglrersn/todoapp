package com.caglar.todoapp.exception.model;

public class TodoItemBadRequestException extends RuntimeException {

    public TodoItemBadRequestException(String errorMessage) {
        super(errorMessage);
    }
}
