import { createI18n } from 'vue-i18n'
import de from './locales/de'
import en from './locales/en'

type Locale = 'de' | 'en'

function detectLocale(): Locale {
  const saved = localStorage.getItem('whenly:locale') as Locale | null
  if (saved === 'de' || saved === 'en') return saved
  const browser = navigator.language?.slice(0, 2)
  return browser === 'de' ? 'de' : 'en'
}

export const i18n = createI18n({
  legacy: false,
  locale: detectLocale(),
  fallbackLocale: 'en',
  messages: { de, en }
})

export function setLocale(locale: Locale) {
  i18n.global.locale.value = locale
  localStorage.setItem('whenly:locale', locale)
  document.documentElement.lang = locale
}
