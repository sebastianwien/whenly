<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useI18n } from 'vue-i18n'
import { useRouter } from 'vue-router'
import { CheckBadgeIcon, TrashIcon, ArrowUturnLeftIcon } from '@heroicons/vue/24/outline'
import { getPollAsAdmin, finalizePoll, reopenPoll, deletePoll } from '@/api/polls'
import { formatOptionLabel } from '@/lib/format'
import type { Poll } from '@/types'
import { ApiError } from '@/api/client'

const props = defineProps<{ adminToken: string }>()
const { t, locale } = useI18n()
const router = useRouter()

const poll = ref<Poll | null>(null)
const loading = ref(true)
const error = ref<string | null>(null)
const acting = ref(false)

const counts = computed(() => {
  if (!poll.value) return new Map<string, { yes: number; maybe: number; no: number }>()
  const map = new Map<string, { yes: number; maybe: number; no: number }>()
  for (const o of poll.value.options) map.set(o.id, { yes: 0, maybe: 0, no: 0 })
  for (const p of poll.value.participants) {
    for (const v of p.votes) {
      const c = map.get(v.optionId)
      if (!c) continue
      if (v.value === 'YES') c.yes++
      else if (v.value === 'MAYBE') c.maybe++
      else c.no++
    }
  }
  return map
})

async function load() {
  try {
    poll.value = await getPollAsAdmin(props.adminToken)
    loading.value = false
  } catch (e) {
    loading.value = false
    if (e instanceof ApiError && e.status === 404) {
      router.replace({ name: 'not-found' })
      return
    }
    error.value = t('common.error')
  }
}

async function pick(optionId: string) {
  acting.value = true
  try { poll.value = await finalizePoll(props.adminToken, optionId) }
  finally { acting.value = false }
}

async function reopen() {
  acting.value = true
  try { poll.value = await reopenPoll(props.adminToken) }
  finally { acting.value = false }
}

async function remove() {
  if (!confirm(t('admin.confirmDelete'))) return
  acting.value = true
  try {
    await deletePoll(props.adminToken)
    router.push({ name: 'landing' })
  } finally { acting.value = false }
}

onMounted(load)
</script>

<template>
  <section class="container-narrow py-6 sm:py-10">
    <p v-if="loading" class="text-center py-12">{{ t('common.loading') }}</p>
    <p v-else-if="error" class="text-center py-12 text-[var(--color-clay-700)]">{{ error }}</p>

    <template v-else-if="poll">
      <p class="chip chip-accent">{{ t('admin.heading') }}</p>
      <h1 class="font-serif text-3xl sm:text-5xl mt-3">{{ poll.title }}</h1>
      <p class="mt-2 text-[var(--color-ink-100)] dark:text-[var(--color-sand-300)]">{{ t('admin.sub') }}</p>

      <div class="mt-6 flex flex-wrap gap-3">
        <RouterLink :to="{ name: 'poll', params: { publicId: poll.publicId } }" class="btn btn-outline">
          {{ t('created.next') }}
        </RouterLink>
        <button class="btn btn-ghost text-sm" :disabled="acting" @click="remove">
          <TrashIcon class="w-4 h-4" /> {{ t('admin.delete') }}
        </button>
      </div>

      <section class="mt-8">
        <h2 class="font-serif text-2xl mb-3">{{ t('poll.results.heading') }}</h2>
        <ul class="space-y-3">
          <li v-for="opt in poll.options" :key="opt.id"
              class="surface-soft p-4 grid gap-3 sm:grid-cols-[1fr_auto] items-center"
              :class="{ 'ring-2 ring-[var(--color-clay-500)] border-[var(--color-clay-500)]': poll.finalizedOptionId === opt.id }">
            <div>
              <p class="font-medium">{{ formatOptionLabel(opt, poll.type, poll.timezone, locale) }}</p>
              <p class="text-xs text-[var(--color-ink-100)] dark:text-[var(--color-sand-300)] mt-0.5">
                {{ counts.get(opt.id)?.yes }} yes · {{ counts.get(opt.id)?.maybe }} maybe · {{ counts.get(opt.id)?.no }} no
              </p>
            </div>
            <div class="justify-self-end">
              <button v-if="poll.finalizedOptionId === opt.id"
                      class="btn btn-outline" :disabled="acting" @click="reopen">
                <ArrowUturnLeftIcon class="w-4 h-4" /> {{ t('admin.reopen') }}
              </button>
              <button v-else class="btn btn-accent" :disabled="acting" @click="pick(opt.id)">
                <CheckBadgeIcon class="w-4 h-4" /> {{ t('admin.finalize') }}
              </button>
            </div>
          </li>
        </ul>
      </section>
    </template>
  </section>
</template>
