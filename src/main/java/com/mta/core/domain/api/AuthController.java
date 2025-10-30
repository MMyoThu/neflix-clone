package com.mta.core.domain.api;

import com.mta.core.domain.request.LoginRequest;
import com.mta.core.domain.request.RegisterRequest;
import com.mta.core.domain.response.LoginResponse;
import com.mta.core.domain.response.RegisterResponse;
import com.mta.core.domain.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserAuthService userAuthService;

    @PostMapping("/register")
    @CrossOrigin(origins = "*")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        log.info("Received registration request for email: {}", request.getEmail());
        
        RegisterResponse response = userAuthService.register(request);
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        log.info("Received login request for email: {}", request.getEmail());
        
        LoginResponse response = userAuthService.login(request);
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshToken(@RequestParam String verifyToken) {
        log.debug("Received token refresh request");
        
        String newToken = userAuthService.refreshToken(verifyToken);
        
        return ResponseEntity.ok(newToken);
    }
}
