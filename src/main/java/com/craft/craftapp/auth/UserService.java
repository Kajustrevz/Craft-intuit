package com.craft.craftapp.auth;

import com.craft.craftapp.auth.dto.UserRequest;
import com.craft.craftapp.auth.dto.TokenDetails;
import com.craft.craftapp.exception.ResourceNotFoundException;
import com.craft.craftapp.exception.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtTokenUtil jwtTokenUtil;

    public void register(UserRequest request) throws UserAlreadyExistsException {
        if(userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists: " + request.getUsername());
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    public User getUser(Long userId) throws ResourceNotFoundException {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(String.format("user not found with id=%d", userId)));
    }

    public TokenDetails getToken(UserRequest request) {
        log.info("received auth request for username {}", request.getUsername());
        try {
            final Authentication authenticate = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            User user = (User) authenticate.getPrincipal();

            return TokenDetails.builder()
                    .token(jwtTokenUtil.generateToken(user))
                    .username(user.getUsername())
                    .build();
        } catch (Exception e) {
            log.error("User authentication failed.", e);
            throw e;
        }
    }
}
