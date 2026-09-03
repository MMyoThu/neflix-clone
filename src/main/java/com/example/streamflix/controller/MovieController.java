package com.example.streamflix.controller;

import com.example.streamflix.entity.Movie;
import com.example.streamflix.entity.Profile;
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
public class MovieController {

    private final CatalogService catalogService;
    private final CurrentUserService currentUserService;
    private final MyListService myListService;
    private final RatingService ratingService;

    public MovieController(
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

    @GetMapping("/movies")
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
        var movies = catalogService.browseMovies(q, genre, sort, PageRequest.of(page, size), profile);
        model.addAttribute("page", movies);
        model.addAttribute("genres", catalogService.allGenres());
        model.addAttribute("q", q);
        model.addAttribute("genre", genre);
        model.addAttribute("sort", sort);
        model.addAttribute("navActive", "movies");
        return "movie/list";
    }

    @GetMapping("/movies/{id}")
    public String details(@PathVariable Long id, HttpSession session, Model model) {
        Profile profile = currentUserService.requireProfile(session);
        Movie movie = catalogService.requireMovie(id);
        model.addAttribute("movie", movie);
        model.addAttribute("inMyList", myListService.isMovieSaved(profile, id));
        model.addAttribute("related", catalogService.relatedMovies(movie, profile));
        model.addAttribute("averageRating", ratingService.averageMovieRating(id));
        model.addAttribute("ratingCount", ratingService.movieRatingCount(id));
        model.addAttribute("userRating", ratingService.userMovieRating(profile, id));
        model.addAttribute("navActive", "movies");
        return "movie/details";
    }
}
