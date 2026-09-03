package com.example.streamflix.service;

import com.example.streamflix.dto.CatalogCard;
import com.example.streamflix.dto.ContentRow;
import com.example.streamflix.dto.HeroContent;
import com.example.streamflix.entity.Genre;
import com.example.streamflix.entity.Movie;
import com.example.streamflix.entity.Profile;
import com.example.streamflix.entity.TvShow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CatalogService {

    HeroContent featuredHero();

    List<ContentRow> homeRows(Profile profile);

    Page<CatalogCard> browseMovies(String query, String genre, String sort, Pageable pageable, Profile profile);

    Page<CatalogCard> browseTvShows(String query, String genre, String sort, Pageable pageable, Profile profile);

    Movie requireMovie(Long id);

    TvShow requireTvShow(Long id);

    List<CatalogCard> relatedMovies(Movie movie, Profile profile);

    List<Genre> allGenres();
}
