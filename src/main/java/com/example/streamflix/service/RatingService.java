package com.example.streamflix.service;

import com.example.streamflix.entity.Profile;

public interface RatingService {

    void rateMovie(Profile profile, Long movieId, int rating);

    void rateTvShow(Profile profile, Long tvShowId, int rating);

    Short userMovieRating(Profile profile, Long movieId);

    Short userTvRating(Profile profile, Long tvShowId);

    Double averageMovieRating(Long movieId);

    long movieRatingCount(Long movieId);

    Double averageTvRating(Long tvShowId);

    long tvRatingCount(Long tvShowId);
}
