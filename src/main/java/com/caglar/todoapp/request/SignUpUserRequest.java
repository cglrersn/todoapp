package com.caglar.todoapp.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class SignUpUserRequest {

    @NotBlank(message = "email should be defined for registration")
    private String email;

    @NotBlank(message = "password should be defined for registration")
    private String password;

    @NotBlank(message = "firstName should be defined for registration")
    private String firstName;

    @NotBlank(message = "lastName should be defined for registration")
    private String lastName;
}
