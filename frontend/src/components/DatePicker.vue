<script setup lang="ts">
import { ref, computed } from 'vue'
import { useI18n } from 'vue-i18n'
import { ChevronLeftIcon, ChevronRightIcon } from '@heroicons/vue/24/outline'
import {
  startOfMonth, endOfMonth, startOfWeek, endOfWeek,
  eachDayOfInterval, addMonths, subMonths, addWeeks,
  format, isSameMonth, isBefore, startOfDay, isSameDay, addDays
} from 'date-fns'

const props = defineProps<{
  selected: string[] // ISO date strings "YYYY-MM-DD"
}>()
const emit = defineEmits<{
  toggle: [date: string]
}>()

const { t, locale } = useI18n()

const today = startOfDay(new Date())
const viewMonth = ref(startOfMonth(today))

const weeks = computed(() => {
  const start = startOfWeek(startOfMonth(viewMonth.value), { weekStartsOn: 1 })
  const end = endOfWeek(endOfMonth(viewMonth.value), { weekStartsOn: 1 })
  const days = eachDayOfInterval({ start, end })
  const result: Date[][] = []
  for (let i = 0; i < days.length; i += 7) result.push(days.slice(i, i + 7))
  return result
})

const monthLabel = computed(() =>
  viewMonth.value.toLocaleDateString(locale.value, { month: 'long', year: 'numeric' })
)

// Quick-select chips: today + next 6 days (Mon-Sun labels)
const quickDays = computed(() => {
  return Array.from({ length: 7 }, (_, i) => addDays(today, i))
})

function isoDate(d: Date): string {
  return format(d, 'yyyy-MM-dd')
}

function isSelected(d: Date): boolean {
  return props.selected.includes(isoDate(d))
}

function isPast(d: Date): boolean {
  return isBefore(d, today)
}

function toggle(d: Date) {
  if (isPast(d)) return
  emit('toggle', isoDate(d))
}

function prevMonth() { viewMonth.value = subMonths(viewMonth.value, 1) }
function nextMonth() { viewMonth.value = addMonths(viewMonth.value, 1) }
function jumpNextWeek() { viewMonth.value = startOfMonth(addWeeks(today, 1)) }

function quickLabel(d: Date): string {
  if (isSameDay(d, today)) return t('create.datepicker.today')
  return d.toLocaleDateString(locale.value, { weekday: 'short' })
}
</script>

<template>
  <div class="space-y-3">
    <!-- Quick-select chips -->
    <div class="flex flex-wrap gap-2">
      <button
        v-for="d in quickDays"
        :key="isoDate(d)"
        type="button"
        class="px-3 py-1 rounded-full text-sm border transition-colors"
        :class="isSelected(d)
          ? 'bg-[var(--color-clay-500)] text-white border-[var(--color-clay-500)]'
          : 'border-[var(--color-sand-400)] text-[var(--color-ink-200)] hover:border-[var(--color-clay-400)]'"
        @click="toggle(d)"
      >
        {{ quickLabel(d) }}
      </button>
      <button
        type="button"
        class="px-3 py-1 rounded-full text-sm border border-dashed border-[var(--color-sand-400)] text-[var(--color-ink-100)] hover:border-[var(--color-clay-400)] transition-colors"
        @click="jumpNextWeek"
      >
        {{ t('create.datepicker.nextWeek') }}
      </button>
    </div>

    <!-- Calendar -->
    <div class="surface-soft rounded-xl p-4 select-none">
      <!-- Header -->
      <div class="flex items-center justify-between mb-3">
        <button type="button" class="btn btn-ghost p-1" @click="prevMonth">
          <ChevronLeftIcon class="w-5 h-5" />
        </button>
        <span class="font-medium capitalize">{{ monthLabel }}</span>
        <button type="button" class="btn btn-ghost p-1" @click="nextMonth">
          <ChevronRightIcon class="w-5 h-5" />
        </button>
      </div>

      <!-- Weekday headers -->
      <div class="grid grid-cols-7 mb-1">
        <div
          v-for="wd in t('create.datepicker.weekdays')"
          :key="wd"
          class="text-center text-xs text-[var(--color-ink-100)] font-medium py-1"
        >
          {{ wd }}
        </div>
      </div>

      <!-- Days -->
      <div v-for="(week, wi) in weeks" :key="wi" class="grid grid-cols-7">
        <button
          v-for="d in week"
          :key="isoDate(d)"
          type="button"
          class="aspect-square flex items-center justify-center text-sm rounded-full transition-colors mx-auto w-9 h-9"
          :class="[
            !isSameMonth(d, viewMonth) ? 'opacity-25 pointer-events-none' : '',
            isPast(d) ? 'opacity-30 cursor-not-allowed' : 'hover:bg-[var(--color-sand-300)]',
            isSelected(d) ? '!bg-[var(--color-clay-500)] text-white font-semibold' : '',
            isSameDay(d, today) && !isSelected(d) ? 'ring-1 ring-[var(--color-clay-400)]' : ''
          ]"
          :disabled="isPast(d) || !isSameMonth(d, viewMonth)"
          @click="toggle(d)"
        >
          {{ d.getDate() }}
        </button>
      </div>
    </div>
  </div>
</template>
