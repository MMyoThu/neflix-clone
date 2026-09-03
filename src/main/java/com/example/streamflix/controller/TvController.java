package com.example.streamflix.controller;

import com.example.streamflix.entity.Profile;
import com.example.streamflix.entity.TvShow;
import com.example.streamflix.service.CatalogService;
import com.example.streamflix.service.CurrentUserService;
import com.example.streamflix.service.MyListService;
import com.example.streamflix.service.RatingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TvController {

    private final CatalogService catalogService;
    private final CurrentUserService currentUserService;
    private final MyListService myListService;
    private final RatingService ratingService;

    public TvController(
            CatalogService catalogService,
            CurrentUserService currentUserService,
            MyListService myListService,
            RatingService ratingService
    ) {
        this.catalogService = catalogService;
        this.currentUserService = currentUserService;
        this.myListService = myListService;
        this.ratingService = ratingService;
    }

    @GetMapping("/tv")
    public String list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String sort,
            HttpSession session,
            Model model
    ) {
        Profile profile = currentUserService.requireProfile(session);
        var shows = catalogService.browseTvShows(q, genre, sort, PageRequest.of(page, size), profile);
        model.addAttribute("page", shows);
        model.addAttribute("genres", catalogService.allGenres());
        model.addAttribute("q", q);
        model.addAttribute("genre", genre);
        model.addAttribute("sort", sort);
        model.addAttribute("navActive", "tv");
        return "tv/list";
    }

    @GetMapping("/tv/{id}")
    public String details(
            @PathVariable Long id,
            @RequestParam(required = false) Integer season,
            HttpSession session,
            Model model
    ) {
        Profile profile = currentUserService.requireProfile(session);
        TvShow show = catalogService.requireTvShow(id);
        var seasons = show.getSeasons();
        int selected = season != null ? season : (seasons.isEmpty() ? 1 : seasons.getFirst().getSeasonNumber());
        var selectedSeason = seasons.stream()
                .filter(item -> item.getSeasonNumber() == selected)
                .findFirst()
                .orElse(seasons.isEmpty() ? null : seasons.getFirst());
        model.addAttribute("show", show);
        model.addAttribute("selectedSeason", selectedSeason);
        model.addAttribute("inMyList", myListService.isTvShowSaved(profile, id));
        model.addAttribute("averageRating", ratingService.averageTvRating(id));
        model.addAttribute("ratingCount", ratingService.tvRatingCount(id));
        model.addAttribute("userRating", ratingService.userTvRating(profile, id));
        model.addAttribute("navActive", "tv");
        return "tv/details";
    }
}
