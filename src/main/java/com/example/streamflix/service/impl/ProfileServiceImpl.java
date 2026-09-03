package com.example.streamflix.service.impl;

import com.example.streamflix.dto.PasswordChangeForm;
import com.example.streamflix.dto.ProfileUpdateForm;
import com.example.streamflix.entity.Profile;
import com.example.streamflix.entity.User;
import com.example.streamflix.exception.ResourceNotFoundException;
import com.example.streamflix.repository.ProfileRepository;
import com.example.streamflix.repository.UserRepository;
import com.example.streamflix.service.ProfileService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileServiceImpl(
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
    public void updateAccount(User user, Profile profile, ProfileUpdateForm form) {
        user.setFirstName(form.getFirstName().trim());
        user.setLastName(form.getLastName().trim());
        userRepository.save(user);
        if (StringUtils.hasText(form.getProfileName())) {
            profile.setProfileName(form.getProfileName().trim());
        }
        profile.setAvatarUrl(form.getAvatarUrl());
        profileRepository.save(profile);
    }

    @Override
    @Transactional
    public void changePassword(User user, PasswordChangeForm form) {
        if (!passwordEncoder.matches(form.getCurrentPassword(), user.getPassword())) {
            throw new BadCredentialsException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(form.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Profile> profilesFor(User user) {
        return profileRepository.findByUserOrderByIdAsc(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Profile selectProfile(User user, Long profileId) {
        return profileRepository.findByIdAndUser(profileId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
    }

    @Override
    @Transactional
    public Profile createProfile(User user, String name, String avatarUrl) {
        Profile profile = new Profile();
        profile.setUser(user);
        profile.setProfileName(name);
        profile.setAvatarUrl(avatarUrl);
        return profileRepository.save(profile);
    }
}
