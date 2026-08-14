# Onion Metro

A polished, independent real-time Madrid Metro frontend. Search a station, see the next trains, refresh arrivals, and save favorite or recently viewed stations.

## Run locally

```bash
pnpm install
pnpm dev
```

Set `VITE_API_BASE_URL` (or `NEXT_PUBLIC_API_BASE_URL` for the Next.js preview) to the Spring Boot API origin, for example `http://localhost:8080`. When no URL is configured, the preview uses a small isolated demo fallback so the interface remains explorable; no station data is embedded in the UI components.

## API contract

- `GET /api/stations` → `{ id, name, lines, accessible? }[]`
- `GET /api/stations/:id` → one station
- `GET /api/stations/:id/arrivals` → `{ line, destination, time, status? }[]`

`time` remains a string so values such as `4 min`, `Tren va a entrar en estación`, and `Actualmente sin previsión` render without coercion. The frontend only talks to the Spring Boot API; Metro de Madrid requests belong in the backend.

## Structure

- `app/` — Next.js entrypoints and theme
- `components/onion-metro.tsx` — focused, reusable product UI pieces
- `lib/api.ts` — typed fetch service, endpoint paths, line metadata, and isolated preview fallback

Favorites and recently viewed station IDs are stored in `localStorage` under `onion-metro:favorites` and `onion-metro:recents`. Automatic refresh is intentionally disabled for the initial release and can be added around `getArrivals`.
