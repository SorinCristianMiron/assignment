package learning.assignment.service.user;

import learning.assignment.dto.UserDTO;
import learning.assignment.exceptions.ResourceNotFoundException;
import learning.assignment.model.User;
import learning.assignment.repository.UserRepository;
import learning.assignment.util.Roles;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getUserById_returnsUser_whenExists() {
        User user = new User();
        user.setId(1L);
        when(userRepository.getUserById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userRepository).getUserById(1L);
    }

    @Test
    void getUserById_throwsException_whenNotFound() {
        when(userRepository.getUserById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserById(1L));
    }

    @Test
    void updateUser_updatesAndSavesUser() {
        User existing = new User();
        existing.setId(1L);
        existing.setEmail("old@mail.com");

        when(userRepository.getUserById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserDTO dto = new UserDTO();
        dto.email = "new@mail.com";
        dto.description = "new desc";
        dto.roles = Set.of(Roles.ROLE_USER);

        User result = userService.updateUser(1L, dto);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User saved = captor.getValue();
        assertEquals("new@mail.com", saved.getEmail());
        assertEquals("new desc", saved.getDescription());
        assertSame(saved, result);
    }
}