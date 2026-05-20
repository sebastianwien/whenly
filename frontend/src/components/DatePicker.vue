<script setup lang="ts">
import { ref, computed } from 'vue'
import { useI18n } from 'vue-i18n'
import { ChevronLeftIcon, ChevronRightIcon } from '@heroicons/vue/24/outline'
import {
  startOfWeek, addDays, addWeeks, format,
  isBefore, startOfDay, isSameDay, getMonth, getYear
} from 'date-fns'

const props = defineProps<{
  selected: string[]
}>()
const emit = defineEmits<{
  toggle: [date: string]
}>()

const { t, tm, locale } = useI18n()

const ROW_H = 36        // px - matches h-9
const VISIBLE_WEEKS = 8
const TOTAL_WEEKS = 52  // rendered in the tape
const today = startOfDay(new Date())
const startMonday = startOfWeek(today, { weekStartsOn: 1 })
const offset = ref(0)   // week index (0 = this week at top)

// All 52 weeks pre-rendered
const allWeeks = Array.from({ length: TOTAL_WEEKS }, (_, wi) =>
  Array.from({ length: 7 }, (_, di) => addDays(addWeeks(startMonday, wi), di))
)

const weekdayLabels = computed(() => tm('create.datepicker.weekdays') as string[])

// Month label above a week row when month changes
function monthLabel(wi: number): string | null {
  if (wi === 0) return allWeeks[0][0].toLocaleDateString(locale.value, { month: 'long', year: 'numeric' })
  const prev = allWeeks[wi - 1][0]
  const cur  = allWeeks[wi][0]
  if (getMonth(cur) !== getMonth(prev) || getYear(cur) !== getYear(prev)) {
    return cur.toLocaleDateString(locale.value, { month: 'long', year: 'numeric' })
  }
  return null
}

// Header label: month of first visible week
const headerLabel = computed(() =>
  allWeeks[offset.value][0].toLocaleDateString(locale.value, { month: 'long', year: 'numeric' })
)

// translateY: each week = ROW_H px, month labels add 24px when present
function rowTop(wi: number): number {
  let y = 0
  for (let i = 0; i < wi; i++) {
    y += ROW_H
    if (monthLabel(i + 1)) y += 24
  }
  return y
}

// Height of the visible window (VISIBLE_WEEKS rows + their month labels)
const visibleHeight = computed(() => {
  let h = 0
  for (let i = offset.value; i < offset.value + VISIBLE_WEEKS; i++) {
    h += ROW_H
    if (i + 1 < TOTAL_WEEKS && monthLabel(i + 1)) h += 24
  }
  return h
})

// Offset in px for the tape
const tapeY = computed(() => rowTop(offset.value))

function prev() { if (offset.value > 0) offset.value = Math.max(0, offset.value - 2) }
function next() { if (offset.value + VISIBLE_WEEKS < TOTAL_WEEKS) offset.value = Math.min(TOTAL_WEEKS - VISIBLE_WEEKS, offset.value + 2) }

// Quick-select chips
const quickDays = computed(() =>
  Array.from({ length: 7 }, (_, i) => addDays(today, i))
)

function isoDate(d: Date): string { return format(d, 'yyyy-MM-dd') }
function isSelected(d: Date): boolean { return props.selected.includes(isoDate(d)) }
function isPast(d: Date): boolean { return isBefore(d, today) }
function toggle(d: Date) {
  if (isPast(d)) return
  emit('toggle', isoDate(d))
}
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
    </div>

    <!-- Calendar -->
    <div class="surface-soft rounded-xl p-4 select-none">
      <!-- Navigation header -->
      <div class="flex items-center justify-between mb-3">
        <button type="button" class="btn btn-ghost p-1" :disabled="offset === 0" @click="prev">
          <ChevronLeftIcon class="w-5 h-5" />
        </button>
        <span class="text-sm text-[var(--color-sand-400)] font-medium capitalize">
          {{ headerLabel }}
        </span>
        <button type="button" class="btn btn-ghost p-1" :disabled="offset + VISIBLE_WEEKS >= TOTAL_WEEKS" @click="next">
          <ChevronRightIcon class="w-5 h-5" />
        </button>
      </div>

      <!-- Weekday headers (fixed) -->
      <div class="grid grid-cols-7 mb-1">
        <div
          v-for="wd in weekdayLabels"
          :key="wd"
          class="text-center text-xs text-[var(--color-sand-400)] font-medium py-1"
        >
          {{ wd }}
        </div>
      </div>

      <!-- Tape window -->
      <div class="overflow-hidden relative" :style="{ height: visibleHeight + 'px' }">
        <div
          class="absolute w-full"
          style="transition: transform 0.28s cubic-bezier(0.4, 0, 0.2, 1)"
          :style="{ transform: `translateY(-${tapeY}px)` }"
        >
          <template v-for="(week, wi) in allWeeks" :key="wi">
            <!-- Month label -->
            <div
              v-if="monthLabel(wi)"
              class="text-xs text-[var(--color-sand-400)] font-semibold capitalize pb-1"
              :class="wi > 0 ? 'border-t border-[var(--color-sand-300)] pt-2 mt-1' : ''"
              style="height: 24px"
            >
              {{ monthLabel(wi) }}
            </div>
            <!-- Week row -->
            <div class="grid grid-cols-7" :style="{ height: ROW_H + 'px' }">
              <button
                v-for="d in week"
                :key="isoDate(d)"
                type="button"
                class="flex items-center justify-center text-sm rounded-full transition-colors mx-auto w-9 h-9"
                :class="[
                  isPast(d) ? 'opacity-20 cursor-not-allowed pointer-events-none' : 'hover:bg-[var(--color-sand-300)]',
                  isSelected(d) ? '!bg-[var(--color-clay-500)] text-white font-semibold' : '',
                  isSameDay(d, today) && !isSelected(d) ? 'ring-1 ring-[var(--color-clay-400)]' : ''
                ]"
                :disabled="isPast(d)"
                @click="toggle(d)"
              >
                {{ d.getDate() }}
              </button>
            </div>
          </template>
        </div>
      </div>
    </div>
  </div>
</template>
