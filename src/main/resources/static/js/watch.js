const player = document.getElementById('player');
if (player) {
    const url = player.dataset.progressUrl;
    const start = Number(player.dataset.start || 0);

    const save = (completed = false) => {
        if (!url) {
            return;
        }
        const duration = Number.isFinite(player.duration) ? Math.floor(player.duration) : null;
        const progress = completed && duration ? duration : Math.floor(player.currentTime);
        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                ...window.StreamFlix.csrfHeaders()
            },
            body: JSON.stringify({
                progressSeconds: progress,
                durationSeconds: duration
            }),
            keepalive: true
        }).catch(() => undefined);
    };

    const errorBox = document.querySelector('[data-player-error]');
    player.addEventListener('error', () => {
        if (errorBox) {
            errorBox.hidden = false;
        }
    });
    player.addEventListener('loadedmetadata', () => {
        if (errorBox) {
            errorBox.hidden = true;
        }
        if (start > 0 && start < player.duration) {
            player.currentTime = start;
        }
        player.play().catch(() => {
            // Autoplay may be blocked until the user presses play.
        });
    });
    player.addEventListener('pause', () => save(false));
    player.addEventListener('ended', () => save(true));
    window.addEventListener('pagehide', () => save(player.ended));
}
