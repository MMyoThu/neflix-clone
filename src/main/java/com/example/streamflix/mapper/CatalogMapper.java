package com.example.streamflix.mapper;

import com.example.streamflix.dto.CatalogCard;
import com.example.streamflix.dto.HeroContent;
import com.example.streamflix.entity.ContentType;
import com.example.streamflix.entity.Episode;
import com.example.streamflix.entity.Movie;
import com.example.streamflix.entity.TvShow;
import com.example.streamflix.entity.WatchHistory;
import org.springframework.stereotype.Component;

@Component
public class CatalogMapper {

    public CatalogCard toCard(Movie movie, boolean inMyList, Integer progressPercent) {
        return new CatalogCard(
                movie.getId(),
                movie.getTitle(),
                movie.getPosterUrl(),
                movie.getReleaseYear(),
                movie.getDurationMinutes(),
                movie.getMaturityRating(),
                ContentType.MOVIE,
                inMyList,
                progressPercent,
                "/movies/" + movie.getId(),
                "/watch/movie/" + movie.getId()
        );
    }

    public CatalogCard toCard(TvShow show, boolean inMyList, Integer progressPercent) {
        return new CatalogCard(
                show.getId(),
                show.getTitle(),
                show.getPosterUrl(),
                show.getReleaseYear(),
                null,
                show.getMaturityRating(),
                ContentType.TV,
                inMyList,
                progressPercent,
                "/tv/" + show.getId(),
                "/tv/" + show.getId()
        );
    }

    public HeroContent toHero(Movie movie) {
        return new HeroContent(
                movie.getId(),
                movie.getTitle(),
                movie.getDescription(),
                movie.getReleaseYear(),
                movie.getMaturityRating(),
                movie.getDurationMinutes(),
                movie.getBackdropUrl(),
                ContentType.MOVIE,
                "/movies/" + movie.getId(),
                "/watch/movie/" + movie.getId()
        );
    }

    public HeroContent toHero(TvShow show) {
        return new HeroContent(
                show.getId(),
                show.getTitle(),
                show.getDescription(),
                show.getReleaseYear(),
                show.getMaturityRating(),
                null,
                show.getBackdropUrl(),
                ContentType.TV,
                "/tv/" + show.getId(),
                "/tv/" + show.getId()
        );
    }

    public CatalogCard fromHistory(WatchHistory history, boolean inMyList) {
        int percent = progressPercent(history);
        if (history.getMovie() != null) {
            return toCard(history.getMovie(), inMyList, percent);
        }
        Episode episode = history.getEpisode();
        TvShow show = episode.getSeason().getTvShow();
        return new CatalogCard(
                show.getId(),
                show.getTitle() + " · S" + episode.getSeason().getSeasonNumber() + "E" + episode.getEpisodeNumber(),
                show.getPosterUrl(),
                show.getReleaseYear(),
                episode.getDurationMinutes(),
                show.getMaturityRating(),
                ContentType.TV,
                inMyList,
                percent,
                "/tv/" + show.getId(),
                "/watch/episode/" + episode.getId()
        );
    }

    private int progressPercent(WatchHistory history) {
        int durationSeconds = 0;
        if (history.getMovie() != null && history.getMovie().getDurationMinutes() != null) {
            durationSeconds = history.getMovie().getDurationMinutes() * 60;
        } else if (history.getEpisode() != null && history.getEpisode().getDurationMinutes() != null) {
            durationSeconds = history.getEpisode().getDurationMinutes() * 60;
        }
        if (durationSeconds <= 0) {
            return 0;
        }
        int progress = history.getProgressSeconds() == null ? 0 : history.getProgressSeconds();
        return Math.min(100, Math.max(0, (int) Math.round(progress * 100.0 / durationSeconds)));
    }
}
