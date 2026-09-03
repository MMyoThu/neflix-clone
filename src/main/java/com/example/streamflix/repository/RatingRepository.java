package com.example.streamflix.repository;

import com.example.streamflix.entity.Movie;
import com.example.streamflix.entity.Profile;
import com.example.streamflix.entity.Rating;
import com.example.streamflix.entity.TvShow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    Optional<Rating> findByProfileAndMovie(Profile profile, Movie movie);

    Optional<Rating> findByProfileAndTvShow(Profile profile, TvShow tvShow);

    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.movie.id = :movieId")
    Double averageForMovie(@Param("movieId") Long movieId);

    @Query("SELECT COUNT(r) FROM Rating r WHERE r.movie.id = :movieId")
    long countForMovie(@Param("movieId") Long movieId);

    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.tvShow.id = :tvShowId")
    Double averageForTvShow(@Param("tvShowId") Long tvShowId);

    @Query("SELECT COUNT(r) FROM Rating r WHERE r.tvShow.id = :tvShowId")
    long countForTvShow(@Param("tvShowId") Long tvShowId);
}
