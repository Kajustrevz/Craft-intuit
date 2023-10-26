package com.craft.craftapp.auth;

import com.craft.craftapp.common.dto.ApiResponse;
import com.craft.craftapp.common.dto.ApiStatus;
import com.craft.craftapp.auth.dto.UserRequest;
import com.craft.craftapp.auth.dto.TokenDetails;
import com.craft.craftapp.exception.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final UserService userService;

    @GetMapping("/hello")
    public String hello(Authentication auth) {
        User user = (User) auth.getPrincipal();
        return "Hello " + user.getUsername() + "!";
    }

    @PostMapping("signup")
    public ResponseEntity<ApiResponse> register(@RequestBody UserRequest request) throws UserAlreadyExistsException {
        log.debug("received signup request for user={}", request.getUsername());
        userService.register(request);

        ApiResponse apiResponse = ApiResponse.builder().message("Sign-up successful").apiStatus(ApiStatus.SUCCESS).build();
        log.debug("completed signup request");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDetails> login(@RequestBody UserRequest request) {
        log.debug("received verify-otp for user={}", request.getUsername());
        final TokenDetails details = userService.getToken(request);
        log.debug("completed verify-otp request");
        return new ResponseEntity<TokenDetails>(details, HttpStatus.OK);
    }

}
