package com.example.streamflix.service.impl;

import com.example.streamflix.dto.CatalogCard;
import com.example.streamflix.dto.WatchProgressRequest;
import com.example.streamflix.entity.Episode;
import com.example.streamflix.entity.Movie;
import com.example.streamflix.entity.Profile;
import com.example.streamflix.entity.WatchHistory;
import com.example.streamflix.exception.ResourceNotFoundException;
import com.example.streamflix.mapper.CatalogMapper;
import com.example.streamflix.repository.EpisodeRepository;
import com.example.streamflix.repository.WatchHistoryRepository;
import com.example.streamflix.service.CatalogService;
import com.example.streamflix.service.WatchProgressService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class WatchProgressServiceImpl implements WatchProgressService {

    private final WatchHistoryRepository watchHistoryRepository;
    private final CatalogService catalogService;
    private final EpisodeRepository episodeRepository;
    private final CatalogMapper catalogMapper;

    public WatchProgressServiceImpl(
            WatchHistoryRepository watchHistoryRepository,
            CatalogService catalogService,
            EpisodeRepository episodeRepository,
            CatalogMapper catalogMapper
    ) {
        this.watchHistoryRepository = watchHistoryRepository;
        this.catalogService = catalogService;
        this.episodeRepository = episodeRepository;
        this.catalogMapper = catalogMapper;
    }

    @Override
    @Transactional
    public WatchHistory saveMovieProgress(Profile profile, Long movieId, WatchProgressRequest request) {
        Movie movie = catalogService.requireMovie(movieId);
        WatchHistory history = watchHistoryRepository.findByProfileAndMovie(profile, movie)
                .orElseGet(() -> {
                    WatchHistory created = new WatchHistory();
                    created.setProfile(profile);
                    created.setMovie(movie);
                    return created;
                });
        applyProgress(history, request);
        return watchHistoryRepository.save(history);
    }

    @Override
    @Transactional
    public WatchHistory saveEpisodeProgress(Profile profile, Long episodeId, WatchProgressRequest request) {
        Episode episode = requireEpisode(episodeId);
        WatchHistory history = watchHistoryRepository.findByProfileAndEpisode(profile, episode)
                .orElseGet(() -> {
                    WatchHistory created = new WatchHistory();
                    created.setProfile(profile);
                    created.setEpisode(episode);
                    return created;
                });
        applyProgress(history, request);
        return watchHistoryRepository.save(history);
    }

    @Override
    @Transactional(readOnly = true)
    public int movieProgressSeconds(Profile profile, Movie movie) {
        return watchHistoryRepository.findByProfileAndMovie(profile, movie)
                .map(WatchHistory::getProgressSeconds)
                .orElse(0);
    }

    @Override
    @Transactional(readOnly = true)
    public int episodeProgressSeconds(Profile profile, Episode episode) {
        return watchHistoryRepository.findByProfileAndEpisode(profile, episode)
                .map(WatchHistory::getProgressSeconds)
                .orElse(0);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CatalogCard> continueWatching(Profile profile) {
        return watchHistoryRepository.findByProfileAndCompletedFalseOrderByLastWatchedAtDesc(profile).stream()
                .map(history -> catalogMapper.fromHistory(history, false))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CatalogCard> history(Profile profile) {
        return watchHistoryRepository.findByProfileOrderByLastWatchedAtDesc(profile).stream()
                .map(history -> catalogMapper.fromHistory(history, false))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Episode requireEpisode(Long id) {
        return episodeRepository.findWithShowById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Episode not found"));
    }

    private void applyProgress(WatchHistory history, WatchProgressRequest request) {
        int progress = Math.max(0, request.progressSeconds());
        history.setProgressSeconds(progress);
        history.setLastWatchedAt(Instant.now());
        Integer duration = request.durationSeconds();
        boolean completed = duration != null && duration > 0 && progress >= Math.round(duration * 0.9f);
        history.setCompleted(completed);
    }
}
