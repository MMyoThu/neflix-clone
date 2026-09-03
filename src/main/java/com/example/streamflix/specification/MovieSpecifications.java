package com.example.streamflix.specification;

import com.example.streamflix.entity.Movie;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public final class MovieSpecifications {

    private MovieSpecifications() {
    }

    public static Specification<Movie> titleContains(String query) {
        return (root, q, cb) -> {
            if (!StringUtils.hasText(query)) {
                return cb.conjunction();
            }
            String like = "%" + query.trim().toLowerCase() + "%";
            return cb.like(cb.lower(root.get("title")), like);
        };
    }

    public static Specification<Movie> hasGenre(String genre) {
        return (root, q, cb) -> {
            if (!StringUtils.hasText(genre)) {
                return cb.conjunction();
            }
            var genres = root.join("genres", JoinType.INNER);
            q.distinct(true);
            return cb.equal(cb.lower(genres.get("name")), genre.trim().toLowerCase());
        };
    }
}
