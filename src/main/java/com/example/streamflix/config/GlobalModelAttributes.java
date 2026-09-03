package com.example.streamflix.config;

import com.example.streamflix.entity.Profile;
import com.example.streamflix.entity.User;
import com.example.streamflix.service.CurrentUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

    private final CurrentUserService currentUserService;

    public GlobalModelAttributes(CurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
    }

    @ModelAttribute("currentProfileName")
    public String currentProfileName(Authentication authentication, HttpSession session) {
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }
        try {
            Profile profile = currentUserService.requireProfile(session);
            return profile.getProfileName();
        } catch (Exception ex) {
            return null;
        }
    }

    @ModelAttribute("currentUserEmail")
    public String currentUserEmail(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }
        try {
            User user = currentUserService.requireUser();
            return user.getEmail();
        } catch (Exception ex) {
            return null;
        }
    }
}
