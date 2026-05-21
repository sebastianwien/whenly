<script setup lang="ts">
import { useI18n } from 'vue-i18n'
import { useSeoHead } from '@/composables/useSeoHead'

const { t } = useI18n()

useSeoHead({
  title: 'About - whenly',
  description: 'whenly is a privacy-friendly open-source group scheduling tool. No accounts, no trackers.',
  canonical: 'https://whenly.de/about',
})
</script>

<template>
  <section class="container-narrow py-12">
    <h1 class="font-serif text-4xl sm:text-5xl">{{ t('about.heading') }}</h1>
    <p class="mt-6 text-lg leading-relaxed">{{ t('about.p1') }}</p>
    <p class="mt-4 text-lg leading-relaxed">{{ t('about.p2') }}</p>

    <h2 class="font-serif text-2xl mt-10">{{ t('about.techHeading') }}</h2>
    <p class="mt-2">{{ t('about.tech') }}</p>

    <h2 class="font-serif text-2xl mt-10">{{ t('about.licenseHeading') }}</h2>
    <p class="mt-2">{{ t('about.license') }}</p>
  </section>
</template>
