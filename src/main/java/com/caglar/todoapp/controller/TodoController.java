package com.caglar.todoapp.controller;

import com.caglar.todoapp.entity.TodoItem;
import com.caglar.todoapp.request.TodoItemRequest;
import com.caglar.todoapp.service.ITodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Todo", description = "Contain the Todo list related APIs")
@Slf4j
@RequestMapping("/todo")
@RestController
public class TodoController {

    private final ITodoService todoService;

    @Autowired
    public TodoController(ITodoService todoService) {
        this.todoService = todoService;
    }

    @Operation(summary = "Get a todo item",
            description = "Return the todo item with the id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = TodoItem.class),
                    mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) })})
    @GetMapping("/{todoItemId}")
    public ResponseEntity<TodoItem> getTodoItem(
            @PathVariable
            @Positive
            @Parameter(description = "todo item id to retrieve", required = true) Long todoItemId) {
        log.debug("/todo is called for itemId={}", todoItemId);
        return ResponseEntity.ok(todoService.getTodoItem(todoItemId));
    }

    @Operation(summary = "Get todo item list",
            description = "Return the todo item list for the user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = List.class),
                    mediaType = "application/json") })})
    @GetMapping("/list")
    public ResponseEntity<List<TodoItem>> getTodoItemListForUser() {
        log.debug("/todo/list is called");
        return ResponseEntity.ok(todoService.getTodoItemListForUser());
    }

    @Operation(summary = "Add a todo item",
            description = "Add new todo item into the user's list")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = TodoItem.class),
                    mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) })})
    @PostMapping("/create")
    public ResponseEntity<TodoItem> addTodoItem(@RequestBody @Valid TodoItemRequest todoItemRequest) {
        log.debug("/todo/create is called");
        return ResponseEntity.ok(todoService.addTodoItem(todoItemRequest));
    }

    @Operation(summary = "Update a todo item",
            description = "Update the existing todo item in the user's list")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = TodoItem.class),
                    mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) })})
    @PutMapping("/{todoItemId}/update")
    public ResponseEntity<TodoItem> updateTodoItem(
            @PathVariable
            @Positive
            @Parameter(description = "todo item id to update", required = true) Long todoItemId,
            @RequestBody @Valid TodoItemRequest todoItemRequest) {
        log.debug("/todo/update is called for itemId={}", todoItemId);
        return ResponseEntity.ok(todoService.updateTodoItem(todoItemId, todoItemRequest));
    }

    @Operation(summary = "Delete a todo item",
            description = "Delete the existing todo item from the user's list")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = String.class),
                    mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) })})
    @DeleteMapping("/{todoItemId}/delete")
    public ResponseEntity<String> deleteTodoItem(
            @PathVariable
            @Positive
            @Parameter(description = "todo item id to delete", required = true) Long todoItemId) {
        log.debug("/todo/delete is called for itemId={}", todoItemId);
        todoService.deleteTodoItem(todoItemId);
        return ResponseEntity.ok("OK");
    }

}
