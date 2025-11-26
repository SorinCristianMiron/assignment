package learning.assignment.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import learning.assignment.dto.UserAuthDTO;
import learning.assignment.model.User;
import learning.assignment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@Validated
@RestController
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;

    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtEncoder jwtEncoder) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
    }

    @GetMapping("/getUserById")
    public ResponseEntity<Object> getUserById(@RequestParam("id") Long id, HttpServletRequest request) {
        Optional<User> user = userService.getUserById(id,request);
        if (user.isPresent()) {
            return new ResponseEntity<>(
                    user.get(),
                    HttpStatus.OK);
        } else return new ResponseEntity<>(
                "not found",
                HttpStatus.BAD_REQUEST);
    }

    @Operation(security = {}) // removes security
    @PostMapping("/auth/register")
    public ResponseEntity<Object> register(@RequestBody UserAuthDTO userAuthDTO) {
        User user = userService.registerUser(userAuthDTO);
        return new ResponseEntity<>(
                Map.of("message", "User registered successfully", "username", user.getUsername()),
                HttpStatus.OK);
    }

    @Operation(security = {})
    @PostMapping("/auth/login")
    public ResponseEntity<Object> login(@RequestBody UserAuthDTO userAuthDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userAuthDTO.username, userAuthDTO.password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Instant now = Instant.now();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(
                org.springframework.security.oauth2.jwt.JwtClaimsSet.builder()
                        .subject(userAuthDTO.username)
                        .issuedAt(now)
                        .expiresAt(now.plusSeconds(3600)) // 1-hour expiration
                        .claim("scope", "read write")
                        .build()
        )).getTokenValue();
        return new ResponseEntity<>(
                Map.of("access_token", token),
                HttpStatus.OK);
    }
}
