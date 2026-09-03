# StreamFlix

StreamFlix is a Netflix-style movie and TV streaming web application built with **Spring Boot**, **Spring MVC**, and **Thymeleaf**. It is a portfolio-quality, server-rendered app — not a React/Angular/Vue SPA.

The UI is inspired by modern streaming platforms. It does **not** copy Netflix branding, logos, copyrighted assets, or proprietary UI.

> **Implementation status:** Browse, search, watch, My List, ratings, profiles, and sign-up are available. Admin CRUD is still a later phase.

---

## Project overview

Users will be able to:

- Register, log in, and manage profiles
- Browse movies and TV shows
- Search and filter by genre
- Watch videos with resume/progress
- Add titles to My List
- Rate titles
- Continue watching and view watch history

Admins will be able to manage movies, TV shows, genres, and users.

---

## Features

| Area | Status | Notes |
| --- | --- | --- |
| Project bootstrap (Maven, Java 21, Spring Boot 3.5) | Done (Phase 1) | Replaces the original Gradle/MySQL starter |
| Registration, login, logout, roles | Done | Email + BCrypt; sign up at `/register` |
| Catalog, details, search | Done | Home rows, movies, TV, details, search |
| Video player, continue watching | Done | HTML5 player + progress APIs |
| My List, ratings, profiles | Done | AJAX add/remove, 1–5 stars, multi-profile |
| Admin dashboard and CRUD | Planned | `/admin/**` |
| Responsive UI and JS enhancements | Done | Dark theme, rows, mobile nav |
| Automated tests | In progress | Context + Flyway Testcontainers test in Phase 1 |

---

## Architecture

Layered Spring MVC:

**Controller → Service → Repository → Database**

Business logic lives in services. Controllers stay thin. Repositories only access data.

```
src/main/java/com/example/streamflix/
    config/
    controller/
    dto/
    entity/
    exception/
    repository/
    security/
    service/
    service/impl/
    specification/
    mapper/
    util/

src/main/resources/
    templates/          # Thymeleaf views
    static/css|js|images
    db/migration/       # Flyway SQL
    application.yml
    application-dev.yml
    application-prod.yml
```

---

## Technology stack

### Backend

- Java 21
- Spring Boot 3.5.6
- Spring MVC
- Spring Data JPA
- Spring Security
- Bean Validation
- Thymeleaf + Thymeleaf Spring Security extras
- Lombok (only where it improves readability)
- Maven
- PostgreSQL 16
- Flyway

### Frontend (later phases)

- HTML5, CSS3, vanilla JavaScript
- Thymeleaf server-side rendering
- Bootstrap 5 only where useful
- CSS Grid / Flexbox
- Responsive layouts (1920 / 1440 / 1280 / 768 / 375)

### Development

- Git
- Docker and Docker Compose
- Environment variables for secrets and datasource settings

---

## Database schema overview

Flyway migration: `src/main/resources/db/migration/V1__init_schema.sql`

| Table | Purpose |
| --- | --- |
| `users` | Accounts, hashed passwords, `USER` / `ADMIN` roles |
| `profiles` | Multiple profiles per user |
| `movies` | Movie catalog, media URLs, featured flag |
| `tv_shows` | TV catalog |
| `seasons` | Seasons belonging to a TV show |
| `episodes` | Episodes belonging to a season |
| `genres` | Shared genre list |
| `movie_genres` | Movie ↔ genre |
| `tv_show_genres` | TV show ↔ genre |
| `watch_history` | Per-profile progress for a movie **or** an episode |
| `my_list` | Per-profile saved movie **or** TV show |
| `ratings` | 1–5 stars, one rating per profile per title |

Constraints include primary keys, foreign keys, unique emails/genre names, partial unique indexes (no duplicate My List / rating / watch rows), and check constraints so My List, ratings, and watch history always point at exactly one target.

Seed data (admin user, demo users, movies, shows) will be added in a later Flyway migration.

---

## Requirements for local run

Install these before you run StreamFlix on your machine:

| Requirement | Version | Why |
| --- | --- | --- |
| JDK | **21** | Application toolchain. Java 8/11/17 on `PATH` is not enough. |
| Maven | **3.9+** or `.\mvnw.cmd` | Build and `spring-boot:run`. Optional if you use Docker-only. A wrapper is included so a global Maven install is not required. |
| Docker Desktop | **20+** with Compose v2 | PostgreSQL (and the full app stack). |
| Git | any recent | Clone / version control. |
| RAM | 4 GB+ free | Docker PostgreSQL + Spring Boot. |
| Ports | **8080** (app), **5433** (Docker Postgres on the host) | Must be free, or override with env vars. A local PostgreSQL install often already uses 5432, so Compose publishes 5433 by default. |

