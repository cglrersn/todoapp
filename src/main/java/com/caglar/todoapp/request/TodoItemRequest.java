package com.caglar.todoapp.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class TodoItemRequest {

    @NotBlank(message = "description should be defined")
    private String description;

    @NotNull(message = "isFinished should be defined")
    private Boolean isFinished;
}
