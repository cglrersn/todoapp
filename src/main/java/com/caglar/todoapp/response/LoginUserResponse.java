package com.caglar.todoapp.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class LoginUserResponse {

    private String token;

    private Long expiresIn;
}
