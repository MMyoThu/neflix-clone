package com.example.streamflix.service.impl;

import com.example.streamflix.dto.RegisterForm;
import com.example.streamflix.entity.Profile;
import com.example.streamflix.entity.Role;
import com.example.streamflix.entity.User;
import com.example.streamflix.exception.DuplicateResourceException;
import com.example.streamflix.repository.ProfileRepository;
import com.example.streamflix.repository.UserRepository;
import com.example.streamflix.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(
            UserRepository userRepository,
            ProfileRepository profileRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User register(RegisterForm form) {
        String email = form.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new DuplicateResourceException("An account with that email already exists");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setFirstName(form.getFirstName().trim());
        user.setLastName(form.getLastName().trim());
        user.setRole(Role.USER);
        user.setEnabled(true);
        user = userRepository.save(user);

        Profile profile = new Profile();
        profile.setUser(user);
        profile.setProfileName(user.getFirstName());
        profile.setAvatarUrl("/images/avatars/avatar-1.svg");
        profileRepository.save(profile);
        return user;
    }
}
