package learning.assignment.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import learning.assignment.dto.UserAuthDTO;
import learning.assignment.dto.UserDTO;
import learning.assignment.dto.UserSearchDTO;
import learning.assignment.model.User;
import learning.assignment.service.user.UserAuthServiceImpl;
import learning.assignment.service.user.UserSearchServiceImpl;
import learning.assignment.service.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Validated
@RestController
public class UserController {

    private final UserServiceImpl userServiceImpl;
    private final UserAuthServiceImpl userAuthService;
    private final UserSearchServiceImpl userSearchServiceImpl;

    @Autowired
    public UserController(UserServiceImpl userServiceImpl, UserAuthServiceImpl userAuthService, UserSearchServiceImpl userSearchServiceImpl) {
        this.userServiceImpl = userServiceImpl;
        this.userAuthService = userAuthService;
        this.userSearchServiceImpl = userSearchServiceImpl;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/updateUser")
    public ResponseEntity<User> updateUser(@RequestParam("id") Long userId, @Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userServiceImpl.updateUser(userId, userDTO));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/getUserById")
    public ResponseEntity<Object> getUserById(@RequestParam("id") Long id) {
        return ResponseEntity.ok(userServiceImpl.getUserById(id));
    }

    @Operation(security = {}) // removes security
    @PostMapping("/auth/register")
    public ResponseEntity<Object> register(@Valid @RequestBody UserAuthDTO userAuthDTO) {
        User user = userAuthService.registerUser(userAuthDTO);
        return new ResponseEntity<>(
                Map.of("message", "User registered successfully", "username", user.getUsername()),
                HttpStatus.CREATED);
    }

    @Operation(security = {})
    @PostMapping("/auth/login")
    public ResponseEntity<Object> login(@Valid @RequestBody UserAuthDTO userAuthDTO) {
        String token = userAuthService.JWTLogin(userAuthDTO);
        return new ResponseEntity<>(
                Map.of("access_token", token),
                HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/searchUser")
    public ResponseEntity<Page<User>> searchUser(@Valid @RequestBody UserSearchDTO userSearchDTO,
                                                 @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                 @RequestParam(name = "size", defaultValue = "5") Integer size,
                                                 @RequestParam(name = "sort", defaultValue = "[{\"field\":\"username\",\"direction\":\"asc\"}]") String sort) {
        return ResponseEntity.ok(userSearchServiceImpl.search(userSearchDTO, page, size, sort));
    }

}
