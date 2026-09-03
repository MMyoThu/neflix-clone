package com.example.streamflix.service;

import com.example.streamflix.dto.CatalogCard;
import com.example.streamflix.dto.WatchProgressRequest;
import com.example.streamflix.entity.Episode;
import com.example.streamflix.entity.Movie;
import com.example.streamflix.entity.Profile;
import com.example.streamflix.entity.WatchHistory;

import java.util.List;

public interface WatchProgressService {

    WatchHistory saveMovieProgress(Profile profile, Long movieId, WatchProgressRequest request);

    WatchHistory saveEpisodeProgress(Profile profile, Long episodeId, WatchProgressRequest request);

    int movieProgressSeconds(Profile profile, Movie movie);

    int episodeProgressSeconds(Profile profile, Episode episode);

    List<CatalogCard> continueWatching(Profile profile);

    List<CatalogCard> history(Profile profile);

    Episode requireEpisode(Long id);
}
