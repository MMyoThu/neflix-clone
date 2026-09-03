package com.example.streamflix.controller;

import com.example.streamflix.entity.Episode;
import com.example.streamflix.entity.Movie;
import com.example.streamflix.entity.Profile;
import com.example.streamflix.service.CatalogService;
import com.example.streamflix.service.CurrentUserService;
import com.example.streamflix.service.WatchProgressService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class WatchController {

    private final CatalogService catalogService;
    private final WatchProgressService watchProgressService;
    private final CurrentUserService currentUserService;

    public WatchController(
            CatalogService catalogService,
            WatchProgressService watchProgressService,
            CurrentUserService currentUserService
    ) {
        this.catalogService = catalogService;
        this.watchProgressService = watchProgressService;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/watch/movie/{id}")
    public String watchMovie(@PathVariable Long id, HttpSession session, Model model) {
        Profile profile = currentUserService.requireProfile(session);
        Movie movie = catalogService.requireMovie(id);
        model.addAttribute("title", movie.getTitle());
        model.addAttribute("videoUrl", movie.getVideoUrl());
        model.addAttribute("posterUrl", movie.getBackdropUrl());
        model.addAttribute("progressSeconds", watchProgressService.movieProgressSeconds(profile, movie));
        model.addAttribute("progressUrl", "/api/watch-progress/movie/" + id);
        model.addAttribute("backHref", "/movies/" + id);
        return "watch/player";
    }

    @GetMapping("/watch/episode/{id}")
    public String watchEpisode(@PathVariable Long id, HttpSession session, Model model) {
        Profile profile = currentUserService.requireProfile(session);
        Episode episode = watchProgressService.requireEpisode(id);
        var show = episode.getSeason().getTvShow();
        model.addAttribute("title", show.getTitle() + " · " + episode.getTitle());
        model.addAttribute("videoUrl", episode.getVideoUrl());
        model.addAttribute("posterUrl", show.getBackdropUrl());
        model.addAttribute("progressSeconds", watchProgressService.episodeProgressSeconds(profile, episode));
        model.addAttribute("progressUrl", "/api/watch-progress/episode/" + id);
        model.addAttribute("backHref", "/tv/" + show.getId());
        return "watch/player";
    }

    @GetMapping("/continue-watching")
    public String continueWatching(HttpSession session, Model model) {
        Profile profile = currentUserService.requireProfile(session);
        model.addAttribute("items", watchProgressService.continueWatching(profile));
        model.addAttribute("navActive", "home");
        return "watch/continue";
    }
}