### Windows notes

1. Set `JAVA_HOME` to JDK 21, for example:

   ```powershell
   $env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
   $env:Path = "$env:JAVA_HOME\bin;" + $env:Path
   java -version
   ```

   You should see `21`, not `1.8`.

2. Docker Compose publishes Postgres on **host port 5433** by default so it does not collide with a local PostgreSQL on 5432. Inside the Compose network the database still listens on 5432.

3. Docker Desktop must be running before `docker compose` or Testcontainers tests.

---

## Installation

```bash
git clone <repository-url>
cd neflix-clone
copy .env.example .env   # Windows
# cp .env.example .env   # macOS / Linux
```

Edit `.env` if you need non-default ports, database name, or passwords. Do not commit `.env`.

---

## Environment variables

| Variable | Default (dev) | Used by |
| --- | --- | --- |
| `SPRING_PROFILES_ACTIVE` | `dev` | Spring profile (`dev` or `prod`) |
| `SERVER_PORT` | `8080` | HTTP port |
| `DB_HOST` | `localhost` | Postgres hostname |
| `DB_PORT` | `5433` (host) / `5432` (inside Compose) | Postgres port. Host apps use 5433; the `streamflix` container talks to `postgres:5432`. |
| `DB_NAME` | `streamflix` | Database name |
| `DB_USERNAME` | `streamflix` | Database user |
| `DB_PASSWORD` | `streamflix_dev_only` | Database password (**change for production**) |

Production (`application-prod.yml`) does **not** ship hardcoded secrets. Docker Compose passes these values into the `streamflix` service. Set real passwords via `.env` or your orchestrator.

You can also use a full JDBC URL pattern later (`DATABASE_URL`) if you deploy to a host that provides one; Phase 1 uses the `DB_*` variables above.

---

## Local development

### Option A — PostgreSQL in Docker, app on the host (recommended while coding)

1. Start only the database:

   ```bash
   docker compose up postgres -d
   ```

2. Confirm Java 21:

   ```powershell
   $env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
   java -version
   ```

3. Run the application (Maven 3.9+ on your `PATH`, or the included wrapper):

   ```bash
   mvn spring-boot:run
   ```

   Windows without a global Maven install:

   ```powershell
   $env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
   .\mvnw.cmd spring-boot:run
   ```

   The `dev` profile is active by default. It connects to `localhost:5433/streamflix`.

