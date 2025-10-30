package com.mta.core.domain.service.impl;

import com.mta.core.domain.entity.UserInfo;
import com.mta.core.domain.entity.UserRefreshToken;
import com.mta.core.domain.repository.UserInfoRepository;
import com.mta.core.domain.repository.UserRefreshTokenRepository;
import com.mta.core.domain.repository.UserTokenRepository;
import com.mta.core.domain.request.LoginRequest;
import com.mta.core.domain.request.RegisterRequest;
import com.mta.core.domain.response.LoginResponse;
import com.mta.core.domain.response.RegisterResponse;
import com.mta.core.domain.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.issuer}")
    private String issuer;

    @Value("${security.jwt.access-token-ttl-minutes}")
    private long accessMinutes;

    @Value("${security.jwt.refresh-token-ttl-days}")
    private long refreshDays;

    private Key key;

    private final PasswordEncoder passwordEncoder;
    private final UserInfoRepository userInfoRepository;

    private final UserRefreshTokenRepository userRefreshTokenRepository;

    private final UserTokenRepository userTokenRepository;

    @Override
    public RegisterResponse register(RegisterRequest request) {
        log.info("Starting user registration process for email: {}", request.getEmail());
        
        // Validate request
        validateRegisterRequest(request);

        try {
            var userInfo = this.userInfoRepository.findByEmail(request.getEmail());
            if (userInfo != null){
                throw new RuntimeException("User with email " + request.getEmail() + " already exists");
            }

            UserInfo newUser = UserInfo.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .build();

            UserInfo savedUser = this.userInfoRepository.save(newUser);
            
            RegisterResponse response = new RegisterResponse();
            response.setSuccess(true);
            response.setMessage("User registered successfully");
            response.setUserId(savedUser.getId());
            response.setEmail(savedUser.getEmail());
            
            log.info("User registration completed successfully for email: {}", request.getEmail());
            return response;
            
        } catch (Exception e) {
            log.error("Error during user registration for email: {}", request.getEmail(), e);
            throw new RuntimeException("Registration failed due to internal error", e);
        }
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        log.info("Starting user login process for email: {}", request.getEmail());

        try {

            // Find user by email
            var userInfo = this.userInfoRepository.findByEmail(request.getEmail());
            if (userInfo == null){
                throw new RuntimeException("User with email " + request.getEmail() + " does not exist.");
            }
            
            // Verify password
            if (!passwordEncoder.matches(request.getPassword(), userInfo.getPassword())) {
                throw new RuntimeException("Invalid password for email: " + request.getEmail());
            }
            
            // Generate tokens
            String refreshToken = createRefreshToken(userInfo.getId());
            String refreshJti = getJti(parse(refreshToken));
            String accessToken = createAccessToken(userInfo.getId(), userInfo.getEmail(), refreshJti);
            
            // Save refresh token to database (aligned with user_fresh_token columns)
            var userRefreshToken = UserRefreshToken.builder()
                    .userId(userInfo.getId())
                    .refreshToken(refreshToken)
                    .regDt(LocalDateTime.now())
                    .expiredAt(LocalDateTime.now().plusDays(refreshDays))
                    .regId(userInfo.getId())
                    .userType("USER")
                    .build();
            this.userRefreshTokenRepository.save(userRefreshToken);
            
            LoginResponse response = new LoginResponse();
            response.setSuccess(true);
            response.setMessage("Login successful");
            response.setUserId(userInfo.getId());
            response.setEmail(userInfo.getEmail());
            response.setAccessToken(accessToken);
            response.setRefreshToken(refreshToken);
            
            log.info("User login completed successfully for email: {}", request.getEmail());
            return response;
            
        } catch (Exception e) {
            log.error("Error during user login for email: {}", request.getEmail(), e);
            throw new RuntimeException("Login failed due to internal error", e);
        }
    }

    @Override
    public String refreshToken(String verifyToken) {
        log.debug("Starting token refresh process");
        
        // Validate token
        if (verifyToken == null || verifyToken.trim().isEmpty()) {
            throw new RuntimeException("Verify token cannot be null or empty");
        }

        try {
            // Parse and validate provided refresh token
            Jws<Claims> jws = parse(verifyToken);
            if (!isRefresh(jws)) {
                throw new RuntimeException("Token provided is not a refresh token");
            }

            Long userId = getUserId(jws);

            // Rotate refresh token and issue new access token
            String newRefreshToken = createRefreshToken(userId);
            String refreshJti = getJti(parse(newRefreshToken));
            String newAccessToken = createAccessToken(userId, jws.getPayload().get("username", String.class), refreshJti);

            // Persist new refresh token (aligned with user_fresh_token schema)
            var rotated = UserRefreshToken.builder()
                    .userId(userId)
                    .refreshToken(newRefreshToken)
                    .regDt(LocalDateTime.now())
                    .expiredAt(LocalDateTime.now().plusDays(refreshDays))
                    .regId(userId)
                    .userType("USER")
                    .build();
            this.userRefreshTokenRepository.save(rotated);

            log.debug("Token refresh completed successfully for userId: {}", userId);
            // Return new access token (client keeps using previous refresh token or can be updated via separate API if needed)
            return newAccessToken;

        } catch (Exception e) {
            log.error("Error during token refresh", e);
            throw new RuntimeException("Token refresh failed", e);
        }
    }

    private void validateRegisterRequest(RegisterRequest request) {
//        if (request == null) {
//            throw new RuntimeException("Registration request cannot be null");
//        }
//
//        if (request.getFirstName() == null || request.getFirstName().trim().isEmpty()) {
//            throw new RuntimeException("First name is required");
//        }
//
//        if (request.getLastName() == null || request.getLastName().trim().isEmpty()) {
//            throw new RuntimeException("Last name is required");
//        }
//
//        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
//            throw new RuntimeException("Email is required");
//        }
//
//        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
//            throw new RuntimeException("Password is required");
//        }
//
//        if (request.getPassword().length() < 8) {
//            throw new RuntimeException("Password must be at least 8 characters long");
//        }
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String createAccessToken(Long userId, String username, String refreshJti) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(accessMinutes * 60);
        return Jwts.builder()
                .header().type("JWT").and()
                .issuer(issuer)
                .subject(String.valueOf(userId))
                .claim("username", username)
                .claim("typ", "access")
                .claim("rt", refreshJti)
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .id(UUID.randomUUID().toString())
                .signWith(getSigningKey())
                .compact();
    }

    public String createRefreshToken(Long userId) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(refreshDays * 86400);
        return Jwts.builder()
                .header().type("JWT").and()
                .issuer(issuer)
                .subject(String.valueOf(userId))
                .claim("typ", "refresh")
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .id(UUID.randomUUID().toString()) // jti for rotation tracking
                .signWith(getSigningKey())
                .compact();
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
    }

//    public boolean isAccess(Jws<Claims> jws) {
//        return "access".equals(jws.getPayload().get("typ"));
//    }

    public boolean isRefresh(Jws<Claims> jws) {
        return "refresh".equals(jws.getPayload().get("typ"));
    }

    public Long getUserId(Jws<Claims> jws) {
        return Long.valueOf(jws.getPayload().getSubject());
    }

    public String getJti(Jws<Claims> jws) {
        return jws.getPayload().getId();
    }

//    public String getRefreshJtiFromAccess(Jws<Claims> jws) {
//        return jws.getPayload().get("rt", String.class);
//    }


}
