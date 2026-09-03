package com.example.streamflix.controller.api;

import com.example.streamflix.dto.RatingRequest;
import com.example.streamflix.entity.Profile;
import com.example.streamflix.service.CurrentUserService;
import com.example.streamflix.service.RatingService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/ratings")
public class RatingApiController {

    private final RatingService ratingService;
    private final CurrentUserService currentUserService;

    public RatingApiController(RatingService ratingService, CurrentUserService currentUserService) {
        this.ratingService = ratingService;
        this.currentUserService = currentUserService;
    }

    @PostMapping("/movie/{id}")
    public ResponseEntity<Map<String, Object>> movie(
            @PathVariable Long id,
            @Valid @RequestBody RatingRequest request,
            HttpSession session
    ) {
        Profile profile = currentUserService.requireProfile(session);
        ratingService.rateMovie(profile, id, request.rating());
        return ResponseEntity.ok(Map.of(
                "rating", request.rating(),
                "average", ratingService.averageMovieRating(id),
                "count", ratingService.movieRatingCount(id)
        ));
    }

    @PostMapping("/tv/{id}")
    public ResponseEntity<Map<String, Object>> tv(
            @PathVariable Long id,
            @Valid @RequestBody RatingRequest request,
            HttpSession session
    ) {
        Profile profile = currentUserService.requireProfile(session);
        ratingService.rateTvShow(profile, id, request.rating());
        return ResponseEntity.ok(Map.of(
                "rating", request.rating(),
                "average", ratingService.averageTvRating(id),
                "count", ratingService.tvRatingCount(id)
        ));
    }
}
