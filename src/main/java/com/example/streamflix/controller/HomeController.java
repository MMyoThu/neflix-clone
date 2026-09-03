package com.example.streamflix.controller;

import com.example.streamflix.entity.Profile;
import com.example.streamflix.service.CatalogService;
import com.example.streamflix.service.CurrentUserService;
import com.example.streamflix.util.AppConstants;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final CatalogService catalogService;
    private final CurrentUserService currentUserService;

    public HomeController(CatalogService catalogService, CurrentUserService currentUserService) {
        this.catalogService = catalogService;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        Profile profile = currentUserService.requireProfile(session);
        model.addAttribute("appName", AppConstants.APPLICATION_NAME);
        model.addAttribute("hero", catalogService.featuredHero());
        model.addAttribute("rows", catalogService.homeRows(profile));
        model.addAttribute("navActive", "home");
        return "home/index";
    }
}
