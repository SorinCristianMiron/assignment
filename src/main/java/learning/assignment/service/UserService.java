package learning.assignment.service;

import learning.assignment.dto.UserAuthDTO;
import learning.assignment.dto.UserDTO;
import learning.assignment.model.User;
import learning.assignment.repository.UserRepository;
import learning.assignment.util.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Transactional
    public User registerUser(UserAuthDTO userAuthDTO) {
        User user = new User();
        user.setUsername(userAuthDTO.username);
        user.setPassword(passwordEncoder.encode(userAuthDTO.password));
        ArrayList<Roles> roles = new ArrayList<>();
        roles.add(Roles.ADMIN);
        user.setRoles(roles);
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.getUserById(id);
    }

    public String updateUser(Long id, UserDTO userDTO) {
        Optional<User> user = userRepository.getUserById(id);
        Optional<User> currentUser = getCurrentUser();
        if(currentUser.isEmpty()) return "not_logged_in";
        if (user.isPresent()) {
            user.get().setEmail(userDTO.email);
            user.get().setDescription(userDTO.description);
            userRepository.save(user.get());
            return "success";
        }
        return "not_found";
    }

    public Optional<User> getCurrentUser() {
        Authentication authenticationToken = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        String username = (String) jwt.getClaims().get("sub");
        return userRepository.findUserByUsername(username);
    }
}