4. Open [http://localhost:8080](http://localhost:8080).

   **Phase 1 behavior:** Flyway creates the schema on startup. Spring Security’s default login is enabled until Phase 2. There is no catalog UI yet. A generated password is printed in the logs for the default `user` account.

5. Stop:

   ```bash
   docker compose stop postgres
   ```

### Option B — Full stack with Docker

```bash
docker compose up --build
```

This starts:

- `postgres` — PostgreSQL 16
- `streamflix` — Spring Boot app on port 8080 (`prod` profile)

Stop with `Ctrl+C`, or:

```bash
docker compose down
```

Data is kept in the `streamflix_pgdata` volume. To reset the database:

```bash
docker compose down -v
```

### Option C — Maven build only

```bash
mvn -DskipTests package
java -jar target/streamflix-0.0.1-SNAPSHOT.jar
```

Windows with the wrapper:

```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
.\mvnw.cmd -DskipTests package
java -jar target/streamflix-0.0.1-SNAPSHOT.jar
```

Postgres must already be running and reachable with the `DB_*` variables.

---

## Docker setup

```bash
docker compose up --build
```

Services:

| Service | Image / build | Port |
| --- | --- | --- |
| `postgres` | `postgres:16-alpine` | `5433` on the host → `5432` in the container |
| `streamflix` | multi-stage Dockerfile (Maven build + JRE 21) | `8080` |

The app container waits until Postgres is healthy, then Flyway runs automatically.

---

## Running migrations

Flyway runs automatically when the Spring Boot application starts.

- Migration files: `src/main/resources/db/migration/`
- Current: `V1__init_schema.sql` (tables, keys, indexes, triggers)
- Hibernate `ddl-auto` is `none` — the schema is owned by Flyway, not Hibernate

To re-run from scratch locally:

```bash
docker compose down -v
docker compose up postgres -d
mvn spring-boot:run
```

---

## Demo credentials

**Development only — change before production.**

| Role | Email | Password |
| --- | --- | --- |
| ADMIN | `admin@streamflix.local` | `streamflix` |
| USER | `user1@streamflix.local` | `streamflix` |
| USER | `user2@streamflix.local` | `streamflix` |
| USER | `user3@streamflix.local` | `streamflix` |
| USER | `user4@streamflix.local` | `streamflix` |
| USER | `user5@streamflix.local` | `streamflix` |

Create your own account at [http://localhost:8080/register](http://localhost:8080/register).

Never reuse these values in a real deployment.

---

## Watching videos

StreamFlix plays whatever URL is stored in `movies.video_url` or `episodes.video_url`. The HTML5 player needs a **direct file**, not a YouTube/Netflix page.

| You want | Set `video_url` to | Notes |
| --- | --- | --- |
| Demo (current seed) | Internet Archive MP4 links | Creative Commons shorts: *Big Buck Bunny*, *Elephants Dream* |
| Your own files | `/videos/my-movie.mp4` | Copy the file into `src/main/resources/static/videos/` |
| Cloud storage | `https://your-bucket/.../movie.mp4` | Must be publicly readable, `video/mp4`, H.264 + AAC |
| Licensed catalog | Same as cloud or local files | You must own/license the file. **Do not use Netflix or pirated rips.** |

**This app cannot legally play Netflix movies.** Those streams are encrypted and licensed. For a portfolio demo we use open Blender films that actually play in the browser.

After pulling this update, restart the app so Flyway `V3` replaces the old Google sample URLs (those now return HTTP 403).

To point one title at a local file (IntelliJ / pgAdmin):

```sql
UPDATE movies SET video_url = '/videos/my-movie.mp4' WHERE id = 1;
```

Then restart or refresh Watch. The file must exist at `src/main/resources/static/videos/my-movie.mp4`.

---

## Testing

Tests use JUnit 5, Spring Boot Test, and Testcontainers (PostgreSQL 16). Docker must be running.

```bash
mvn test
```

Windows:

```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
.\mvnw.cmd test
```

Phase 1 coverage:

- Application context loads
- Flyway migrates the schema against a throwaway Postgres container

Later phases add MockMvc tests for authentication, catalog, search, My List, watch progress, ratings, admin authorization, and validation.

---

## Project screenshots

Add screenshots under `docs/screenshots/` as the UI is built.

| Screen | File | Status |
| --- | --- | --- |
| Home / hero | `docs/screenshots/home.png` | Placeholder |
| Movies grid | `docs/screenshots/movies.png` | Placeholder |
| TV show details | `docs/screenshots/tv-details.png` | Placeholder |
| Video player | `docs/screenshots/watch.png` | Placeholder |
| Admin dashboard | `docs/screenshots/admin.png` | Placeholder |

---

## Future improvements

- Multi-profile switching with watch-progress isolation
- Recommendations based on watch history and ratings
- CDN-backed media storage
- Email verification and password reset
- Redis caching for catalog rows
- Full-text search (PostgreSQL `pg_trgm` / `tsvector`)
- Accessibility pass (keyboard, captions, contrast)

---

## Product requirements (backlog)

These are the target product requirements. They are implemented incrementally.

1. **Auth:** `/login`, `/register`, `/logout`; roles `USER` and `ADMIN`; BCrypt; session auth; CSRF.
2. **Home:** Netflix-style hero + horizontal rows (Trending, Popular, genres, Continue Watching, My List).
3. **Movies / TV:** grids, pagination, search, genre filter, sorting, details, related titles.
4. **Player:** `/watch/movie/{id}`, `/watch/episode/{id}`; legal demo videos only; persist `progress_seconds` and `completed`.
5. **My List / ratings / profiles:** AJAX add/remove, 1–5 stars, multiple profiles per user.
6. **Admin:** `/admin/**` CRUD for movies, TV, seasons, episodes, genres, user enable/disable.
7. **Quality:** Bean Validation, `@ControllerAdvice`, 403/404/500 pages, pagination, indexes, no N+1, no pirated/copyrighted Netflix media.

---

## License / media

Use only publicly available sample/demo video and image URLs, or locally stored demo files. Do not use pirated movies or copyrighted Netflix content.
