<script setup lang="ts">
import { computed } from 'vue'
import { useI18n } from 'vue-i18n'
import type { Poll, PollOption, VoteValue } from '@/types'
import { formatOptionLabel } from '@/lib/format'

interface Props {
  poll: Poll
  myVotes: Map<string, VoteValue>
  editable: boolean
}

const props = defineProps<Props>()
const emit = defineEmits<{ (e: 'change', optionId: string, value: VoteValue): void }>()

const { t, locale } = useI18n()

const counts = computed(() => {
  const map = new Map<string, { yes: number; maybe: number; no: number }>()
  for (const o of props.poll.options) map.set(o.id, { yes: 0, maybe: 0, no: 0 })
  for (const p of props.poll.participants) {
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

function valueFor(optionId: string): VoteValue | undefined {
  return props.myVotes.get(optionId)
}

function set(optionId: string, value: VoteValue) {
  if (!props.editable) return
  emit('change', optionId, value)
}

function labelFor(o: PollOption) {
  return formatOptionLabel(o, props.poll.type, props.poll.timezone, locale.value)
}
</script>

<template>
  <ul class="space-y-2">
    <li v-for="opt in poll.options" :key="opt.id"
        class="surface-soft px-3 py-2.5 sm:px-4 sm:py-3 grid gap-2 sm:grid-cols-[1fr_auto] items-center"
        :class="{ 'ring-2 ring-[var(--color-clay-500)] border-[var(--color-clay-500)]': poll.finalizedOptionId === opt.id }">
      <div>
        <p class="font-medium text-base leading-tight">{{ labelFor(opt) }}</p>
        <div class="flex gap-1.5 mt-1 flex-wrap">
          <span class="inline-flex items-center gap-1 text-xs px-1.5 py-0.5 rounded bg-[var(--color-sage-400)]/20 text-[var(--color-sage-600)] dark:bg-[var(--color-sage-600)]/30 dark:text-[var(--color-sage-400)] font-medium">
            ✓ {{ counts.get(opt.id)?.yes ?? 0 }}
          </span>
          <span v-if="poll.settings.allowMaybe"
                class="inline-flex items-center gap-1 text-xs px-1.5 py-0.5 rounded bg-[var(--color-citrus-400)]/20 text-[var(--color-citrus-500)] dark:bg-[var(--color-citrus-400)]/20 dark:text-[var(--color-citrus-400)] font-medium">
            ~ {{ counts.get(opt.id)?.maybe ?? 0 }}
          </span>
          <span class="inline-flex items-center gap-1 text-xs px-1.5 py-0.5 rounded bg-[var(--color-clay-300)]/20 text-[var(--color-clay-700)] dark:bg-[var(--color-clay-300)]/20 dark:text-[var(--color-clay-300)] font-medium">
            ✗ {{ counts.get(opt.id)?.no ?? 0 }}
          </span>
        </div>
      </div>
      <div class="flex gap-1.5 shrink-0 justify-end sm:gap-2">
        <!-- Mobile-friendly cycle button + 3 explicit pills -->
        <button type="button"
                class="vote-pill !h-9 !min-w-[2.75rem] sm:!h-10 sm:!min-w-[3.25rem]"
                :class="valueFor(opt.id) === 'YES' ? 'vote-yes' : 'vote-empty'"
                :disabled="!editable"
                :aria-label="t('poll.votes.yes')"
                @click="set(opt.id, 'YES')">
          ✓
        </button>
        <button v-if="poll.settings.allowMaybe"
                type="button"
                class="vote-pill !h-9 !min-w-[2.75rem] sm:!h-10 sm:!min-w-[3.25rem]"
                :class="valueFor(opt.id) === 'MAYBE' ? 'vote-maybe' : 'vote-empty'"
                :disabled="!editable"
                :aria-label="t('poll.votes.maybe')"
                @click="set(opt.id, 'MAYBE')">
          ~
        </button>
        <button type="button"
                class="vote-pill !h-9 !min-w-[2.75rem] sm:!h-10 sm:!min-w-[3.25rem]"
                :class="valueFor(opt.id) === 'NO' ? 'vote-no' : 'vote-empty'"
                :disabled="!editable"
                :aria-label="t('poll.votes.no')"
                @click="set(opt.id, 'NO')">
          ✗
        </button>
      </div>
    </li>
  </ul>
</template>
