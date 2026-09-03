-- StreamFlix initial schema.
-- Seed data is added in a later migration (Phase 3).

CREATE TABLE users (
    id              BIGSERIAL PRIMARY KEY,
    email           VARCHAR(255) NOT NULL,
    password        VARCHAR(255) NOT NULL,
    first_name      VARCHAR(100) NOT NULL,
    last_name       VARCHAR(100) NOT NULL,
    role            VARCHAR(20)  NOT NULL,
    enabled         BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_users_email UNIQUE (email),
    CONSTRAINT chk_users_role CHECK (role IN ('USER', 'ADMIN'))
);

CREATE TABLE profiles (
    id            BIGSERIAL PRIMARY KEY,
    user_id       BIGINT       NOT NULL,
    profile_name  VARCHAR(100) NOT NULL,
    avatar_url    VARCHAR(500),
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_profiles_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_profiles_user_id ON profiles (user_id);

CREATE TABLE movies (
    id                BIGSERIAL PRIMARY KEY,
    title             VARCHAR(255)  NOT NULL,
    description       TEXT          NOT NULL,
    release_year      INTEGER       NOT NULL,
    duration_minutes  INTEGER       NOT NULL,
    maturity_rating   VARCHAR(20)   NOT NULL,
    poster_url        VARCHAR(1000),
    backdrop_url      VARCHAR(1000),
    trailer_url       VARCHAR(1000),
    video_url         VARCHAR(1000),
    featured          BOOLEAN       NOT NULL DEFAULT FALSE,
    created_at        TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at        TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_movies_release_year CHECK (release_year >= 1888),
    CONSTRAINT chk_movies_duration CHECK (duration_minutes > 0)
);

CREATE INDEX idx_movies_title ON movies (title);
CREATE INDEX idx_movies_featured ON movies (featured);
CREATE INDEX idx_movies_release_year ON movies (release_year);

CREATE TABLE tv_shows (
    id               BIGSERIAL PRIMARY KEY,
    title            VARCHAR(255)  NOT NULL,
    description      TEXT          NOT NULL,
    release_year     INTEGER       NOT NULL,
    maturity_rating  VARCHAR(20)   NOT NULL,
    poster_url       VARCHAR(1000),
    backdrop_url     VARCHAR(1000),
    trailer_url      VARCHAR(1000),
    featured         BOOLEAN       NOT NULL DEFAULT FALSE,
    created_at       TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_tv_shows_release_year CHECK (release_year >= 1888)
);

CREATE INDEX idx_tv_shows_title ON tv_shows (title);
CREATE INDEX idx_tv_shows_featured ON tv_shows (featured);
CREATE INDEX idx_tv_shows_release_year ON tv_shows (release_year);

CREATE TABLE seasons (
    id             BIGSERIAL PRIMARY KEY,
    tv_show_id     BIGINT       NOT NULL,
    season_number  INTEGER      NOT NULL,
    title          VARCHAR(255) NOT NULL,
    CONSTRAINT fk_seasons_tv_show FOREIGN KEY (tv_show_id) REFERENCES tv_shows (id) ON DELETE CASCADE,
    CONSTRAINT uq_seasons_show_number UNIQUE (tv_show_id, season_number),
    CONSTRAINT chk_seasons_number CHECK (season_number > 0)
);

CREATE INDEX idx_seasons_tv_show_id ON seasons (tv_show_id);

CREATE TABLE episodes (
    id                BIGSERIAL PRIMARY KEY,
    season_id         BIGINT       NOT NULL,
    episode_number    INTEGER      NOT NULL,
    title             VARCHAR(255) NOT NULL,
    description       TEXT         NOT NULL,
    duration_minutes  INTEGER      NOT NULL,
    thumbnail_url     VARCHAR(1000),
    video_url         VARCHAR(1000),
    release_date      DATE,
    CONSTRAINT fk_episodes_season FOREIGN KEY (season_id) REFERENCES seasons (id) ON DELETE CASCADE,
    CONSTRAINT uq_episodes_season_number UNIQUE (season_id, episode_number),
    CONSTRAINT chk_episodes_number CHECK (episode_number > 0),
    CONSTRAINT chk_episodes_duration CHECK (duration_minutes > 0)
);

CREATE INDEX idx_episodes_season_id ON episodes (season_id);

CREATE TABLE genres (
    id    BIGSERIAL PRIMARY KEY,
    name  VARCHAR(100) NOT NULL,
    CONSTRAINT uq_genres_name UNIQUE (name)
);

CREATE TABLE movie_genres (
    movie_id  BIGINT NOT NULL,
    genre_id  BIGINT NOT NULL,
    PRIMARY KEY (movie_id, genre_id),
    CONSTRAINT fk_movie_genres_movie FOREIGN KEY (movie_id) REFERENCES movies (id) ON DELETE CASCADE,
    CONSTRAINT fk_movie_genres_genre FOREIGN KEY (genre_id) REFERENCES genres (id) ON DELETE RESTRICT
);

CREATE INDEX idx_movie_genres_genre_id ON movie_genres (genre_id);

CREATE TABLE tv_show_genres (
    tv_show_id  BIGINT NOT NULL,
    genre_id    BIGINT NOT NULL,
    PRIMARY KEY (tv_show_id, genre_id),
    CONSTRAINT fk_tv_show_genres_show FOREIGN KEY (tv_show_id) REFERENCES tv_shows (id) ON DELETE CASCADE,
    CONSTRAINT fk_tv_show_genres_genre FOREIGN KEY (genre_id) REFERENCES genres (id) ON DELETE RESTRICT
);

CREATE INDEX idx_tv_show_genres_genre_id ON tv_show_genres (genre_id);

CREATE TABLE watch_history (
    id                BIGSERIAL PRIMARY KEY,
    profile_id        BIGINT      NOT NULL,
    movie_id          BIGINT,
    episode_id        BIGINT,
    progress_seconds  INTEGER     NOT NULL DEFAULT 0,
    completed         BOOLEAN     NOT NULL DEFAULT FALSE,
    last_watched_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_watch_history_profile FOREIGN KEY (profile_id) REFERENCES profiles (id) ON DELETE CASCADE,
    CONSTRAINT fk_watch_history_movie FOREIGN KEY (movie_id) REFERENCES movies (id) ON DELETE CASCADE,
    CONSTRAINT fk_watch_history_episode FOREIGN KEY (episode_id) REFERENCES episodes (id) ON DELETE CASCADE,
    CONSTRAINT chk_watch_history_target CHECK (
        (movie_id IS NOT NULL AND episode_id IS NULL)
        OR (movie_id IS NULL AND episode_id IS NOT NULL)
    ),
    CONSTRAINT chk_watch_history_progress CHECK (progress_seconds >= 0)
);

CREATE UNIQUE INDEX uq_watch_history_profile_movie
    ON watch_history (profile_id, movie_id)
    WHERE movie_id IS NOT NULL;

CREATE UNIQUE INDEX uq_watch_history_profile_episode
    ON watch_history (profile_id, episode_id)
    WHERE episode_id IS NOT NULL;

CREATE INDEX idx_watch_history_profile_last_watched
    ON watch_history (profile_id, last_watched_at DESC);

CREATE TABLE my_list (
    id          BIGSERIAL PRIMARY KEY,
    profile_id  BIGINT      NOT NULL,
    movie_id    BIGINT,
    tv_show_id  BIGINT,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_my_list_profile FOREIGN KEY (profile_id) REFERENCES profiles (id) ON DELETE CASCADE,
    CONSTRAINT fk_my_list_movie FOREIGN KEY (movie_id) REFERENCES movies (id) ON DELETE CASCADE,
    CONSTRAINT fk_my_list_tv_show FOREIGN KEY (tv_show_id) REFERENCES tv_shows (id) ON DELETE CASCADE,
    CONSTRAINT chk_my_list_target CHECK (
        (movie_id IS NOT NULL AND tv_show_id IS NULL)
        OR (movie_id IS NULL AND tv_show_id IS NOT NULL)
    )
);

CREATE UNIQUE INDEX uq_my_list_profile_movie
    ON my_list (profile_id, movie_id)
    WHERE movie_id IS NOT NULL;

CREATE UNIQUE INDEX uq_my_list_profile_tv
    ON my_list (profile_id, tv_show_id)
    WHERE tv_show_id IS NOT NULL;

CREATE INDEX idx_my_list_profile_id ON my_list (profile_id);

CREATE TABLE ratings (
    id          BIGSERIAL PRIMARY KEY,
    profile_id  BIGINT      NOT NULL,
    movie_id    BIGINT,
    tv_show_id  BIGINT,
    rating      SMALLINT    NOT NULL,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_ratings_profile FOREIGN KEY (profile_id) REFERENCES profiles (id) ON DELETE CASCADE,
    CONSTRAINT fk_ratings_movie FOREIGN KEY (movie_id) REFERENCES movies (id) ON DELETE CASCADE,
    CONSTRAINT fk_ratings_tv_show FOREIGN KEY (tv_show_id) REFERENCES tv_shows (id) ON DELETE CASCADE,
    CONSTRAINT chk_ratings_value CHECK (rating BETWEEN 1 AND 5),
    CONSTRAINT chk_ratings_target CHECK (
        (movie_id IS NOT NULL AND tv_show_id IS NULL)
        OR (movie_id IS NULL AND tv_show_id IS NOT NULL)
    )
);

CREATE UNIQUE INDEX uq_ratings_profile_movie
    ON ratings (profile_id, movie_id)
    WHERE movie_id IS NOT NULL;

CREATE UNIQUE INDEX uq_ratings_profile_tv
    ON ratings (profile_id, tv_show_id)
    WHERE tv_show_id IS NOT NULL;

CREATE INDEX idx_ratings_movie_id ON ratings (movie_id);
CREATE INDEX idx_ratings_tv_show_id ON ratings (tv_show_id);

CREATE OR REPLACE FUNCTION set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_profiles_updated_at
    BEFORE UPDATE ON profiles
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_movies_updated_at
    BEFORE UPDATE ON movies
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_tv_shows_updated_at
    BEFORE UPDATE ON tv_shows
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_ratings_updated_at
    BEFORE UPDATE ON ratings
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();
