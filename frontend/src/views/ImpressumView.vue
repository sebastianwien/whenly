<script setup lang="ts">
import { computed } from 'vue'
import { useI18n } from 'vue-i18n'

const { t } = useI18n()
const email = computed(() => ['impressum', 'whenly.app'].join('@'))
</script>

<template>
  <section class="container-narrow py-12">
    <h1 class="font-serif text-4xl sm:text-5xl">{{ t('impressum.heading') }}</h1>

    <div class="mt-10 space-y-8 text-base leading-relaxed">
      <div>
        <h2 class="font-serif text-2xl mb-2">{{ t('impressum.tmg') }}</h2>
        <p>
          Sebastian Wien<br />
          Cotheniusstrasse 5<br />
          10407 Berlin<br />
          Deutschland
        </p>
      </div>

      <div>
        <h2 class="font-serif text-2xl mb-2">{{ t('impressum.contact') }}</h2>
        <p>
          E-Mail:
          <a :href="`mailto:${email}`" class="underline underline-offset-2 hover:text-[var(--color-clay-600)]">
            {{ email }}
          </a>
        </p>
      </div>

      <div>
        <h2 class="font-serif text-2xl mb-2">{{ t('impressum.responsible') }}</h2>
        <p>
          Sebastian Wien<br />
          Cotheniusstrasse 5<br />
          10407 Berlin
        </p>
      </div>

      <div>
        <h2 class="font-serif text-2xl mb-2">{{ t('impressum.euDisputeHeading') }}</h2>
        <p>
          {{ t('impressum.euDispute') }}
          <a
            href="https://ec.europa.eu/consumers/odr/"
            target="_blank"
            rel="noopener noreferrer"
            class="underline underline-offset-2 hover:text-[var(--color-clay-600)]"
          >
            https://ec.europa.eu/consumers/odr/
          </a>
        </p>
        <p class="mt-3">{{ t('impressum.euDisputeEmail') }}</p>
      </div>

      <div>
        <h2 class="font-serif text-2xl mb-2">{{ t('impressum.arbitrationHeading') }}</h2>
        <p>{{ t('impressum.arbitration') }}</p>
      </div>

      <div>
        <h2 class="font-serif text-2xl mb-2">{{ t('impressum.liabilityContentHeading') }}</h2>
        <p>{{ t('impressum.liabilityContent1') }}</p>
        <p class="mt-3">{{ t('impressum.liabilityContent2') }}</p>
      </div>

      <div>
        <h2 class="font-serif text-2xl mb-2">{{ t('impressum.liabilityLinksHeading') }}</h2>
        <p>{{ t('impressum.liabilityLinks1') }}</p>
        <p class="mt-3">{{ t('impressum.liabilityLinks2') }}</p>
      </div>

      <div>
        <h2 class="font-serif text-2xl mb-2">{{ t('impressum.copyrightHeading') }}</h2>
        <p>{{ t('impressum.copyright1') }}</p>
        <p class="mt-3">{{ t('impressum.copyright2') }}</p>
      </div>
    </div>

    <div class="mt-12 pt-8 border-t-2 border-[var(--color-ink-900)]">
      <RouterLink to="/" class="underline underline-offset-2 hover:text-[var(--color-clay-600)]">
        {{ t('impressum.back') }}
      </RouterLink>
    </div>
  </section>
</template>
