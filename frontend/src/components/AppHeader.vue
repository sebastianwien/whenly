<script setup lang="ts">
import { useI18n } from 'vue-i18n'
import { setLocale } from '@/i18n'
import { ChevronDownIcon } from '@heroicons/vue/24/outline'
import { ref, onMounted, onBeforeUnmount } from 'vue'

const { t, locale } = useI18n()
const langOpen = ref(false)

function pick(code: 'de' | 'en') {
  setLocale(code)
  langOpen.value = false
}

function onDocClick(e: MouseEvent) {
  const root = (e.target as HTMLElement).closest?.('[data-lang-menu]')
  if (!root) langOpen.value = false
}

onMounted(() => document.addEventListener('click', onDocClick))
onBeforeUnmount(() => document.removeEventListener('click', onDocClick))
</script>

<template>
  <header class="container-wide pt-4 sm:pt-6">
    <div class="flex items-center justify-between gap-3">
      <RouterLink to="/" class="inline-flex items-baseline gap-1.5 group">
        <span class="font-serif text-2xl sm:text-3xl font-semibold">whenly</span>
        <span class="inline-block w-2 h-2 rounded-full bg-[var(--color-clay-500)] mb-1 group-hover:scale-125 transition-transform"></span>
      </RouterLink>
      <nav class="flex items-center gap-1 sm:gap-2">
        <RouterLink to="/about" class="btn btn-ghost px-3 sm:px-4 text-sm">
          {{ t('nav.about') }}
        </RouterLink>
        <div class="relative" data-lang-menu>
          <button class="btn btn-ghost px-3 text-sm" @click="langOpen = !langOpen">
            {{ locale.toUpperCase() }}
            <ChevronDownIcon class="w-4 h-4" />
          </button>
          <div v-if="langOpen"
               class="absolute right-0 mt-2 surface min-w-[8rem] z-30">
            <button class="w-full text-left px-4 py-2 hover:bg-[var(--color-sand-200)] text-sm"
                    @click="pick('en')">English</button>
            <button class="w-full text-left px-4 py-2 hover:bg-[var(--color-sand-200)] text-sm"
                    @click="pick('de')">Deutsch</button>
          </div>
        </div>
        <RouterLink to="/new" class="btn btn-primary px-3 sm:px-4 text-sm">
          {{ t('nav.new') }}
        </RouterLink>
      </nav>
    </div>
  </header>
</template>
