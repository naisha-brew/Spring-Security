package com.gsmarin.springsecurity.service;

import com.gsmarin.springsecurity.Entity.LoggingRequest;
import com.gsmarin.springsecurity.Entity.LoggingResponse;
import com.gsmarin.springsecurity.Entity.Role;
import com.gsmarin.springsecurity.Entity.User;
import com.gsmarin.springsecurity.Repository.RoleRepository;
import com.gsmarin.springsecurity.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtEncoder jwtEncoder;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleService roleService;


    public ResponseEntity<LoggingResponse> login(String username, String password) {
        Optional<User>  user = userRepository.findByUsername(username);

        if (user.isPresent() && user.get().isLogginCorrect(password, bCryptPasswordEncoder)) {
            throw new BadCredentialsException("Invalid username or password");
        }

        var jwtExpiresIn = 300L;

        var scopes = user.get().getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("mybackend")
                .subject(user.get().getId().toString())
                .expiresAt(Instant.now().plusSeconds(jwtExpiresIn))
                .claim("scope", scopes)
                .issuedAt(Instant.now()).build();

        var jwt_Value = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.ok(new LoggingResponse(jwt_Value, jwtExpiresIn));

    }

    public Optional<User>  findByUsername(String admin) {
        return userRepository.findByUsername("admin");
    }

    public void save(User user) {
        var role = roleService.findByName(user.getUsername());
        var userFromDb = userRepository.findByUsername(user.getUsername());

        if(userFromDb.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "User already exists");
        }
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        if(role == null) {
            role = roleService.findByName(Role.Values.BASIC.name());
        }
        newUser.setRoles(Set.of(role));
        userRepository.save(newUser);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }
}
