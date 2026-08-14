'use client'

import {useEffect, useMemo, useRef, useState} from 'react'
import {
    Accessibility,
    ArrowRight,
    ChevronDown,
    ChevronUp,
    Clock3,
    Heart,
    ListFilter,
    MapPin,
    RefreshCw,
    Search,
    TrainFront,
    X
} from 'lucide-react'
import {Arrival, Station, getArrivals, getStations, lineColor, lineNumber, lineOrder, getLines} from '@/lib/api'

const FAVORITES_KEY = 'onion-metro:favorites'
const RECENTS_KEY = 'onion-metro:recents'

function LineBadge({line}: { line: string }) {
    return <span className="line-badge" style={{backgroundColor: lineColor(line)}}>{lineNumber(line)}</span>
}

function FavoriteButton({active, onClick, label}: { active: boolean; onClick: () => void; label: string }) {
    return <button aria-label={label} onClick={onClick}
                   className={`icon-button favorite-button ${active ? 'is-favorite' : ''}`}><Heart size={17}
                                                                                                   fill={active ? 'currentColor' : 'none'}/>
    </button>
}

function StationRow({station, active, favorite, onSelect, onToggle}: {
    station: Station;
    active?: boolean;
    favorite: boolean;
    onSelect: () => void;
    onToggle: () => void
}) {
    return <div className={`station-row ${active ? 'active' : ''}`}>
        <button className="station-row-main" onClick={onSelect}><span className="station-marker"><TrainFront size={15}/></span><span
            className="station-name"><strong>{station.name}</strong><small>{station.lines.length} {station.lines.length === 1 ? 'line' : 'lines'}{station.accessible ? ' · accessible' : ''}</small></span><span
            className="line-stack">{station.lines.map((line) => <LineBadge key={line} line={line}/>)}</span><ArrowRight
            className="row-arrow" size={15}/></button>
        <FavoriteButton active={favorite} onClick={onToggle}
                        label={`${favorite ? 'Remove' : 'Add'} ${station.name} ${favorite ? 'from' : 'to'} favorites`}/>
    </div>
}

function ArrivalRow({arrival}: { arrival: Arrival }) {
    return <article className={`arrival-row ${arrival.status ?? ''}`}><LineBadge line={lineNumber(arrival.line)}/>
        <div className="arrival-destination"><span>towards</span><strong>{arrival.destination}</strong></div>
        <div className={`arrival-time ${arrival.status ?? ''}`}><Clock3 size={14}/><strong>{arrival.time}</strong></div>
    </article>
}

