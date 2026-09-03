package com.example.streamflix.repository;

import com.example.streamflix.entity.TvShow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TvShowRepository extends JpaRepository<TvShow, Long>, JpaSpecificationExecutor<TvShow> {

    Optional<TvShow> findFirstByFeaturedTrueOrderByIdAsc();

    List<TvShow> findTop12ByOrderByReleaseYearDescIdDesc();

    List<TvShow> findTop12ByOrderByIdDesc();

    @Query("""
            SELECT DISTINCT t FROM TvShow t
            JOIN t.genres g
            WHERE g.name = :genre
            ORDER BY t.releaseYear DESC, t.id DESC
            """)
    List<TvShow> findTop12ByGenreName(@Param("genre") String genre);

    @EntityGraph(attributePaths = {"genres", "seasons"})
    @Query("SELECT t FROM TvShow t WHERE t.id = :id")
    Optional<TvShow> findWithDetailsById(@Param("id") Long id);

    @Query("""
            SELECT DISTINCT t FROM TvShow t
            WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :query, '%'))
               OR LOWER(t.description) LIKE LOWER(CONCAT('%', :query, '%'))
            """)
    Page<TvShow> search(@Param("query") String query, Pageable pageable);
}
