-- Demo catalog and accounts. Password for every seeded user: streamflix
-- Hash: BCrypt of "streamflix" (development only — change before production)

INSERT INTO genres (id, name) VALUES
    (1, 'Action'),
    (2, 'Comedy'),
    (3, 'Drama'),
    (4, 'Thriller'),
    (5, 'Sci-Fi'),
    (6, 'Horror'),
    (7, 'Romance'),
    (8, 'Documentary'),
    (9, 'Animation'),
    (10, 'Crime'),
    (11, 'Adventure'),
    (12, 'Fantasy');
SELECT setval('genres_id_seq', 12);

INSERT INTO users (id, email, password, first_name, last_name, role, enabled) VALUES
    (1, 'admin@streamflix.local', '$2a$10$au9Eh.8G8CtcQTPL50Z4JeNuHwYWCMzZkQOFv6f0arvnLAWLHPY0G', 'Ava', 'Admin', 'ADMIN', TRUE),
    (2, 'user1@streamflix.local', '$2a$10$au9Eh.8G8CtcQTPL50Z4JeNuHwYWCMzZkQOFv6f0arvnLAWLHPY0G', 'Noah', 'Reed', 'USER', TRUE),
    (3, 'user2@streamflix.local', '$2a$10$au9Eh.8G8CtcQTPL50Z4JeNuHwYWCMzZkQOFv6f0arvnLAWLHPY0G', 'Maya', 'Chen', 'USER', TRUE),
    (4, 'user3@streamflix.local', '$2a$10$au9Eh.8G8CtcQTPL50Z4JeNuHwYWCMzZkQOFv6f0arvnLAWLHPY0G', 'Luis', 'Ortega', 'USER', TRUE),
    (5, 'user4@streamflix.local', '$2a$10$au9Eh.8G8CtcQTPL50Z4JeNuHwYWCMzZkQOFv6f0arvnLAWLHPY0G', 'Priya', 'Shah', 'USER', TRUE),
    (6, 'user5@streamflix.local', '$2a$10$au9Eh.8G8CtcQTPL50Z4JeNuHwYWCMzZkQOFv6f0arvnLAWLHPY0G', 'Eli', 'Brooks', 'USER', TRUE);
SELECT setval('users_id_seq', 6);

INSERT INTO profiles (id, user_id, profile_name, avatar_url) VALUES
    (1, 1, 'Ava', '/images/avatars/avatar-1.svg'),
    (2, 2, 'Noah', '/images/avatars/avatar-2.svg'),
    (3, 3, 'Maya', '/images/avatars/avatar-3.svg'),
    (4, 4, 'Luis', '/images/avatars/avatar-4.svg'),
    (5, 5, 'Priya', '/images/avatars/avatar-1.svg'),
    (6, 6, 'Eli', '/images/avatars/avatar-2.svg');
SELECT setval('profiles_id_seq', 6);

