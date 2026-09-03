package com.example.streamflix.controller.api;

import com.example.streamflix.service.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/search")
public class SearchApiController {

    private final SearchService searchService;

    public SearchApiController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/suggest")
    public ResponseEntity<Map<String, Object>> suggest(@RequestParam("q") String query) {
        return ResponseEntity.ok(Map.of("suggestions", searchService.suggestTitles(query)));
    }
}
