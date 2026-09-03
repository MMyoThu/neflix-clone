package com.example.streamflix.dto;

import com.example.streamflix.entity.ContentType;

public record CatalogCard(
        Long id,
        String title,
        String posterUrl,
        Integer releaseYear,
        Integer durationMinutes,
        String maturityRating,
        ContentType type,
        boolean inMyList,
        Integer progressPercent,
        String detailsHref,
        String watchHref
) {
}
