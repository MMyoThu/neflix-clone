package com.example.streamflix.service.impl;

import com.example.streamflix.dto.CatalogCard;
import com.example.streamflix.dto.ContentRow;
import com.example.streamflix.dto.HeroContent;
import com.example.streamflix.entity.Genre;
import com.example.streamflix.entity.Movie;
import com.example.streamflix.entity.MyListItem;
import com.example.streamflix.entity.Profile;
import com.example.streamflix.entity.TvShow;
import com.example.streamflix.exception.ResourceNotFoundException;
import com.example.streamflix.mapper.CatalogMapper;
import com.example.streamflix.repository.GenreRepository;
import com.example.streamflix.repository.MovieRepository;
import com.example.streamflix.repository.MyListRepository;
import com.example.streamflix.repository.TvShowRepository;
import com.example.streamflix.repository.WatchHistoryRepository;
import com.example.streamflix.service.CatalogService;
import com.example.streamflix.specification.MovieSpecifications;
import com.example.streamflix.specification.TvShowSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CatalogServiceImpl implements CatalogService {

    private final MovieRepository movieRepository;
    private final TvShowRepository tvShowRepository;
    private final GenreRepository genreRepository;
    private final MyListRepository myListRepository;
    private final WatchHistoryRepository watchHistoryRepository;
    private final CatalogMapper catalogMapper;

    public CatalogServiceImpl(
            MovieRepository movieRepository,
            TvShowRepository tvShowRepository,
            GenreRepository genreRepository,
            MyListRepository myListRepository,
            WatchHistoryRepository watchHistoryRepository,
            CatalogMapper catalogMapper
    ) {
        this.movieRepository = movieRepository;
        this.tvShowRepository = tvShowRepository;
        this.genreRepository = genreRepository;
        this.myListRepository = myListRepository;
        this.watchHistoryRepository = watchHistoryRepository;
        this.catalogMapper = catalogMapper;
    }

    @Override
    public HeroContent featuredHero() {
        return movieRepository.findFirstByFeaturedTrueOrderByIdAsc()
                .map(catalogMapper::toHero)
                .or(() -> tvShowRepository.findFirstByFeaturedTrueOrderByIdAsc().map(catalogMapper::toHero))
                .orElseGet(() -> movieRepository.findAll(PageRequest.of(0, 1)).stream()
                        .findFirst()
                        .map(catalogMapper::toHero)
                        .orElse(null));
    }

    @Override
    public List<ContentRow> homeRows(Profile profile) {
        Set<Long> movieListIds = movieIdsOnList(profile);
        Set<Long> tvListIds = tvIdsOnList(profile);
        List<ContentRow> rows = new ArrayList<>();

        var continueWatching = watchHistoryRepository
                .findByProfileAndCompletedFalseOrderByLastWatchedAtDesc(profile)
                .stream()
                .limit(12)
                .map(history -> catalogMapper.fromHistory(history, false))
                .toList();
        if (!continueWatching.isEmpty()) {
            rows.add(new ContentRow("Continue Watching", continueWatching));
        }

        var myList = myListRepository.findByProfileOrderByCreatedAtDesc(profile).stream()
                .map(item -> toCard(item, true))
                .toList();
        if (!myList.isEmpty()) {
            rows.add(new ContentRow("My List", myList));
        }

        rows.add(new ContentRow("Trending Now", moviesToCards(movieRepository.findTop12ByOrderByIdDesc(), movieListIds)));
        rows.add(new ContentRow("Popular Movies", moviesToCards(movieRepository.findTop12ByOrderByReleaseYearDescIdDesc(), movieListIds)));
        rows.add(new ContentRow("Popular TV Shows", tvToCards(tvShowRepository.findTop12ByOrderByReleaseYearDescIdDesc(), tvListIds)));
        rows.add(new ContentRow("New Releases", moviesToCards(movieRepository.findTop12ByOrderByReleaseYearDescIdDesc(), movieListIds)));
        addGenreRow(rows, "Action", movieListIds);
        addGenreRow(rows, "Comedy", movieListIds);
        addGenreRow(rows, "Drama", movieListIds);
        rows.add(new ContentRow("Recommended For You", moviesToCards(movieRepository.findTop12ByOrderByIdDesc(), movieListIds)));
        return rows.stream().filter(row -> row.items() != null && !row.items().isEmpty()).toList();
    }

    @Override
    public Page<CatalogCard> browseMovies(String query, String genre, String sort, Pageable pageable, Profile profile) {
        Specification<Movie> spec = MovieSpecifications.titleContains(query)
                .and(MovieSpecifications.hasGenre(genre));
        Pageable sorted = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortForMovies(sort));
        Set<Long> listIds = movieIdsOnList(profile);
        return movieRepository.findAll(spec, sorted)
                .map(movie -> catalogMapper.toCard(movie, listIds.contains(movie.getId()), null));
    }

    @Override
    public Page<CatalogCard> browseTvShows(String query, String genre, String sort, Pageable pageable, Profile profile) {
        Specification<TvShow> spec = TvShowSpecifications.titleContains(query)
                .and(TvShowSpecifications.hasGenre(genre));
        Pageable sorted = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortForTv(sort));
        Set<Long> listIds = tvIdsOnList(profile);
        return tvShowRepository.findAll(spec, sorted)
                .map(show -> catalogMapper.toCard(show, listIds.contains(show.getId()), null));
    }

    @Override
    public Movie requireMovie(Long id) {
        return movieRepository.findWithGenresById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));
    }

    @Override
    public TvShow requireTvShow(Long id) {
        TvShow show = tvShowRepository.findWithDetailsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TV show not found"));
        show.getSeasons().forEach(season -> season.getEpisodes().size());
        return show;
    }

    @Override
    public List<CatalogCard> relatedMovies(Movie movie, Profile profile) {
        var genreIds = movie.getGenres().stream().map(Genre::getId).toList();
        if (genreIds.isEmpty()) {
            return List.of();
        }
        Set<Long> listIds = movieIdsOnList(profile);
        return movieRepository.findRelatedByGenreIds(genreIds, movie.getId(), PageRequest.of(0, 8)).stream()
                .map(related -> catalogMapper.toCard(related, listIds.contains(related.getId()), null))
                .toList();
    }

    @Override
    public List<Genre> allGenres() {
        return genreRepository.findAllByOrderByNameAsc();
    }

    private void addGenreRow(List<ContentRow> rows, String genre, Set<Long> movieListIds) {
        var items = moviesToCards(movieRepository.findTop12ByGenreName(genre), movieListIds);
        if (!items.isEmpty()) {
            rows.add(new ContentRow(genre, items));
        }
    }

    private List<CatalogCard> moviesToCards(List<Movie> movies, Set<Long> listIds) {
        return movies.stream()
                .map(movie -> catalogMapper.toCard(movie, listIds.contains(movie.getId()), null))
                .toList();
    }

    private List<CatalogCard> tvToCards(List<TvShow> shows, Set<Long> listIds) {
        return shows.stream()
                .map(show -> catalogMapper.toCard(show, listIds.contains(show.getId()), null))
                .toList();
    }

    private CatalogCard toCard(MyListItem item, boolean inMyList) {
        if (item.getMovie() != null) {
            return catalogMapper.toCard(item.getMovie(), inMyList, null);
        }
        return catalogMapper.toCard(item.getTvShow(), inMyList, null);
    }

    private Set<Long> movieIdsOnList(Profile profile) {
        return myListRepository.findByProfileOrderByCreatedAtDesc(profile).stream()
                .filter(item -> item.getMovie() != null)
                .map(item -> item.getMovie().getId())
                .collect(Collectors.toSet());
    }

    private Set<Long> tvIdsOnList(Profile profile) {
        return myListRepository.findByProfileOrderByCreatedAtDesc(profile).stream()
                .filter(item -> item.getTvShow() != null)
                .map(item -> item.getTvShow().getId())
                .collect(Collectors.toSet());
    }

    private Sort sortForMovies(String sort) {
        if ("title".equalsIgnoreCase(sort)) {
            return Sort.by(Sort.Direction.ASC, "title");
        }
        if ("year".equalsIgnoreCase(sort)) {
            return Sort.by(Sort.Direction.DESC, "releaseYear");
        }
        return Sort.by(Sort.Direction.DESC, "releaseYear", "id");
    }

    private Sort sortForTv(String sort) {
        if ("title".equalsIgnoreCase(sort)) {
            return Sort.by(Sort.Direction.ASC, "title");
        }
        return Sort.by(Sort.Direction.DESC, "releaseYear", "id");
    }
}
