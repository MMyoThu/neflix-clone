package com.example.streamflix.service;

import com.example.streamflix.dto.CatalogCard;
import com.example.streamflix.entity.Profile;

import java.util.List;

public interface MyListService {

    List<CatalogCard> list(Profile profile);

    boolean addMovie(Profile profile, Long movieId);

    boolean removeMovie(Profile profile, Long movieId);

    boolean addTvShow(Profile profile, Long tvShowId);

    boolean removeTvShow(Profile profile, Long tvShowId);

    boolean isMovieSaved(Profile profile, Long movieId);

    boolean isTvShowSaved(Profile profile, Long tvShowId);
}
