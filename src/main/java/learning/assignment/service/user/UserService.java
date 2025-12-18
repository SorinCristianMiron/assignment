package learning.assignment.service.user;

import learning.assignment.dto.UserDTO;
import learning.assignment.model.User;

public interface UserService {

    User getUserById(Long id);
    User updateUser(Long id, UserDTO userDTO);
}
