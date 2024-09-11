package com.caglar.todoapp.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class LoginUserRequest {

    @NotBlank(message = "email should be defined for login")
    private String email;

    @NotBlank(message = "password should be defined for login")
    private String password;
}
