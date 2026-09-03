package com.example.streamflix.service.impl;

import com.example.streamflix.entity.Movie;
import com.example.streamflix.entity.Profile;
import com.example.streamflix.entity.Rating;
import com.example.streamflix.entity.TvShow;
import com.example.streamflix.repository.RatingRepository;
import com.example.streamflix.service.CatalogService;
import com.example.streamflix.service.RatingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final CatalogService catalogService;

    public RatingServiceImpl(RatingRepository ratingRepository, CatalogService catalogService) {
        this.ratingRepository = ratingRepository;
        this.catalogService = catalogService;
    }

    @Override
    @Transactional
    public void rateMovie(Profile profile, Long movieId, int ratingValue) {
        Movie movie = catalogService.requireMovie(movieId);
        Rating rating = ratingRepository.findByProfileAndMovie(profile, movie).orElseGet(Rating::new);
        rating.setProfile(profile);
        rating.setMovie(movie);
        rating.setRating((short) ratingValue);
        ratingRepository.save(rating);
    }

    @Override
    @Transactional
    public void rateTvShow(Profile profile, Long tvShowId, int ratingValue) {
        TvShow show = catalogService.requireTvShow(tvShowId);
        Rating rating = ratingRepository.findByProfileAndTvShow(profile, show).orElseGet(Rating::new);
        rating.setProfile(profile);
        rating.setTvShow(show);
        rating.setRating((short) ratingValue);
        ratingRepository.save(rating);
    }

    @Override
    @Transactional(readOnly = true)
    public Short userMovieRating(Profile profile, Long movieId) {
        Movie movie = catalogService.requireMovie(movieId);
        return ratingRepository.findByProfileAndMovie(profile, movie)
                .map(Rating::getRating)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Short userTvRating(Profile profile, Long tvShowId) {
        return ratingRepository.findByProfileAndTvShow(profile, catalogService.requireTvShow(tvShowId))
                .map(Rating::getRating)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Double averageMovieRating(Long movieId) {
        return ratingRepository.averageForMovie(movieId);
    }

    @Override
    @Transactional(readOnly = true)
    public long movieRatingCount(Long movieId) {
        return ratingRepository.countForMovie(movieId);
    }

    @Override
    @Transactional(readOnly = true)
    public Double averageTvRating(Long tvShowId) {
        return ratingRepository.averageForTvShow(tvShowId);
    }

    @Override
    @Transactional(readOnly = true)
    public long tvRatingCount(Long tvShowId) {
        return ratingRepository.countForTvShow(tvShowId);
    }
}
