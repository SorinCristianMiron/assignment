package learning.assignment.service.user;

import learning.assignment.dto.UserSearchDTO;
import learning.assignment.model.User;
import learning.assignment.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserSearchServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserSearchServiceImpl userSearchService;

    @Test
    void search_callsRepositoryWithSpecificationAndPageRequest() {
        UserSearchDTO dto = new UserSearchDTO();
        dto.setEmail("test@mail.com");
        dto.setUsername("userTest");

        Page<User> page = new PageImpl<>(java.util.List.of());
        when(userRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);

        Page<User> result = userSearchService.search(dto, 0, 10, "[{\"field\":\"username\",\"direction\":\"asc\"}]");

        assertSame(page, result);
        verify(userRepository).findAll(any(Specification.class), any(Pageable.class));
    }
}
