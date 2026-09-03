package com.example.streamflix.security;

import com.example.streamflix.entity.Profile;
import com.example.streamflix.entity.User;
import com.example.streamflix.repository.ProfileRepository;
import com.example.streamflix.repository.UserRepository;
import com.example.streamflix.util.AppConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    public LoginSuccessHandler(UserRepository userRepository, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        String email = authentication.getName();
        User user = userRepository.findByEmailIgnoreCase(email).orElse(null);
        if (user != null) {
            Profile profile = profileRepository.findFirstByUserOrderByIdAsc(user).orElse(null);
            if (profile != null) {
                request.getSession().setAttribute(AppConstants.SESSION_PROFILE_ID, profile.getId());
            }
        }
        response.sendRedirect(request.getContextPath() + "/");
    }
}
