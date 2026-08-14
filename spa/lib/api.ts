export type Station = {
  id: string
  name: string
  lines: string[]
  accessible?: boolean
}

export type Arrival = {
  line: string
  destination: string
  time: string
  status?: 'normal' | 'entering' | 'unavailable'
}

// const API_BASE_URL = (process.env.NEXT_PUBLIC_API_BASE_URL ?? process.env.VITE_API_BASE_URL ?? '').replace(/\/$/, '')

const API_BASE_URL = "http://localhost:8080"

async function request<T>(path: string): Promise<T> {
  try {
    const response = await fetch(`${API_BASE_URL}${path}`, { headers: { Accept: 'application/json' } })
    if (!response.ok) throw new Error('API request failed')
    return await response.json() as T
  } catch {
    throw new Error('API request failed')
  }
}

export async function getStations() {
  return request<Station[]>('/api/metro/stations')
}

export async function getLines(): Promise<Map<string, string[]>> {
  const data = await request<Record<string, string[]>>(
      '/api/metro/lines'
  )

  return new Map(Object.entries(data))
}

export async function getArrivals(id: string) {
  return request<Arrival[]>(`/api/metro/arrival/${id}`)
}

export function lineNumber(line: string) {
  if (line == "Ramal"){
    return "R"
  }
  return line.replace("ínea ", "").toUpperCase()
}

export function lineColor(line: string) {
  const num = lineNumber(line)
  const colors: Record<string, string> = {
    'L1': '#2bb6e6', 'L2': '#eb2f29', 'L3': '#fecb18', 'L4': '#a15c2f',
    'L5': '#7ac142', 'L6': '#737f86', 'L7': '#f5842a', 'L8': '#db74ae',
    'L9': '#914499', 'L10': '#015395', 'L11': '#00a34d', 'L12': '#a99100',
    'ML1': '#4d84c4', 'R': "#ffffff"
  }
  return colors[num] ?? '#8c8a83'
}

export function lineOrder(line: string) {
  const num = lineNumber(line)
  const order: Record<string, number> = {
    'L1': 1, 'L2': 2, 'L3': 3, 'L4': 4,
    'L5': 5, 'L6': 6, 'L7': 7, 'L8': 8,
    'L9': 9, 'L10': 10, 'L11': 11, 'L12': 12,
    "R": 13,"ML1": 14,
  }
  return order[num] ?? 0
}

