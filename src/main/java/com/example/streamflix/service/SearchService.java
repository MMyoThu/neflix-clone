package com.example.streamflix.service;

import com.example.streamflix.dto.CatalogCard;
import com.example.streamflix.entity.Profile;

import java.util.List;

public interface SearchService {

    List<CatalogCard> searchMovies(String query, Profile profile);

    List<CatalogCard> searchTvShows(String query, Profile profile);

    List<String> suggestTitles(String query);
}
