<script setup lang="ts">
import { useI18n } from 'vue-i18n'
import {
  CalendarDaysIcon,
  SparklesIcon,
  TrashIcon,
  CheckCircleIcon,
  ArrowDownTrayIcon,
  GlobeAltIcon
} from '@heroicons/vue/24/outline'

const { t } = useI18n()

const features = [
  { icon: CheckCircleIcon, key: 'noAccount' },
  { icon: SparklesIcon,    key: 'smart' },
  { icon: TrashIcon,       key: 'retention' },
  { icon: CalendarDaysIcon, key: 'maybe' },
  { icon: ArrowDownTrayIcon, key: 'ics' },
  { icon: GlobeAltIcon,    key: 'i18n' }
] as const
</script>

<template>
  <div>
  <!-- Hero ------------------------------------------------------------- -->
  <section class="container-wide pt-10 sm:pt-16 pb-12 sm:pb-20">
    <div class="grid lg:grid-cols-[3fr_2fr] gap-10 items-end">
      <div>
        <p class="chip chip-accent uppercase tracking-wider text-xs">
          {{ t('landing.hero.eyebrow') }}
        </p>
        <h1 class="mt-4 font-serif text-5xl sm:text-7xl leading-[0.95] font-semibold whitespace-pre-line">
          {{ t('landing.hero.heading') }}
        </h1>
        <p class="mt-6 max-w-xl text-lg text-[var(--color-ink-100)] dark:text-[var(--color-sand-200)]">
          {{ t('landing.hero.sub') }}
        </p>
        <div class="mt-8 flex flex-wrap gap-3">
          <RouterLink to="/new" class="btn btn-accent text-base px-6 py-3">
            {{ t('landing.hero.cta') }}
          </RouterLink>
          <a href="#how" class="btn btn-outline text-base px-6 py-3">
            {{ t('landing.hero.ctaSecondary') }}
          </a>
        </div>
      </div>

      <!-- Decorative poll preview, hand-shaped -->
      <div class="relative hidden lg:block">
        <div class="absolute -top-4 -left-4 w-full h-full bg-[var(--color-clay-300)] rounded-[8px] rotate-[-2deg] -z-10"></div>
        <div class="surface p-6 rotate-[1.5deg]">
          <p class="font-serif text-xl">Sunday brunch?</p>
          <div class="mt-3 space-y-2">
            <div class="flex items-center justify-between">
              <span class="text-sm">Sun · 11:00</span>
              <div class="flex gap-1">
                <span class="vote-pill vote-yes !min-w-0 !h-7 !text-xs px-2">✓ 4</span>
                <span class="vote-pill vote-maybe !min-w-0 !h-7 !text-xs px-2">~ 1</span>
                <span class="vote-pill vote-no !min-w-0 !h-7 !text-xs px-2">✗ 0</span>
              </div>
            </div>
            <div class="flex items-center justify-between">
              <span class="text-sm">Sun · 13:00</span>
              <div class="flex gap-1">
                <span class="vote-pill vote-yes !min-w-0 !h-7 !text-xs px-2">✓ 2</span>
                <span class="vote-pill vote-maybe !min-w-0 !h-7 !text-xs px-2">~ 2</span>
                <span class="vote-pill vote-no !min-w-0 !h-7 !text-xs px-2">✗ 1</span>
              </div>
            </div>
            <div class="flex items-center justify-between">
              <span class="text-sm">Mon · 19:00</span>
              <div class="flex gap-1">
                <span class="vote-pill vote-yes !min-w-0 !h-7 !text-xs px-2">✓ 1</span>
                <span class="vote-pill vote-maybe !min-w-0 !h-7 !text-xs px-2">~ 1</span>
                <span class="vote-pill vote-no !min-w-0 !h-7 !text-xs px-2">✗ 3</span>
              </div>
            </div>
          </div>
          <div class="mt-4 border-t-2 border-dashed border-[var(--color-ink-900)] pt-3 text-sm">
            <span class="font-serif italic">whenly suggests</span> Sun · 11:00
          </div>
        </div>
      </div>
    </div>
  </section>

  <!-- Features --------------------------------------------------------- -->
  <section class="container-wide py-10 sm:py-16">
    <div class="grid sm:grid-cols-2 lg:grid-cols-3 gap-4 sm:gap-5">
      <div v-for="f in features" :key="f.key" class="surface-soft p-5">
        <component :is="f.icon" class="w-8 h-8 mb-3 text-[var(--color-clay-600)]" stroke-width="1.75" />
        <h3 class="font-serif text-xl mb-1">{{ t(`landing.feature.${f.key}.title`) }}</h3>
        <p class="text-sm text-[var(--color-ink-100)] dark:text-[var(--color-sand-300)]">
          {{ t(`landing.feature.${f.key}.body`) }}
        </p>
      </div>
    </div>
  </section>

  <!-- How it works ----------------------------------------------------- -->
  <section id="how" class="container-wide py-12 sm:py-20">
    <h2 class="font-serif text-3xl sm:text-5xl mb-8">{{ t('landing.howItWorks.heading') }}</h2>
    <ol class="grid sm:grid-cols-3 gap-5">
      <li v-for="i in 3" :key="i" class="surface p-5">
        <span class="chip mb-3">0{{ i }}</span>
        <h3 class="font-serif text-xl">{{ t(`landing.howItWorks.step${i}.title`) }}</h3>
        <p class="mt-1 text-sm text-[var(--color-ink-100)] dark:text-[var(--color-sand-300)]">
          {{ t(`landing.howItWorks.step${i}.body`) }}
        </p>
      </li>
    </ol>
    <div class="mt-10 flex justify-center">
      <RouterLink to="/new" class="btn btn-accent text-base px-8 py-3">
        {{ t('landing.hero.cta') }}
      </RouterLink>
    </div>
  </section>
  </div>
</template>
