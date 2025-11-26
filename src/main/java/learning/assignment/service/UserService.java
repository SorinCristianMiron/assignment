package learning.assignment.service;

import jakarta.servlet.http.HttpServletRequest;
import learning.assignment.dto.UserAuthDTO;
import learning.assignment.dto.UserDTO;
import learning.assignment.model.User;
import learning.assignment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(UserAuthDTO userAuthDTO) {
        User user = new User();
        user.setUsername(userAuthDTO.username);
        user.setPassword(passwordEncoder.encode(userAuthDTO.password));
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return (UserDetails) userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public Optional<User> getUserById(Long id, HttpServletRequest request) {
        return userRepository.getUserById(id);
    }

    public String updateUser(Long id, UserDTO userDTO, HttpServletRequest request) {
        Optional<User> user = userRepository.getUserById(id);
        return null;
    }
}
