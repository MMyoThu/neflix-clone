package com.example.streamflix.repository;

import com.example.streamflix.entity.Episode;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EpisodeRepository extends JpaRepository<Episode, Long> {

    @EntityGraph(attributePaths = {"season", "season.tvShow"})
    @Query("SELECT e FROM Episode e WHERE e.id = :id")
    Optional<Episode> findWithShowById(@Param("id") Long id);
}
