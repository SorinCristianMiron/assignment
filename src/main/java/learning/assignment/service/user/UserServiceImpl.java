package learning.assignment.service.user;

import learning.assignment.dto.UserDTO;
import learning.assignment.exceptions.ResourceNotFoundException;
import learning.assignment.model.User;
import learning.assignment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(Long id) {
        return userRepository.getUserById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User updateUser(Long id, UserDTO userDTO) {
        User user = getUserById(id);
        user.setEmail(userDTO.email);
        user.setDescription(userDTO.description);
        user.setRoles(userDTO.roles);
        return userRepository.save(user);
    }


}
