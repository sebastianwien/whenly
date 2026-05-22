<script setup lang="ts">
import { useI18n } from 'vue-i18n'
const { t } = useI18n()
</script>

<template>
  <footer class="container-wide pb-8 pt-12">
    <div class="border-t-2 border-[var(--color-ink-900)] pt-4 flex flex-col sm:flex-row gap-3 sm:items-center sm:justify-between text-sm">
      <p class="text-[var(--color-ink-100)] dark:text-[var(--color-sand-300)]">
        {{ t('landing.footer.built') }}
      </p>
      <div class="flex gap-4">
        <a href="https://github.com/sebastianwien/whenly" class="underline underline-offset-2 hover:text-[var(--color-clay-600)]" rel="noreferrer noopener" target="_blank">
          {{ t('landing.footer.sourceCode') }}
        </a>
        <RouterLink to="/datenschutz" class="underline underline-offset-2 hover:text-[var(--color-clay-600)]">
          {{ t('landing.footer.privacy') }}
        </RouterLink>
        <RouterLink to="/impressum" class="underline underline-offset-2 hover:text-[var(--color-clay-600)]">
          {{ t('landing.footer.impressum') }}
        </RouterLink>
      </div>
    </div>
  </footer>
</template>
