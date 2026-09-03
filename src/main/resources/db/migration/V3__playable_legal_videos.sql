-- Replace dead Google sample clips (now 403) with legal, browser-playable films.
-- Creative Commons / public-domain sources only. Not Netflix titles.

UPDATE movies SET
    trailer_url = CASE MOD(id, 3)
        WHEN 0 THEN 'https://archive.org/download/big-buck-bunny-1440p-60-fps-vp-8/Big%20Buck%20Bunny%20720p%2030FPS.mp4'
        WHEN 1 THEN 'https://archive.org/download/big-buck-bunny-1440p-60-fps-vp-8/Big%20Buck%20Bunny%20360p%2030FPS.mp4'
        ELSE 'https://archive.org/download/ElephantsDream/ed_hd_512kb.mp4'
    END,
    video_url = CASE MOD(id, 3)
        WHEN 0 THEN 'https://archive.org/download/big-buck-bunny-1440p-60-fps-vp-8/Big%20Buck%20Bunny%20720p%2030FPS.mp4'
        WHEN 1 THEN 'https://archive.org/download/big-buck-bunny-1440p-60-fps-vp-8/Big%20Buck%20Bunny%20360p%2030FPS.mp4'
        ELSE 'https://archive.org/download/ElephantsDream/ed_hd_512kb.mp4'
    END;

UPDATE tv_shows SET
    trailer_url = CASE MOD(id, 3)
        WHEN 0 THEN 'https://archive.org/download/big-buck-bunny-1440p-60-fps-vp-8/Big%20Buck%20Bunny%20720p%2030FPS.mp4'
        WHEN 1 THEN 'https://archive.org/download/big-buck-bunny-1440p-60-fps-vp-8/Big%20Buck%20Bunny%20360p%2030FPS.mp4'
        ELSE 'https://archive.org/download/ElephantsDream/ed_hd_512kb.mp4'
    END;

UPDATE episodes SET
    video_url = CASE MOD(id, 3)
        WHEN 0 THEN 'https://archive.org/download/big-buck-bunny-1440p-60-fps-vp-8/Big%20Buck%20Bunny%20720p%2030FPS.mp4'
        WHEN 1 THEN 'https://archive.org/download/big-buck-bunny-1440p-60-fps-vp-8/Big%20Buck%20Bunny%20360p%2030FPS.mp4'
        ELSE 'https://archive.org/download/ElephantsDream/ed_hd_512kb.mp4'
    END;
