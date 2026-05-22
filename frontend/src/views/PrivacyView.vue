<script setup lang="ts">
import { useI18n } from 'vue-i18n'
import { useSeoHead } from '@/composables/useSeoHead'

const { t } = useI18n()

useSeoHead({
  title: t('privacy.heading') + ' - whenly',
  canonical: 'https://whenly.de/datenschutz',
})
</script>

<template>
  <section class="container-narrow py-12">
    <h1 class="font-serif text-4xl sm:text-5xl">{{ t('privacy.heading') }}</h1>

    <div class="mt-10 space-y-8 text-base leading-relaxed">

      <div>
        <h2 class="font-serif text-2xl mb-2">{{ t('privacy.overviewHeading') }}</h2>
        <p>{{ t('privacy.overview') }}</p>
      </div>

      <div>
        <h2 class="font-serif text-2xl mb-2">{{ t('privacy.dataHeading') }}</h2>
        <p>{{ t('privacy.data1') }}</p>
        <p class="mt-3">{{ t('privacy.data2') }}</p>
      </div>

      <div>
        <h2 class="font-serif text-2xl mb-2">{{ t('privacy.logsHeading') }}</h2>
        <p>{{ t('privacy.logs') }}</p>
      </div>

      <div>
        <h2 class="font-serif text-2xl mb-2">{{ t('privacy.cookiesHeading') }}</h2>
        <p>{{ t('privacy.cookies') }}</p>
      </div>

      <div>
        <h2 class="font-serif text-2xl mb-2">{{ t('privacy.adsHeading') }}</h2>
        <p>{{ t('privacy.ads1') }}</p>
        <p class="mt-3">{{ t('privacy.ads2') }}</p>
        <p class="mt-3">{{ t('privacy.ads3') }}</p>
        <p class="mt-3">
          {{ t('privacy.ads4') }}
          <a
            href="https://policies.google.com/privacy"
            target="_blank"
            rel="noopener noreferrer"
            class="underline underline-offset-2 hover:text-[var(--color-clay-600)]"
          >
            policies.google.com/privacy
          </a>
        </p>
        <p class="mt-3">
          {{ t('privacy.ads5') }}
          <a
            href="https://support.google.com/adsense/answer/9007336"
            target="_blank"
            rel="noopener noreferrer"
            class="underline underline-offset-2 hover:text-[var(--color-clay-600)]"
          >
            support.google.com/adsense/answer/9007336
          </a>
        </p>
      </div>

      <div>
        <h2 class="font-serif text-2xl mb-2">{{ t('privacy.retentionHeading') }}</h2>
        <p>{{ t('privacy.retention') }}</p>
      </div>

      <div>
        <h2 class="font-serif text-2xl mb-2">{{ t('privacy.rightsHeading') }}</h2>
        <p>{{ t('privacy.rights') }}</p>
      </div>

      <div>
        <h2 class="font-serif text-2xl mb-2">{{ t('privacy.contactHeading') }}</h2>
        <p>
          {{ t('privacy.contact') }}
          <a
            href="mailto:impressum@whenly.app"
            class="underline underline-offset-2 hover:text-[var(--color-clay-600)]"
          >
            impressum@whenly.app
          </a>
        </p>
      </div>

    </div>

    <div class="mt-12 pt-8 border-t-2 border-[var(--color-ink-900)]">
      <RouterLink to="/" class="underline underline-offset-2 hover:text-[var(--color-clay-600)]">
        {{ t('impressum.back') }}
      </RouterLink>
    </div>
  </section>
</template>
