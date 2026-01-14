package learning.assignment.service.user;

import learning.assignment.dto.UserAuthDTO;
import learning.assignment.model.User;
import learning.assignment.repository.UserRepository;
import learning.assignment.util.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserAuthServiceImpl extends UserServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;

    @Autowired
    public UserAuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtEncoder jwtEncoder) {
        super(userRepository);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
    }


    @Transactional
    public User registerUser(UserAuthDTO userAuthDTO) {
        User user = new User();
        user.setUsername(userAuthDTO.username);
        user.setPassword(passwordEncoder.encode(userAuthDTO.password));
        Set<Roles> roles = new HashSet<>();
        roles.add(Roles.ROLE_ADMIN);
        user.setRoles(roles);
        return userRepository.save(user);
    }

    public String JWTLogin(UserAuthDTO userAuthDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userAuthDTO.username, userAuthDTO.password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Instant now = Instant.now();
        var userDetails = (org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal();
        var roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        var claims = org.springframework.security.oauth2.jwt.JwtClaimsSet.builder()
                .subject(userDetails.getUsername())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(3600)) // 1 hour
                .claim("roles", roles)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public Optional<User> getCurrentUser() {
        Authentication authenticationToken = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        String username = (String) jwt.getClaims().get("sub");
        return userRepository.findUserByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }




    //TODO:
    //flyway migr, edgecase uri exceptii,
}
