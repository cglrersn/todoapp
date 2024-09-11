package com.caglar.todoapp.controller;

import com.caglar.todoapp.request.LoginUserRequest;
import com.caglar.todoapp.request.SignUpUserRequest;
import com.caglar.todoapp.entity.User;
import com.caglar.todoapp.impl.JwtService;
import com.caglar.todoapp.response.LoginUserResponse;
import com.caglar.todoapp.service.IAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "Signup and Login APIs")
@Slf4j
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final JwtService jwtService;
    private final IAuthService authService;

    @Autowired
    public AuthController(JwtService jwtService, IAuthService authService) {
        this.jwtService = jwtService;
        this.authService = authService;
    }

    @GetMapping("/healthcheck")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }

    @Operation(summary = "Signup the user",
            description = "Create the user to use the todo app")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = User.class),
                    mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) })})
    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@RequestBody @Valid SignUpUserRequest signUpUserRequest) {
        log.debug("/auth/signup is called");
        return ResponseEntity.ok(authService.signUp(signUpUserRequest));
    }

    @Operation(summary = "Login the user",
            description = "Log user in to the system and return the token")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = { @Content(schema = @Schema(implementation = LoginUserResponse.class),
                    mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) })})
    @PostMapping("/login")
    public ResponseEntity<LoginUserResponse> login(@RequestBody @Valid LoginUserRequest loginUserRequest) {
        log.debug("/auth/login is called for {}", loginUserRequest.getEmail());
        User authUser = authService.authenticate(loginUserRequest);
        String jwtToken = jwtService.generateToken(authUser);
        return ResponseEntity.ok(new LoginUserResponse().setToken(jwtToken).setExpiresIn(jwtService.getExpirationTime()));
    }
}
