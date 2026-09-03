package com.example.streamflix.service.impl;

import com.example.streamflix.dto.CatalogCard;
import com.example.streamflix.entity.Movie;
import com.example.streamflix.entity.MyListItem;
import com.example.streamflix.entity.Profile;
import com.example.streamflix.entity.TvShow;
import com.example.streamflix.mapper.CatalogMapper;
import com.example.streamflix.repository.MovieRepository;
import com.example.streamflix.repository.MyListRepository;
import com.example.streamflix.repository.TvShowRepository;
import com.example.streamflix.service.CatalogService;
import com.example.streamflix.service.MyListService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MyListServiceImpl implements MyListService {

    private final MyListRepository myListRepository;
    private final CatalogService catalogService;
    private final CatalogMapper catalogMapper;
    private final MovieRepository movieRepository;
    private final TvShowRepository tvShowRepository;

    public MyListServiceImpl(
            MyListRepository myListRepository,
            CatalogService catalogService,
            CatalogMapper catalogMapper,
            MovieRepository movieRepository,
            TvShowRepository tvShowRepository
    ) {
        this.myListRepository = myListRepository;
        this.catalogService = catalogService;
        this.catalogMapper = catalogMapper;
        this.movieRepository = movieRepository;
        this.tvShowRepository = tvShowRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CatalogCard> list(Profile profile) {
        return myListRepository.findByProfileOrderByCreatedAtDesc(profile).stream()
                .map(item -> item.getMovie() != null
                        ? catalogMapper.toCard(item.getMovie(), true, null)
                        : catalogMapper.toCard(item.getTvShow(), true, null))
                .toList();
    }

    @Override
    @Transactional
    public boolean addMovie(Profile profile, Long movieId) {
        Movie movie = catalogService.requireMovie(movieId);
        if (myListRepository.existsByProfileAndMovie(profile, movie)) {
            return false;
        }
        MyListItem item = new MyListItem();
        item.setProfile(profile);
        item.setMovie(movie);
        myListRepository.save(item);
        return true;
    }

    @Override
    @Transactional
    public boolean removeMovie(Profile profile, Long movieId) {
        Movie movie = movieRepository.findById(movieId).orElse(null);
        if (movie == null) {
            return false;
        }
        return myListRepository.findByProfileAndMovie(profile, movie)
                .map(item -> {
                    myListRepository.delete(item);
                    return true;
                })
                .orElse(false);
    }

    @Override
    @Transactional
    public boolean addTvShow(Profile profile, Long tvShowId) {
        TvShow show = catalogService.requireTvShow(tvShowId);
        if (myListRepository.existsByProfileAndTvShow(profile, show)) {
            return false;
        }
        MyListItem item = new MyListItem();
        item.setProfile(profile);
        item.setTvShow(show);
        myListRepository.save(item);
        return true;
    }

    @Override
    @Transactional
    public boolean removeTvShow(Profile profile, Long tvShowId) {
        TvShow show = tvShowRepository.findById(tvShowId).orElse(null);
        if (show == null) {
            return false;
        }
        return myListRepository.findByProfileAndTvShow(profile, show)
                .map(item -> {
                    myListRepository.delete(item);
                    return true;
                })
                .orElse(false);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isMovieSaved(Profile profile, Long movieId) {
        return movieRepository.findById(movieId)
                .map(movie -> myListRepository.existsByProfileAndMovie(profile, movie))
                .orElse(false);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isTvShowSaved(Profile profile, Long tvShowId) {
        return tvShowRepository.findById(tvShowId)
                .map(show -> myListRepository.existsByProfileAndTvShow(profile, show))
                .orElse(false);
    }
}
