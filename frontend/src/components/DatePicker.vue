<script setup lang="ts">
import { ref, computed } from 'vue'
import { useI18n } from 'vue-i18n'
import { ChevronLeftIcon, ChevronRightIcon } from '@heroicons/vue/24/outline'
import {
  startOfWeek, addDays, addWeeks, format,
  isBefore, startOfDay, isSameDay, getMonth
} from 'date-fns'

const props = defineProps<{
  selected: string[]
}>()
const emit = defineEmits<{
  toggle: [date: string]
}>()

const { t, tm, locale } = useI18n()

const WEEKS_SHOWN = 8
const today = startOfDay(new Date())
const startMonday = startOfWeek(today, { weekStartsOn: 1 })
const offset = ref(0)
const direction = ref<'left' | 'right'>('right')

const weeks = computed(() => {
  const monday = addWeeks(startMonday, offset.value)
  return Array.from({ length: WEEKS_SHOWN }, (_, wi) =>
    Array.from({ length: 7 }, (_, di) => addDays(addWeeks(monday, wi), di))
  )
})

function prev() {
  if (offset.value <= 0) return
  direction.value = 'left'
  offset.value--
}
function next() {
  direction.value = 'right'
  offset.value++
}

const quickDays = computed(() =>
  Array.from({ length: 7 }, (_, i) => addDays(today, i))
)

const weekdayLabels = computed(() => tm('create.datepicker.weekdays') as string[])

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

function monthLabelForWeek(week: Date[], wi: number): string | null {
  if (wi === 0) return week[0].toLocaleDateString(locale.value, { month: 'long', year: 'numeric' })
  if (getMonth(week[0]) !== getMonth(weeks.value[wi - 1][0])) {
    return week[0].toLocaleDateString(locale.value, { month: 'long', year: 'numeric' })
  }
  return null
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

    <!-- Rolling calendar -->
    <div class="surface-soft rounded-xl p-4 select-none overflow-hidden">
      <!-- Navigation -->
      <div class="flex items-center justify-between mb-3">
        <button type="button" class="btn btn-ghost p-1" :disabled="offset === 0" @click="prev">
          <ChevronLeftIcon class="w-5 h-5" />
        </button>
        <span class="text-sm text-[var(--color-sand-400)] font-medium capitalize">
          {{ weeks[0][0].toLocaleDateString(locale, { month: 'long', year: 'numeric' }) }}
        </span>
        <button type="button" class="btn btn-ghost p-1" @click="next">
          <ChevronRightIcon class="w-5 h-5" />
        </button>
      </div>

      <!-- Weekday headers (static, outside slide) -->
      <div class="grid grid-cols-7 mb-1">
        <div
          v-for="wd in weekdayLabels"
          :key="wd"
          class="text-center text-xs text-[var(--color-sand-400)] font-medium py-1"
        >
          {{ wd }}
        </div>
      </div>

      <!-- Animated weeks -->
      <Transition :name="direction === 'right' ? 'slide-left' : 'slide-right'" mode="out-in">
        <div :key="offset">
          <div v-for="(week, wi) in weeks" :key="wi">
            <div
              v-if="monthLabelForWeek(week, wi)"
              class="text-xs text-[var(--color-sand-400)] font-semibold capitalize pt-2 pb-1"
              :class="wi > 0 ? 'border-t border-[var(--color-sand-300)] mt-1' : ''"
            >
              {{ monthLabelForWeek(week, wi) }}
            </div>
            <div class="grid grid-cols-7">
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
          </div>
        </div>
      </Transition>
    </div>
  </div>
</template>

<style scoped>
.slide-left-enter-active,
.slide-left-leave-active,
.slide-right-enter-active,
.slide-right-leave-active {
  transition: transform 0.22s ease, opacity 0.22s ease;
}

.slide-left-enter-from  { transform: translateX(40px); opacity: 0; }
.slide-left-leave-to    { transform: translateX(-40px); opacity: 0; }

.slide-right-enter-from { transform: translateX(-40px); opacity: 0; }
.slide-right-leave-to   { transform: translateX(40px); opacity: 0; }
</style>
