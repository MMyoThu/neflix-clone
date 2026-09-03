package com.example.streamflix.controller;

import com.example.streamflix.dto.PasswordChangeForm;
import com.example.streamflix.dto.ProfileUpdateForm;
import com.example.streamflix.entity.Profile;
import com.example.streamflix.entity.User;
import com.example.streamflix.service.CurrentUserService;
import com.example.streamflix.service.ProfileService;
import com.example.streamflix.service.WatchProgressService;
import com.example.streamflix.util.AppConstants;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {

    private static final String[] AVATARS = {
            "/images/avatars/avatar-1.svg",
            "/images/avatars/avatar-2.svg",
            "/images/avatars/avatar-3.svg",
            "/images/avatars/avatar-4.svg"
    };

    private final CurrentUserService currentUserService;
    private final ProfileService profileService;
    private final WatchProgressService watchProgressService;

    public ProfileController(
            CurrentUserService currentUserService,
            ProfileService profileService,
            WatchProgressService watchProgressService
    ) {
        this.currentUserService = currentUserService;
        this.profileService = profileService;
        this.watchProgressService = watchProgressService;
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        User user = currentUserService.requireUser();
        Profile profile = currentUserService.requireProfile(session);
        if (!model.containsAttribute("profileForm")) {
            ProfileUpdateForm form = new ProfileUpdateForm();
            form.setFirstName(user.getFirstName());
            form.setLastName(user.getLastName());
            form.setProfileName(profile.getProfileName());
            form.setAvatarUrl(profile.getAvatarUrl());
            model.addAttribute("profileForm", form);
        }
        if (!model.containsAttribute("passwordForm")) {
            model.addAttribute("passwordForm", new PasswordChangeForm());
        }
        model.addAttribute("avatars", AVATARS);
        model.addAttribute("history", watchProgressService.history(profile));
        model.addAttribute("navActive", "profile");
        return "profile/index";
    }

    @PostMapping("/profile")
    public String updateProfile(
            @Valid @ModelAttribute("profileForm") ProfileUpdateForm form,
            BindingResult bindingResult,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        User user = currentUserService.requireUser();
        Profile profile = currentUserService.requireProfile(session);
        if (bindingResult.hasErrors()) {
            model.addAttribute("passwordForm", new PasswordChangeForm());
            model.addAttribute("avatars", AVATARS);
            model.addAttribute("history", watchProgressService.history(profile));
            model.addAttribute("navActive", "profile");
            return "profile/index";
        }
        profileService.updateAccount(user, profile, form);
        redirectAttributes.addFlashAttribute("updated", true);
        return "redirect:/profile";
    }

    @PostMapping("/profile/password")
    public String changePassword(
            @Valid @ModelAttribute("passwordForm") PasswordChangeForm form,
            BindingResult bindingResult,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        User user = currentUserService.requireUser();
        Profile profile = currentUserService.requireProfile(session);
        if (bindingResult.hasErrors()) {
            ProfileUpdateForm profileForm = new ProfileUpdateForm();
            profileForm.setFirstName(user.getFirstName());
            profileForm.setLastName(user.getLastName());
            profileForm.setProfileName(profile.getProfileName());
            profileForm.setAvatarUrl(profile.getAvatarUrl());
            model.addAttribute("profileForm", profileForm);
            model.addAttribute("avatars", AVATARS);
            model.addAttribute("history", watchProgressService.history(profile));
            model.addAttribute("navActive", "profile");
            return "profile/index";
        }
        try {
            profileService.changePassword(user, form);
        } catch (BadCredentialsException ex) {
            bindingResult.rejectValue("currentPassword", "mismatch", "Current password is incorrect");
            ProfileUpdateForm profileForm = new ProfileUpdateForm();
            profileForm.setFirstName(user.getFirstName());
            profileForm.setLastName(user.getLastName());
            profileForm.setProfileName(profile.getProfileName());
            profileForm.setAvatarUrl(profile.getAvatarUrl());
            model.addAttribute("profileForm", profileForm);
            model.addAttribute("avatars", AVATARS);
            model.addAttribute("history", watchProgressService.history(profile));
            model.addAttribute("navActive", "profile");
            return "profile/index";
        }
        redirectAttributes.addFlashAttribute("passwordUpdated", true);
        return "redirect:/profile";
    }

    @GetMapping("/profiles")
    public String profiles(Model model) {
        User user = currentUserService.requireUser();
        model.addAttribute("profiles", profileService.profilesFor(user));
        model.addAttribute("avatars", AVATARS);
        model.addAttribute("navActive", "profile");
        return "profile/select";
    }

    @PostMapping("/profiles/{id}/select")
    public String select(@PathVariable Long id, HttpSession session) {
        User user = currentUserService.requireUser();
        Profile profile = profileService.selectProfile(user, id);
        session.setAttribute(AppConstants.SESSION_PROFILE_ID, profile.getId());
        return "redirect:/";
    }

    @PostMapping("/profiles")
    public String create(@RequestParam String profileName, @RequestParam(required = false) String avatarUrl) {
        User user = currentUserService.requireUser();
        profileService.createProfile(user, profileName, avatarUrl);
        return "redirect:/profiles";
    }
}
