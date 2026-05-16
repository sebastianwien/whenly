import type { PollOption, PollType } from '@/types'

export function formatOptionLabel(
  option: PollOption,
  type: PollType,
  timezone: string,
  locale: string
): string {
  if (type === 'GENERIC') {
    return option.label ?? ''
  }
  if (!option.startAt) return option.label ?? ''
  const start = new Date(option.startAt)
  const end = option.endAt ? new Date(option.endAt) : null

  const fmt: Intl.DateTimeFormatOptions = type === 'DATE_ONLY'
    ? { weekday: 'short', day: '2-digit', month: 'short', year: 'numeric', timeZone: timezone }
    : { weekday: 'short', day: '2-digit', month: 'short', hour: '2-digit', minute: '2-digit', timeZone: timezone }
  const startStr = new Intl.DateTimeFormat(locale, fmt).format(start)
  if (!end) return startStr
  const endStr = new Intl.DateTimeFormat(locale, type === 'DATE_ONLY'
    ? { day: '2-digit', month: 'short', timeZone: timezone }
    : { hour: '2-digit', minute: '2-digit', timeZone: timezone }
  ).format(end)
  return `${startStr} – ${endStr}`
}

export function formatRelative(iso: string, locale: string): string {
  const d = new Date(iso)
  const diff = (d.getTime() - Date.now()) / 1000
  const abs = Math.abs(diff)
  const rtf = new Intl.RelativeTimeFormat(locale, { numeric: 'auto' })
  if (abs < 60) return rtf.format(Math.round(diff), 'second')
  if (abs < 3600) return rtf.format(Math.round(diff / 60), 'minute')
  if (abs < 86400) return rtf.format(Math.round(diff / 3600), 'hour')
  if (abs < 86400 * 7) return rtf.format(Math.round(diff / 86400), 'day')
  return new Intl.DateTimeFormat(locale, { dateStyle: 'medium' }).format(d)
}
