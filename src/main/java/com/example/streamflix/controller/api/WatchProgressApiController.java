package com.example.streamflix.controller.api;

import com.example.streamflix.dto.WatchProgressRequest;
import com.example.streamflix.entity.Profile;
import com.example.streamflix.service.CurrentUserService;
import com.example.streamflix.service.WatchProgressService;
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
@RequestMapping("/api/watch-progress")
public class WatchProgressApiController {

    private final WatchProgressService watchProgressService;
    private final CurrentUserService currentUserService;

    public WatchProgressApiController(
            WatchProgressService watchProgressService,
            CurrentUserService currentUserService
    ) {
        this.watchProgressService = watchProgressService;
        this.currentUserService = currentUserService;
    }

    @PostMapping("/movie/{id}")
    public ResponseEntity<Map<String, Object>> movie(
            @PathVariable Long id,
            @Valid @RequestBody WatchProgressRequest request,
            HttpSession session
    ) {
        Profile profile = currentUserService.requireProfile(session);
        var history = watchProgressService.saveMovieProgress(profile, id, request);
        return ResponseEntity.ok(Map.of(
                "progressSeconds", history.getProgressSeconds(),
                "completed", history.isCompleted()
        ));
    }

    @PostMapping("/episode/{id}")
    public ResponseEntity<Map<String, Object>> episode(
            @PathVariable Long id,
            @Valid @RequestBody WatchProgressRequest request,
            HttpSession session
    ) {
        Profile profile = currentUserService.requireProfile(session);
        var history = watchProgressService.saveEpisodeProgress(profile, id, request);
        return ResponseEntity.ok(Map.of(
                "progressSeconds", history.getProgressSeconds(),
                "completed", history.isCompleted()
        ));
    }
}
