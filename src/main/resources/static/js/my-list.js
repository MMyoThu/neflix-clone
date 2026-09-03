function endpoint(type, id) {
    return type === 'TV' ? `/api/my-list/tv/${id}` : `/api/my-list/movie/${id}`;
}

document.addEventListener('click', async (event) => {
    const button = event.target.closest('[data-mylist-toggle]');
    if (!button) {
        return;
    }
    event.preventDefault();
    event.stopPropagation();
    const card = button.closest('[data-id]');
    const id = button.dataset.id || card?.dataset.id;
    const type = button.dataset.type || card?.dataset.type;
    if (!id || !type) {
        return;
    }
    const saved = button.getAttribute('aria-pressed') === 'true';
    const res = await fetch(endpoint(type, id), {
        method: saved ? 'DELETE' : 'POST',
        headers: { ...window.StreamFlix.csrfHeaders() }
    });
    if (!res.ok) {
        window.StreamFlix.toast('Could not update My List');
        return;
    }
    const next = !saved;
    button.setAttribute('aria-pressed', String(next));
    if (button.textContent === '+' || button.textContent === '✓') {
        button.textContent = next ? '✓' : '+';
    } else {
        button.textContent = next ? 'Remove from My List' : 'Add to My List';
    }
    window.StreamFlix.toast(next ? 'Added to My List' : 'Removed from My List');
});
