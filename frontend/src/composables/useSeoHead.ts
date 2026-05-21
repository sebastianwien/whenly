import { useHead, useSeoMeta, type UseHeadInput } from '@unhead/vue'

const BASE_URL = 'https://whenly.de'
const DEFAULT_OG_IMAGE = `${BASE_URL}/og-image.png`
const DEFAULT_DESCRIPTION =
  'whenly - privacy-friendly open-source group scheduling. No accounts, no trackers, no dark patterns.'

interface SeoOptions {
  title: string
  description?: string
  canonical?: string
  noindex?: boolean
  ogImage?: string
  jsonLd?: Record<string, unknown> | Record<string, unknown>[]
}

export function useSeoHead(options: SeoOptions) {
  const {
    title,
    description = DEFAULT_DESCRIPTION,
    canonical,
    noindex = false,
    ogImage = DEFAULT_OG_IMAGE,
    jsonLd,
  } = options

  useSeoMeta({
    title,
    ogTitle: title,
    description,
    ogDescription: description,
    ogImage,
    ogType: 'website',
    ogUrl: canonical ?? BASE_URL,
    twitterCard: 'summary_large_image',
    twitterTitle: title,
    twitterDescription: description,
    twitterImage: ogImage,
    robots: noindex ? 'noindex, nofollow' : 'index, follow',
  })

  const head: UseHeadInput = {}

  if (canonical) {
    head.link = [{ rel: 'canonical', href: canonical }]
  }

  if (jsonLd) {
    const schemas = Array.isArray(jsonLd) ? jsonLd : [jsonLd]
    head.script = schemas.map((schema) => ({
      type: 'application/ld+json' as const,
      innerHTML: JSON.stringify(schema),
    }))
  }

  useHead(head)
}
