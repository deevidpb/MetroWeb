import { Analytics } from '@vercel/analytics/next'
import type { Metadata, Viewport } from 'next'
import { DM_Sans } from 'next/font/google'
import './globals.css'

const dmSans = DM_Sans({ subsets: ['latin'], variable: '--font-dm-sans' })

export const metadata: Metadata = {
  title: 'Onion Metro — Madrid arrivals',
  description: 'A focused real-time Madrid Metro arrivals workspace.',
  generator: 'Onion Metro',
}

export const viewport: Viewport = { colorScheme: 'dark', themeColor: '#0b1220' }

export default function RootLayout({ children }: Readonly<{ children: React.ReactNode }>) {
  return <html lang="en" className={dmSans.variable}><body className="antialiased">{children}{process.env.NODE_ENV === 'production' && <Analytics />}</body></html>
}
