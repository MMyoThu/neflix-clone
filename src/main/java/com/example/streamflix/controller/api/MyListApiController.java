package com.example.streamflix.controller.api;

import com.example.streamflix.entity.Profile;
import com.example.streamflix.service.CurrentUserService;
import com.example.streamflix.service.MyListService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/my-list")
public class MyListApiController {

    private final MyListService myListService;
    private final CurrentUserService currentUserService;

    public MyListApiController(MyListService myListService, CurrentUserService currentUserService) {
        this.myListService = myListService;
        this.currentUserService = currentUserService;
    }

    @PostMapping("/movie/{id}")
    public ResponseEntity<Map<String, Object>> addMovie(@PathVariable Long id, HttpSession session) {
        Profile profile = currentUserService.requireProfile(session);
        boolean added = myListService.addMovie(profile, id);
        return ResponseEntity.status(added ? HttpStatus.CREATED : HttpStatus.OK)
                .body(Map.of("saved", true));
    }

    @DeleteMapping("/movie/{id}")
    public ResponseEntity<Map<String, Object>> removeMovie(@PathVariable Long id, HttpSession session) {
        Profile profile = currentUserService.requireProfile(session);
        myListService.removeMovie(profile, id);
        return ResponseEntity.ok(Map.of("saved", false));
    }

    @PostMapping("/tv/{id}")
    public ResponseEntity<Map<String, Object>> addTv(@PathVariable Long id, HttpSession session) {
        Profile profile = currentUserService.requireProfile(session);
        boolean added = myListService.addTvShow(profile, id);
        return ResponseEntity.status(added ? HttpStatus.CREATED : HttpStatus.OK)
                .body(Map.of("saved", true));
    }

    @DeleteMapping("/tv/{id}")
    public ResponseEntity<Map<String, Object>> removeTv(@PathVariable Long id, HttpSession session) {
        Profile profile = currentUserService.requireProfile(session);
        myListService.removeTvShow(profile, id);
        return ResponseEntity.ok(Map.of("saved", false));
    }
}
