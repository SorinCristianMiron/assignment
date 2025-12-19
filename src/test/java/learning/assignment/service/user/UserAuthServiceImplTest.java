package learning.assignment.service.user;

import learning.assignment.dto.UserAuthDTO;
import learning.assignment.model.User;
import learning.assignment.repository.UserRepository;
import learning.assignment.util.Roles;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtEncoder jwtEncoder;

    @InjectMocks
    private UserAuthServiceImpl userAuthService;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void registerUser_encodesPassword_andSavesUser() {
        UserAuthDTO dto = new UserAuthDTO();
        dto.username = "userTest";
        dto.password = "plain";

        when(passwordEncoder.encode("plain")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User saved = userAuthService.registerUser(dto);

        assertEquals("userTest", saved.getUsername());
        assertEquals("encoded", saved.getPassword());
        assertTrue(saved.getRoles().contains(Roles.ROLE_ADMIN));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void jwtLogin_returnsToken_andSetsSecurityContext() {
        UserAuthDTO dto = new UserAuthDTO();
        dto.username = "userTest";
        dto.password = "pass";

        var authorities = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        var principal = new org.springframework.security.core.userdetails.User(
                "userTest", "encoded", authorities);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                principal, "encoded", authorities);

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(auth);

        Jwt jwt = Jwt.withTokenValue("token-123")
                .header("alg", "none")
                .claim("sub", "userTest")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);

        String token = userAuthService.JWTLogin(dto);

        assertEquals("token-123", token);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void getCurrentUser_returnsUserFromJwt() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaims()).thenReturn(Map.of("sub", "userTest"));

        Authentication auth = mock(Authentication.class);
        when(auth.getCredentials()).thenReturn(jwt);

        SecurityContextHolder.getContext().setAuthentication(auth);

        User user = new User();
        user.setUsername("userTest");

        when(userRepository.findUserByUsername("userTest"))
                .thenReturn(Optional.of(user));

        Optional<User> result = userAuthService.getCurrentUser();

        assertTrue(result.isPresent());
        assertEquals("userTest", result.get().getUsername());
    }

    @Test
    void loadUserByUsername_returnsUserDetails_whenFound() {
        User user = new User();
        user.setUsername("userTest");
        user.setPassword("pass");
        user.setRoles(Set.of(Roles.ROLE_ADMIN));

        when(userRepository.findUserByUsername("userTest"))
                .thenReturn(Optional.of(user));

        var details = userAuthService.loadUserByUsername("userTest");

        assertEquals("userTest", details.getUsername());
        assertEquals("pass", details.getPassword());
    }

    @Test
    void loadUserByUsername_throws_whenNotFound() {
        when(userRepository.findUserByUsername("userTest"))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userAuthService.loadUserByUsername("userTest"));
    }
}