INSERT INTO movies (
    id, title, description, release_year, duration_minutes, maturity_rating,
    poster_url, backdrop_url, trailer_url, video_url, featured
) VALUES
    (1, 'Night Circuit', 'A courier races across a neon city to deliver a drive that several syndicates want erased.', 2024, 118, 'PG-13',
        'https://picsum.photos/seed/sf-m1/400/600', 'https://picsum.photos/seed/sf-m1b/1920/800',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4', TRUE),
    (2, 'Harbor Lights', 'A dockworker and a marine biologist uncover a quiet conspiracy in a coastal town.', 2023, 126, 'PG-13',
        'https://picsum.photos/seed/sf-m2/400/600', 'https://picsum.photos/seed/sf-m2b/1920/800',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4', FALSE),
    (3, 'Quiet Frequency', 'Astronomers pick up a repeating signal that answers questions nobody asked.', 2022, 132, 'PG-13',
        'https://picsum.photos/seed/sf-m3/400/600', 'https://picsum.photos/seed/sf-m3b/1920/800',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4', FALSE),
    (4, 'Copper Canyon', 'A surveyor maps a desert gorge and finds a map that should not exist.', 2021, 110, 'PG',
        'https://picsum.photos/seed/sf-m4/400/600', 'https://picsum.photos/seed/sf-m4b/1920/800',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4', FALSE),
    (5, 'Glass Orchard', 'Two strangers share a greenhouse during a city-wide blackout and rewrite their plans.', 2024, 104, 'PG',
        'https://picsum.photos/seed/sf-m5/400/600', 'https://picsum.photos/seed/sf-m5b/1920/800',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4', FALSE),
    (6, 'Ember Protocol', 'An elite extraction team has twelve hours to reverse a rigged satellite burn.', 2023, 121, 'PG-13',
        'https://picsum.photos/seed/sf-m6/400/600', 'https://picsum.photos/seed/sf-m6b/1920/800',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4', FALSE),
    (7, 'Paper Moons', 'A failing variety show becomes the last honest place in a polished media town.', 2020, 98, 'PG',
        'https://picsum.photos/seed/sf-m7/400/600', 'https://picsum.photos/seed/sf-m7b/1920/800',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4', FALSE),
    (8, 'The Last Signal', 'A radio host stays on air during a storm that knocks out every other channel.', 2022, 107, 'PG-13',
        'https://picsum.photos/seed/sf-m8/400/600', 'https://picsum.photos/seed/sf-m8b/1920/800',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4', FALSE),
    (9, 'Willow Station', 'A rural teacher inherits a shuttered rail stop and the letters left behind.', 2019, 115, 'PG',
        'https://picsum.photos/seed/sf-m9/400/600', 'https://picsum.photos/seed/sf-m9b/1920/800',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4', FALSE),
    (10, 'Neon Tide', 'Street racers smuggle medicine through flooded districts after the levees fail.', 2024, 119, 'PG-13',
        'https://picsum.photos/seed/sf-m10/400/600', 'https://picsum.photos/seed/sf-m10b/1920/800',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackOnStreetAndDirt.mp4',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackOnStreetAndDirt.mp4', FALSE),
    (11, 'Sunday Gravity', 'A clumsy physicist and a pastry chef try to keep a community kitchen open.', 2021, 96, 'PG',
        'https://picsum.photos/seed/sf-m11/400/600', 'https://picsum.photos/seed/sf-m11b/1920/800',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4', FALSE),
    (12, 'Iron Orchard', 'Campers follow a trail of metal trees into a forest that records every sound.', 2020, 101, 'R',
        'https://picsum.photos/seed/sf-m12/400/600', 'https://picsum.photos/seed/sf-m12b/1920/800',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4', FALSE),
    (13, 'Blue Hour', 'Two architects restore a lighthouse and a relationship they thought was finished.', 2023, 109, 'PG-13',
        'https://picsum.photos/seed/sf-m13/400/600', 'https://picsum.photos/seed/sf-m13b/1920/800',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4', FALSE),
    (14, 'Static Kingdom', 'A teenager inherits a pocket universe that only works when the TV is on.', 2024, 128, 'PG-13',
        'https://picsum.photos/seed/sf-m14/400/600', 'https://picsum.photos/seed/sf-m14b/1920/800',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4', FALSE),
    (15, 'Crow''s Winter', 'A private investigator tracks a missing journalist through a frozen port city.', 2022, 113, 'R',
        'https://picsum.photos/seed/sf-m15/400/600', 'https://picsum.photos/seed/sf-m15b/1920/800',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4', FALSE),
    (16, 'Maple Court', 'Siblings return home to sell a house that keeps rearranging their memories.', 2018, 122, 'PG-13',
        'https://picsum.photos/seed/sf-m16/400/600', 'https://picsum.photos/seed/sf-m16b/1920/800',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4', FALSE),
    (17, 'Velocity Bloom', 'A stunt driver is hired to fake an accident and accidentally prevents a real one.', 2023, 116, 'PG-13',
        'https://picsum.photos/seed/sf-m17/400/600', 'https://picsum.photos/seed/sf-m17b/1920/800',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4', FALSE),
    (18, 'The Inland Sea', 'A field team documents a lake that appears only after long droughts.', 2021, 90, 'PG',
        'https://picsum.photos/seed/sf-m18/400/600', 'https://picsum.photos/seed/sf-m18b/1920/800',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4', FALSE),
    (19, 'Kindling', 'Stand-up comics on a doomed tour bus rewrite their sets after every breakdown.', 2024, 94, 'PG-13',
        'https://picsum.photos/seed/sf-m19/400/600', 'https://picsum.photos/seed/sf-m19b/1920/800',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4', FALSE),
    (20, 'Afterlight', 'A cartographer follows a river that only exists at dusk and leads to a hidden city.', 2022, 130, 'PG',
        'https://picsum.photos/seed/sf-m20/400/600', 'https://picsum.photos/seed/sf-m20b/1920/800',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackOnStreetAndDirt.mp4',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackOnStreetAndDirt.mp4', FALSE);
SELECT setval('movies_id_seq', 20);

INSERT INTO movie_genres (movie_id, genre_id) VALUES
    (1, 1), (1, 4),
    (2, 3),
    (3, 5), (3, 3),
    (4, 11),
    (5, 7),
    (6, 1), (6, 5),
    (7, 2),
    (8, 4),
    (9, 3),
    (10, 1), (10, 11),
    (11, 2), (11, 7),
    (12, 6),
    (13, 7), (13, 3),
    (14, 5), (14, 12),
    (15, 4), (15, 10),
    (16, 3),
    (17, 1),
    (18, 8),
    (19, 2),
    (20, 12), (20, 11);

INSERT INTO tv_shows (
    id, title, description, release_year, maturity_rating,
    poster_url, backdrop_url, trailer_url, featured
) VALUES
    (1, 'Portico', 'Neighbors in a converted warehouse share secrets, meals, and a locked seventh floor.', 2023, 'TV-14',
        'https://picsum.photos/seed/sf-t1/400/600', 'https://picsum.photos/seed/sf-t1b/1920/800',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4', TRUE),
    (2, 'Northline', 'Transit police investigate crimes that only happen between stations.', 2022, 'TV-MA',
        'https://picsum.photos/seed/sf-t2/400/600', 'https://picsum.photos/seed/sf-t2b/1920/800',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4', FALSE),
    (3, 'Room 12', 'A community college AV club accidentally broadcasts the town''s private radio.', 2024, 'TV-PG',
        'https://picsum.photos/seed/sf-t3/400/600', 'https://picsum.photos/seed/sf-t3b/1920/800',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4', FALSE),
    (4, 'Cascade', 'Engineers keep a floating city stable while politics try to sink it.', 2021, 'TV-14',
        'https://picsum.photos/seed/sf-t4/400/600', 'https://picsum.photos/seed/sf-t4b/1920/800',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4', FALSE),
    (5, 'The Hollow Year', 'A journalist lives the same winter twelve times and files a new lead each loop.', 2023, 'TV-14',
        'https://picsum.photos/seed/sf-t5/400/600', 'https://picsum.photos/seed/sf-t5b/1920/800',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4', FALSE),
    (6, 'Eastgate', 'A family clinic on the edge of the city treats patients who should not exist.', 2020, 'TV-14',
        'https://picsum.photos/seed/sf-t6/400/600', 'https://picsum.photos/seed/sf-t6b/1920/800',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4', FALSE),
    (7, 'Low Orbit', 'Maintenance crews on a civilian space station bargain with gravity and gossip.', 2024, 'TV-PG',
        'https://picsum.photos/seed/sf-t7/400/600', 'https://picsum.photos/seed/sf-t7b/1920/800',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4', FALSE),
    (8, 'Bakery Hours', 'Night bakers compete, collaborate, and hide a second kitchen downstairs.', 2022, 'TV-PG',
        'https://picsum.photos/seed/sf-t8/400/600', 'https://picsum.photos/seed/sf-t8b/1920/800',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4', FALSE),
    (9, 'Red Harbor', 'A port authority lawyer and a tug captain unwind cargo that was never declared.', 2021, 'TV-MA',
        'https://picsum.photos/seed/sf-t9/400/600', 'https://picsum.photos/seed/sf-t9b/1920/800',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4', FALSE),
    (10, 'Night School', 'Adult learners share a classroom that opens a different city each evening.', 2019, 'TV-14',
        'https://picsum.photos/seed/sf-t10/400/600', 'https://picsum.photos/seed/sf-t10b/1920/800',
        'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackOnStreetAndDirt.mp4', FALSE);
SELECT setval('tv_shows_id_seq', 10);

INSERT INTO tv_show_genres (tv_show_id, genre_id) VALUES
    (1, 3),
    (2, 10), (2, 4),
    (3, 2),
    (4, 5),
    (5, 4),
    (6, 3),
    (7, 5), (7, 2),
    (8, 2),
    (9, 10),
    (10, 3), (10, 12);

INSERT INTO seasons (id, tv_show_id, season_number, title) VALUES
    (1, 1, 1, 'Season 1'),
    (2, 1, 2, 'Season 2'),
    (3, 2, 1, 'Season 1'),
    (4, 2, 2, 'Season 2'),
    (5, 3, 1, 'Season 1'),
    (6, 4, 1, 'Season 1'),
    (7, 5, 1, 'Season 1'),
    (8, 6, 1, 'Season 1'),
    (9, 7, 1, 'Season 1'),
    (10, 8, 1, 'Season 1'),
    (11, 9, 1, 'Season 1'),
    (12, 10, 1, 'Season 1');
SELECT setval('seasons_id_seq', 12);

INSERT INTO episodes (season_id, episode_number, title, description, duration_minutes, thumbnail_url, video_url, release_date)
SELECT s.id, e.n,
       'Episode ' || e.n,
       'An original StreamFlix episode from this season.',
       42,
       'https://picsum.photos/seed/sf-ep-' || s.id || '-' || e.n || '/640/360',
       CASE (e.n % 3)
           WHEN 0 THEN 'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4'
           WHEN 1 THEN 'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4'
           ELSE 'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4'
       END,
       DATE '2023-01-01' + ((s.id * 10 + e.n)::integer)
FROM seasons s
CROSS JOIN (VALUES (1), (2), (3)) AS e(n);

INSERT INTO my_list (profile_id, movie_id, tv_show_id) VALUES
    (2, 1, NULL),
    (2, 5, NULL),
    (2, NULL, 1);

INSERT INTO watch_history (profile_id, movie_id, episode_id, progress_seconds, completed, last_watched_at) VALUES
    (2, 1, NULL, 480, FALSE, NOW() - INTERVAL '1 day'),
    (2, 7, NULL, 200, FALSE, NOW() - INTERVAL '2 days');

INSERT INTO ratings (profile_id, movie_id, tv_show_id, rating) VALUES
    (2, 1, NULL, 5),
    (3, 1, NULL, 4),
    (2, NULL, 1, 5);
