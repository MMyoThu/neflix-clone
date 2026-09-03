package com.example.streamflix.repository;

import com.example.streamflix.entity.Movie;
import com.example.streamflix.entity.MyListItem;
import com.example.streamflix.entity.Profile;
import com.example.streamflix.entity.TvShow;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MyListRepository extends JpaRepository<MyListItem, Long> {

    @EntityGraph(attributePaths = {"movie", "tvShow"})
    List<MyListItem> findByProfileOrderByCreatedAtDesc(Profile profile);

    boolean existsByProfileAndMovie(Profile profile, Movie movie);

    boolean existsByProfileAndTvShow(Profile profile, TvShow tvShow);

    Optional<MyListItem> findByProfileAndMovie(Profile profile, Movie movie);

    Optional<MyListItem> findByProfileAndTvShow(Profile profile, TvShow tvShow);
}
