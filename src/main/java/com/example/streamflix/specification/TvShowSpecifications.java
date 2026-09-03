package com.example.streamflix.specification;

import com.example.streamflix.entity.TvShow;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public final class TvShowSpecifications {

    private TvShowSpecifications() {
    }

    public static Specification<TvShow> titleContains(String query) {
        return (root, q, cb) -> {
            if (!StringUtils.hasText(query)) {
                return cb.conjunction();
            }
            String like = "%" + query.trim().toLowerCase() + "%";
            return cb.like(cb.lower(root.get("title")), like);
        };
    }

    public static Specification<TvShow> hasGenre(String genre) {
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
