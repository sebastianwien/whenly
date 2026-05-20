<script setup lang="ts">
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { XMarkIcon, PencilSquareIcon, CheckIcon } from '@heroicons/vue/24/outline'
import TimeSlotPicker from './TimeSlotPicker.vue'

export interface OptionDraft {
  id: number
  date: string        // "YYYY-MM-DD"
  time: string | null // "HH:MM" | null
  timeOverridden: boolean
}

const props = defineProps<{
  options: OptionDraft[]
  showTime: boolean       // DATE_TIME mode
  globalTime: string | null
  sameTimeForAll: boolean
}>()

const emit = defineEmits<{
  remove: [id: number]
  setTime: [id: number, time: string]
}>()

const { locale } = useI18n()

const editingId = ref<number | null>(null)

function formatDate(iso: string): string {
  const d = new Date(iso + 'T00:00:00')
  return d.toLocaleDateString(locale.value, { weekday: 'short', day: 'numeric', month: 'short' })
}

function displayTime(opt: OptionDraft): string | null {
  if (!props.showTime) return null
  return opt.timeOverridden ? opt.time : props.globalTime
}

function startEdit(id: number) {
  editingId.value = id
}

function commitEdit(id: number, time: string) {
  emit('setTime', id, time)
  editingId.value = null
}
</script>

<template>
  <div v-if="options.length === 0" class="text-sm text-[var(--color-ink-100)]">
    {{ $t('create.options.noSelection') }}
  </div>
  <ul v-else class="space-y-2">
    <li
      v-for="opt in options"
      :key="opt.id"
      class="surface-soft rounded-lg px-4 py-2"
    >
      <!-- Normal row -->
      <div v-if="editingId !== opt.id" class="flex items-center gap-3 min-h-[2.25rem]">
        <span class="flex-1 text-sm font-medium">{{ formatDate(opt.date) }}</span>

        <template v-if="showTime">
          <span class="text-sm tabular-nums text-[var(--color-ink-200)]">
            {{ displayTime(opt) ?? '—' }}
          </span>
          <!-- Dot indicator for overridden time -->
          <span
            v-if="sameTimeForAll && opt.timeOverridden"
            class="w-2 h-2 rounded-full bg-[var(--color-clay-500)]"
            :title="$t('create.time.override')"
          />
          <button type="button" class="btn btn-ghost p-1" @click="startEdit(opt.id)">
            <PencilSquareIcon class="w-4 h-4" />
          </button>
        </template>

        <button type="button" class="btn btn-ghost p-1 text-[var(--color-ink-100)]" @click="emit('remove', opt.id)">
          <XMarkIcon class="w-4 h-4" />
        </button>
      </div>

      <!-- Inline edit row -->
      <div v-else class="space-y-2 py-1">
        <div class="flex items-center gap-2">
          <span class="text-sm font-medium flex-1">{{ formatDate(opt.date) }}</span>
          <button type="button" class="btn btn-ghost p-1 text-[var(--color-clay-500)]" @click="editingId = null">
            <CheckIcon class="w-4 h-4" />
          </button>
        </div>
        <TimeSlotPicker
          :model-value="displayTime(opt)"
          @update:model-value="commitEdit(opt.id, $event)"
        />
      </div>
    </li>
  </ul>
</template>
