package com.example.streamflix.repository;

import com.example.streamflix.entity.Episode;
import com.example.streamflix.entity.Movie;
import com.example.streamflix.entity.Profile;
import com.example.streamflix.entity.WatchHistory;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WatchHistoryRepository extends JpaRepository<WatchHistory, Long> {

    @EntityGraph(attributePaths = {"movie", "episode", "episode.season", "episode.season.tvShow"})
    List<WatchHistory> findByProfileAndCompletedFalseOrderByLastWatchedAtDesc(Profile profile);

    @EntityGraph(attributePaths = {"movie", "episode", "episode.season", "episode.season.tvShow"})
    List<WatchHistory> findByProfileOrderByLastWatchedAtDesc(Profile profile);

    Optional<WatchHistory> findByProfileAndMovie(Profile profile, Movie movie);

    Optional<WatchHistory> findByProfileAndEpisode(Profile profile, Episode episode);
}
