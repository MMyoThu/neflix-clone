package com.example.streamflix.dto;

import com.example.streamflix.entity.ContentType;

public record HeroContent(
        Long id,
        String title,
        String description,
        Integer releaseYear,
        String maturityRating,
        Integer durationMinutes,
        String backdropUrl,
        ContentType type,
        String detailsHref,
        String watchHref
) {
}
