package learning.assignment.service;

import jakarta.servlet.http.HttpServletRequest;
import learning.assignment.dto.UserDTO;
import learning.assignment.model.User;
import learning.assignment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUserById(Long id, HttpServletRequest request) {
        return userRepository.getUserById(id);
    }

    public String updateUser(Long id, UserDTO userDTO, HttpServletRequest request) {
        Optional<User> user = userRepository.getUserById(id);
        return null;
    }
}
