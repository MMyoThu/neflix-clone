const input = document.querySelector('[data-search-input]');
const box = document.querySelector('[data-search-suggest]');
if (input && box) {
    let timer;
    input.addEventListener('input', () => {
        clearTimeout(timer);
        const q = input.value.trim();
        if (q.length < 2) {
            box.hidden = true;
            return;
        }
        timer = setTimeout(async () => {
            const res = await fetch(`/api/search/suggest?q=${encodeURIComponent(q)}`);
            if (!res.ok) {
                return;
            }
            const data = await res.json();
            box.innerHTML = '';
            (data.suggestions || []).forEach((title) => {
                const btn = document.createElement('button');
                btn.type = 'button';
                btn.textContent = title;
                btn.addEventListener('click', () => {
                    input.value = title;
                    input.form?.submit();
                });
                box.appendChild(btn);
            });
            box.hidden = box.childElementCount === 0;
        }, 200);
    });
    document.addEventListener('click', (event) => {
        if (!box.contains(event.target) && event.target !== input) {
            box.hidden = true;
        }
    });
}
