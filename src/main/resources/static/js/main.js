function csrfHeaders() {
    const token = document.querySelector('meta[name="_csrf"]')?.content;
    const header = document.querySelector('meta[name="_csrf_header"]')?.content;
    return token && header ? { [header]: token } : {};
}

function toast(message) {
    const el = document.createElement('div');
    el.className = 'toast toast--ok';
    el.style.position = 'fixed';
    el.style.bottom = '1rem';
    el.style.right = '1rem';
    el.style.zIndex = '50';
    el.textContent = message;
    document.body.appendChild(el);
    setTimeout(() => el.remove(), 2200);
}

document.querySelector('[data-nav-toggle]')?.addEventListener('click', () => {
    document.querySelector('[data-nav-panel]')?.classList.toggle('is-open');
});

document.querySelectorAll('[data-row-scroller]').forEach((scroller) => {
    const row = scroller.closest('.row');
    row?.querySelector('[data-row-next]')?.addEventListener('click', () => {
        scroller.scrollBy({ left: 400, behavior: 'smooth' });
    });
    row?.querySelector('[data-row-prev]')?.addEventListener('click', () => {
        scroller.scrollBy({ left: -400, behavior: 'smooth' });
    });
});

window.StreamFlix = { csrfHeaders, toast };
