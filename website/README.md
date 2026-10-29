# Docs website

The site at https://junaaid96.github.io/oop-concepts/ is built with [VitePress](https://vitepress.dev) from the repo's own Markdown, so `basics/`, the topic READMEs, `docs/` and the root `README.md` stay the single source of truth.

- `scripts/sync.mjs` copies those files into `content/` (git-ignored), rewrites links, and embeds each topic's C++ file, Java sources and tests.
- `.vitepress/config.mts` holds the sidebar, search, Mermaid and SEO settings.
- `.github/workflows/pages.yml` builds and deploys on every push to `main` that touches the docs or sources.

```bash
cd website
npm install      # or npm ci once package-lock.json exists
npm run dev      # http://localhost:5173/oop-concepts/
npm run build    # output in .vitepress/dist
```
