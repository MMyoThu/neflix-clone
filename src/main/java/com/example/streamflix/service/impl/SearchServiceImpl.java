package com.example.streamflix.service.impl;

import com.example.streamflix.dto.CatalogCard;
import com.example.streamflix.entity.Profile;
import com.example.streamflix.mapper.CatalogMapper;
import com.example.streamflix.repository.MovieRepository;
import com.example.streamflix.repository.TvShowRepository;
import com.example.streamflix.service.MyListService;
import com.example.streamflix.service.SearchService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    private final MovieRepository movieRepository;
    private final TvShowRepository tvShowRepository;
    private final CatalogMapper catalogMapper;
    private final MyListService myListService;

    public SearchServiceImpl(
            MovieRepository movieRepository,
            TvShowRepository tvShowRepository,
            CatalogMapper catalogMapper,
            MyListService myListService
    ) {
        this.movieRepository = movieRepository;
        this.tvShowRepository = tvShowRepository;
        this.catalogMapper = catalogMapper;
        this.myListService = myListService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CatalogCard> searchMovies(String query, Profile profile) {
        if (!StringUtils.hasText(query)) {
            return List.of();
        }
        return movieRepository.search(query.trim(), PageRequest.of(0, 24)).stream()
                .map(movie -> catalogMapper.toCard(movie, myListService.isMovieSaved(profile, movie.getId()), null))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CatalogCard> searchTvShows(String query, Profile profile) {
        if (!StringUtils.hasText(query)) {
            return List.of();
        }
        return tvShowRepository.search(query.trim(), PageRequest.of(0, 24)).stream()
                .map(show -> catalogMapper.toCard(show, myListService.isTvShowSaved(profile, show.getId()), null))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> suggestTitles(String query) {
        if (!StringUtils.hasText(query) || query.trim().length() < 2) {
            return List.of();
        }
        List<String> titles = new ArrayList<>();
        movieRepository.search(query.trim(), PageRequest.of(0, 5))
                .forEach(movie -> titles.add(movie.getTitle()));
        tvShowRepository.search(query.trim(), PageRequest.of(0, 5))
                .forEach(show -> titles.add(show.getTitle()));
        return titles.stream().distinct().limit(8).toList();
    }
}
