package com.example.streamflix.service;

import com.example.streamflix.dto.PasswordChangeForm;
import com.example.streamflix.dto.ProfileUpdateForm;
import com.example.streamflix.entity.Profile;
import com.example.streamflix.entity.User;

import java.util.List;

public interface ProfileService {

    void updateAccount(User user, Profile profile, ProfileUpdateForm form);

    void changePassword(User user, PasswordChangeForm form);

    List<Profile> profilesFor(User user);

    Profile selectProfile(User user, Long profileId);

    Profile createProfile(User user, String name, String avatarUrl);
}
