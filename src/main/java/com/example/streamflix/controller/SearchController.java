package com.example.streamflix.controller;

import com.example.streamflix.entity.Profile;
import com.example.streamflix.service.CurrentUserService;
import com.example.streamflix.service.SearchService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {

    private final SearchService searchService;
    private final CurrentUserService currentUserService;

    public SearchController(SearchService searchService, CurrentUserService currentUserService) {
        this.searchService = searchService;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "q", required = false) String query, HttpSession session, Model model) {
        Profile profile = currentUserService.requireProfile(session);
        var movies = searchService.searchMovies(query, profile);
        var shows = searchService.searchTvShows(query, profile);
        model.addAttribute("q", query);
        model.addAttribute("movies", movies);
        model.addAttribute("shows", shows);
        model.addAttribute("empty", (query != null && !query.isBlank()) && movies.isEmpty() && shows.isEmpty());
        model.addAttribute("navActive", "search");
        return "search/index";
    }
}
