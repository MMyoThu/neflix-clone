package com.example.streamflix.controller;

import com.example.streamflix.service.CurrentUserService;
import com.example.streamflix.service.MyListService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyListController {

    private final MyListService myListService;
    private final CurrentUserService currentUserService;

    public MyListController(MyListService myListService, CurrentUserService currentUserService) {
        this.myListService = myListService;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/my-list")
    public String myList(HttpSession session, Model model) {
        var profile = currentUserService.requireProfile(session);
        model.addAttribute("items", myListService.list(profile));
        model.addAttribute("navActive", "my-list");
        return "my-list/index";
    }
}
