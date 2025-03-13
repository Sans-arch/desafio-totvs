package com.github.sansarch.desafio_totvs.infrastructure.http.controller.auth;

import com.github.sansarch.desafio_totvs.infrastructure.http.dto.auth.AuthenticationDto;
import com.github.sansarch.desafio_totvs.infrastructure.http.dto.auth.RegisterDto;
import com.github.sansarch.desafio_totvs.infrastructure.http.security.JwtService;
import com.github.sansarch.desafio_totvs.infrastructure.persistence.UserRepository;
import com.github.sansarch.desafio_totvs.infrastructure.persistence.model.UserModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Operation(
            description = "Login into the application and obtain a token to use for secured endpoints",
            summary = "Login",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Token generated successfully",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Invalid credentials"
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthenticationDto data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var auth = authenticationManager.authenticate(usernamePassword);

        var userModel = (UserModel) auth.getPrincipal();
        var token = jwtService.generateToken(userModel.getUsername());

        return ResponseEntity.ok(token);
    }

    @Operation(description = "Register into the application", summary = "Register")
    @ApiResponse(responseCode = "201", description = "User registered successfully")
    @ApiResponse(responseCode = "400", description = "User already exists")
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterDto data) {
        if (userRepository.findByUsername(data.username()) != null) {
            return ResponseEntity.badRequest().build();
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        UserModel userModel = new UserModel(data.username(), encryptedPassword);
        userRepository.save(userModel);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
