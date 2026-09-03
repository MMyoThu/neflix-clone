package com.example.streamflix.service;

import com.example.streamflix.entity.Profile;
import com.example.streamflix.entity.User;
import com.example.streamflix.exception.ResourceNotFoundException;
import com.example.streamflix.repository.ProfileRepository;
import com.example.streamflix.repository.UserRepository;
import com.example.streamflix.util.AppConstants;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CurrentUserService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    public CurrentUserService(UserRepository userRepository, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    @Transactional(readOnly = true)
    public User requireUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResourceNotFoundException("Not signed in");
        }
        return userRepository.findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }

    @Transactional
    public Profile requireProfile(HttpSession session) {
        User user = requireUser();
        Long profileId = (Long) session.getAttribute(AppConstants.SESSION_PROFILE_ID);
        if (profileId != null) {
            return profileRepository.findByIdAndUser(profileId, user)
                    .orElseGet(() -> assignDefaultProfile(session, user));
        }
        return assignDefaultProfile(session, user);
    }

    @Transactional
    public Profile assignDefaultProfile(HttpSession session, User user) {
        Profile profile = profileRepository.findFirstByUserOrderByIdAsc(user)
                .orElseThrow(() -> new ResourceNotFoundException("No profile found"));
        session.setAttribute(AppConstants.SESSION_PROFILE_ID, profile.getId());
        return profile;
    }
}
