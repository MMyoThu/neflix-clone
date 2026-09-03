package com.example.streamflix.repository;

import com.example.streamflix.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {

    Optional<Movie> findFirstByFeaturedTrueOrderByIdAsc();

    List<Movie> findTop12ByOrderByReleaseYearDescIdDesc();

    List<Movie> findTop12ByOrderByIdDesc();

    @Query("""
            SELECT DISTINCT m FROM Movie m
            JOIN m.genres g
            WHERE g.name = :genre
            ORDER BY m.releaseYear DESC, m.id DESC
            """)
    List<Movie> findTop12ByGenreName(@Param("genre") String genre);

    @EntityGraph(attributePaths = "genres")
    @Query("SELECT m FROM Movie m WHERE m.id = :id")
    Optional<Movie> findWithGenresById(@Param("id") Long id);

    @Query("""
            SELECT DISTINCT m FROM Movie m
            JOIN m.genres g
            WHERE g.id IN :genreIds AND m.id <> :movieId
            ORDER BY m.releaseYear DESC
            """)
    List<Movie> findRelatedByGenreIds(@Param("genreIds") Collection<Long> genreIds, @Param("movieId") Long movieId, Pageable pageable);

    @EntityGraph(attributePaths = "genres")
    Page<Movie> findAll(Pageable pageable);

    @Query("""
            SELECT DISTINCT m FROM Movie m
            WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :query, '%'))
               OR LOWER(m.description) LIKE LOWER(CONCAT('%', :query, '%'))
            """)
    Page<Movie> search(@Param("query") String query, Pageable pageable);
}
