document.querySelectorAll('[data-rating-form]').forEach((form) => {
    form.addEventListener('click', async (event) => {
        const button = event.target.closest('[data-rating]');
        if (!button) {
            return;
        }
        event.preventDefault();
        const rating = Number(button.dataset.rating);
        const res = await fetch(form.dataset.url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                ...window.StreamFlix.csrfHeaders()
            },
            body: JSON.stringify({ rating })
        });
        if (!res.ok) {
            window.StreamFlix.toast('Could not save rating');
            return;
        }
        form.querySelectorAll('[data-rating]').forEach((el) => {
            el.classList.toggle('is-on', Number(el.dataset.rating) <= rating);
        });
        window.StreamFlix.toast('Rating saved');
    });
});