export default function OnionMetro() {
    const [stations, setStations] = useState<Station[]>([]), [selected, setSelected] = useState<Station | null>(null), [arrivals, setArrivals] = useState<Arrival[]>([]), [query, setQuery] = useState(''), [favorites, setFavorites] = useState<string[]>([]), [recents, setRecents] = useState<string[]>([]), [loading, setLoading] = useState(true), [refreshing, setRefreshing] = useState(false), [error, setError] = useState(false), [lastUpdated, setLastUpdated] = useState<Date | null>(null), [directoryOpen, setDirectoryOpen] = useState(false), [lineFilter, setLineFilter] = useState('all')
    const [stationOrder, setStationOrder] = useState<Map<string, string[]>>(new Map())
    const directoryPanelRef = useRef<HTMLDivElement>(null)
    const searchWrapRef = useRef<HTMLDivElement>(null)
    useEffect(() => {
        try {
            setFavorites(JSON.parse(localStorage.getItem(FAVORITES_KEY) ?? '[]'));
            setRecents(JSON.parse(localStorage.getItem(RECENTS_KEY) ?? '[]'))
        } catch {
        }
        Promise.all([
            getStations(),
            getLines()
        ])
            .then(([stations, order]) => {
                setStations(stations)
                setStationOrder(order ?? new Map())
            })
            .catch(() => setError(true))
            .finally(() => setLoading(false))
    }, [])

    useEffect(() => {
        const handleClickOutside = (event: MouseEvent) => {
            if (directoryPanelRef.current && !directoryPanelRef.current.contains(event.target as Node)) {
                setDirectoryOpen(false)
            }
        }

        document.addEventListener('mousedown', handleClickOutside)

        return () => {
            document.removeEventListener('mousedown', handleClickOutside)
        }
    }, [])

    useEffect(() => {
        const handleClickOutsideSearch = (event: MouseEvent) => {
            if (searchWrapRef.current && !searchWrapRef.current.contains(event.target as Node)) {
                setQuery('')
            }
        }

        document.addEventListener('mousedown', handleClickOutsideSearch)

        return () => {
            document.removeEventListener('mousedown', handleClickOutsideSearch)
        }
    }, [])
    const selectStation = async (station: Station) => {
        setSelected(station);
        setQuery('');
        setDirectoryOpen(false);
        setError(false);
        setRefreshing(true);
        const next = [station.id, ...recents.filter((id) => id !== station.id)].slice(0, 4);
        setRecents(next);
        localStorage.setItem(RECENTS_KEY, JSON.stringify(next));
        try {
            setArrivals(await getArrivals(station.id));
            setLastUpdated(new Date())
        } catch {
            setError(true)
        } finally {
            setRefreshing(false)
        }
    }
    const toggleFavorite = (station: Station) => {
        const next = favorites.includes(station.id) ? favorites.filter((id) => id !== station.id) : [...favorites, station.id];
        setFavorites(next);
        localStorage.setItem(FAVORITES_KEY, JSON.stringify(next))
    }
    const refresh = async () => {
        if (!selected || refreshing) return;
        setRefreshing(true);
        setError(false);
        try {
            setArrivals(await getArrivals(selected.id));
            setLastUpdated(new Date())
        } catch {
            setError(true)
        } finally {
            setRefreshing(false)
        }
    }

    const lines = [...new Set(stations.flatMap((station) => station.lines))].sort((a, b) => Number(lineOrder(a)) - Number(lineOrder(b)))
    // const matches = useMemo(() => query.trim() ? stations.filter((s) => s.name.toLowerCase().includes(query.toLowerCase())).slice(0, 6) : [], [query, stations])
    const matches = useMemo(() => {
        const trimmedQuery = query.trim();
        if (!trimmedQuery) return [];

        // Helper para quitar tildes y pasar a minúsculas
        const normalize = (str: string) =>
            str.normalize("NFD").replace(/[\u0300-\u036f]/g, "").toLowerCase();

        const cleanQuery = normalize(trimmedQuery);

        return stations
            .filter((s) => normalize(s.name).includes(cleanQuery))
            .slice(0, 6);
    }, [query, stations]);

    const directoryStations = stations
        .filter((station) =>
            lineFilter === 'all' || station.lines.includes(lineFilter)
        )
        .sort((a, b) => {
            if (lineFilter === 'all') {
                return a.name.localeCompare(b.name, 'es')
            }

            const order = stationOrder.get(lineFilter) ?? []

            return order.indexOf(a.id) - order.indexOf(b.id)
        })

    const favoriteStations = stations.filter((s) => favorites.includes(s.id)),
        recentStations = recents.map((id) => stations.find((s) => s.id === id)).filter(Boolean) as Station[]
    return <main className="onion-shell">
        <header className="topbar"><a className="brand" href="#top" aria-label="Onion Metro home"><span
            className="brand-mark"><span/><span/><span/></span><span>onion<span
            className="brand-dot">.</span>metro</span></a>
            <nav><a className="nav-link active" href="#search">Search</a><a className="nav-link"
                                                                            href="#favorites">Favorites <span
                className="nav-count">{favorites.length}</span></a></nav>
            <div className="topbar-actions">
                <div className="directory-container" ref={directoryPanelRef}>
                    <button className={`directory-trigger ${directoryOpen ? 'open' : ''}`}
                            onClick={() => setDirectoryOpen(!directoryOpen)}><ListFilter size={15}/> Stations <ChevronDown
                        size={14}/></button>
                    {directoryOpen && <div className="directory-panel">
                <div className="directory-head">
                    <div><span className="section-kicker">NETWORK DIRECTORY</span><h2>Choose a station</h2></div>
                    <button className="icon-button" onClick={() => setDirectoryOpen(false)}
                            aria-label="Close station directory"><X size={17}/></button>
                </div>
                <div className="line-filters">
                    <button className={lineFilter === 'all' ? 'selected' : ''} onClick={() => setLineFilter('all')}>All
                        lines
                    </button>
                    {lines.map((line) => <button key={line} className={lineFilter === line ? 'selected' : ''}
                                                 onClick={() => setLineFilter(line)}><LineBadge
                        line={line}/>{line}</button>)}</div>
                <div className="directory-list">{directoryStations.map((station) => <button key={station.id}
                                                                                            className={`directory-station ${selected?.id === station.id ? 'selected' : ''}`}
                                                                                            onClick={() => selectStation(station)}>
                    <span>{station.name}</span><span className="line-stack">{station.lines.map((line) => <LineBadge
                    key={line} line={line}/>)}</span></button>)}</div>
            </div>}
                </div>
            </div></header>
        <section className="hero" id="top">
            <div className="eyebrow"><span className="eyebrow-line"/> MADRID METRO · LIVE <span
                className="eyebrow-line"/></div>
            <h1>Next train,<br/><em>right now.</em></h1><p className="hero-copy">Search a station to see live arrivals,
            line status, and your quickest way forward.</p>
            <div className="search-wrap" id="search" ref={searchWrapRef}><Search size={18}/><input value={query}
                                                                               onChange={(e) => setQuery(e.target.value)}
                                                                               placeholder="Search station by name"
                                                                               aria-label="Search for a station"
                                                                               autoComplete="off"/>{query &&
                <button className="clear-search" onClick={() => setQuery('')} aria-label="Clear search"><X size={16}/>
                </button>}{matches.length > 0 &&
                <div className="search-results">{matches.map((station) => <StationRow key={station.id} station={station}
                                                                                      favorite={favorites.includes(station.id)}
                                                                                      onSelect={() => selectStation(station)}
                                                                                      onToggle={() => toggleFavorite(station)}/>)}</div>}
            </div>
            </section>
        <div className="content-grid">
            <aside className="side-column">
                <section className="side-section" id="favorites">
                    <div className="section-heading">
                        <div><span className="section-kicker">YOUR SHORTLIST</span><h2>Favorites</h2></div>
                        <span>{favoriteStations.length}</span></div>
                    {favoriteStations.length ?
                        <div className="station-list">{favoriteStations.map((s) => <StationRow key={s.id} station={s}
                                                                                               active={selected?.id === s.id}
                                                                                               favorite
                                                                                               onSelect={() => selectStation(s)}
                                                                                               onToggle={() => toggleFavorite(s)}/>)}</div> :
                        <div className="quiet-state"><Heart size={16}/><p>Pin stations for quick access.</p></div>}
                </section>
                {recentStations.length > 0 && <section className="side-section">
                    <div className="section-heading">
                        <div><span className="section-kicker">HISTORY</span><h2>Recently viewed</h2></div>
                    </div>
                    <div className="station-list">{recentStations.map((s) => <StationRow key={s.id} station={s}
                                                                                         active={selected?.id === s.id}
                                                                                         favorite={favorites.includes(s.id)}
                                                                                         onSelect={() => selectStation(s)}
                                                                                         onToggle={() => toggleFavorite(s)}/>)}</div>
                </section>}
                <div className="nearby-teaser"><MapPin size={17}/>
                    <div><strong>Nearby stations</strong><span>Location access coming soon</span></div>
                    <ArrowRight size={16}/></div>
            </aside>
            <section className="station-panel" aria-live="polite">{loading ? <div className="panel-empty">
                <div className="pulse-mark"><TrainFront size={24}/></div>
                <h2>Loading network</h2><p>Getting Madrid ready.</p></div> : selected ? <>
                <div className="station-header">
                    <div>
                        <div className="station-kicker"><span className="live-pulse"/> LIVE ARRIVALS</div>
                        <h2>{selected.name}</h2>
                        <div className="station-meta" style={{ display: "flex", gap: "12px", alignItems: "center" }}>
                            {selected.lines.map((line) => (
                                <span key={line} style={{ display: "inline-block", transform: "scale(1.8)", transformOrigin: "left center", margin: "0 4px" }}>
                                    <LineBadge line={line} />
                                </span>
                            ))}
                            {selected.accessible &&
                            <span className="accessibility"><Accessibility size={14}/> Accessible</span>}</div>
                    </div>
                    <FavoriteButton active={favorites.includes(selected.id)} onClick={() => toggleFavorite(selected)}
                                    label={`${favorites.includes(selected.id) ? 'Remove' : 'Add'} ${selected.name} favorites`}/>
                </div>
                <div className="arrivals-heading">
                    <div><span className="section-kicker">PREDICTIONS</span><h3>Next trains</h3>
                        <p>{lastUpdated ? `Updated ${lastUpdated.toLocaleTimeString([], {
                            hour: '2-digit',
                            minute: '2-digit',
                            second: '2-digit'
                        })}` : 'Waiting for live data'}</p></div>
                    <button className="refresh-button" onClick={refresh} disabled={refreshing}><RefreshCw size={15}
                                                                                                          className={refreshing ? 'spin' : ''}/> {refreshing ? 'Updating' : 'Refresh'}
                    </button>
                </div>
                {error ?
                    <div className="inline-error">Arrival data is unavailable. Try refreshing.</div> : arrivals.length ?
                        <div className="arrivals-list">{arrivals.map((a, i) => <ArrivalRow key={`${a.line}-${i}`}
                                                                                           arrival={a}/>)}</div> :
                        <div className="empty-arrivals"><TrainFront size={22}/><p>No arrival predictions right now.</p>
                        </div>}
                <div className="panel-footer"><span><span
                    className="status-dot"/> Live data from your metro API</span><span>Auto-refresh off</span></div>
            </> : <div className="panel-empty">
                <div className="pulse-mark"><Search size={23}/></div>
                <h2>Select a station</h2><p>Use the directory above or search to see your next trains.</p>
            </div>}</section>
        </div>
        <footer><span>ONION METRO</span><span>Independent real-time transit information for Madrid</span><span>Built for the next move <ChevronUp
            size={14}/></span></footer>
    </main>
}
