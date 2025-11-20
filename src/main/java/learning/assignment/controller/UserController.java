package learning.assignment.controller;

import jakarta.servlet.http.HttpServletRequest;
import learning.assignment.model.User;
import learning.assignment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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
}
