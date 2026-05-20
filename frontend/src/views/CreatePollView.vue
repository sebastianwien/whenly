<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { useI18n } from 'vue-i18n'
import { useRouter } from 'vue-router'
import { PlusIcon, ArrowRightIcon } from '@heroicons/vue/24/outline'
import { createPoll } from '@/api/polls'
import type { CreatePollPayload, PollType } from '@/types'
import { ApiError } from '@/api/client'
import DatePicker from '@/components/DatePicker.vue'
import TimeSlotPicker from '@/components/TimeSlotPicker.vue'
import OptionsList from '@/components/OptionsList.vue'
import type { OptionDraft } from '@/components/OptionsList.vue'

const { t } = useI18n()
const router = useRouter()

const title = ref('')
const description = ref('')
const location = ref('')
const pollType = ref<PollType>('DATE_ONLY')

// Date/time options state
let nextId = 1
const dateoptions = ref<OptionDraft[]>([])
const sameTimeForAll = ref(true)
const globalTime = ref<string | null>('19:00')

// Generic text options
const genericOptions = ref([{ id: nextId++, label: '' }, { id: nextId++, label: '' }])

const allowMaybe = ref(true)
const hideResults = ref(false)
const requireName = ref(true)
const retentionDays = ref(30)

const submitting = ref(false)
const submitError = ref<string | null>(null)

const timezone = computed(() => Intl.DateTimeFormat().resolvedOptions().timeZone || 'UTC')

const isDateType = computed(() => pollType.value === 'DATE_TIME' || pollType.value === 'DATE_ONLY')
const isDateTime = computed(() => pollType.value === 'DATE_TIME')

// Reset time overrides when switching away from DATE_TIME
watch(pollType, (val) => {
  if (val !== 'DATE_TIME') {
    dateoptions.value.forEach(o => { o.timeOverridden = false })
  }
})

function toggleDate(iso: string) {
  const idx = dateoptions.value.findIndex(o => o.date === iso)
  if (idx >= 0) {
    dateoptions.value.splice(idx, 1)
  } else {
    dateoptions.value.push({ id: nextId++, date: iso, time: null, timeOverridden: false })
    dateoptions.value.sort((a, b) => a.date.localeCompare(b.date))
  }
}

function removeOption(id: number) {
  dateoptions.value = dateoptions.value.filter(o => o.id !== id)
}

function setOptionTime(id: number, time: string) {
  const opt = dateoptions.value.find(o => o.id === id)
  if (!opt) return
  opt.time = time
  opt.timeOverridden = true
}

function addGeneric() {
  genericOptions.value.push({ id: nextId++, label: '' })
}
function removeGeneric(id: number) {
  if (genericOptions.value.length <= 2) return
  genericOptions.value = genericOptions.value.filter(o => o.id !== id)
}

const selectedDates = computed(() => dateoptions.value.map(o => o.date))

const valid = computed(() => {
  if (!title.value.trim()) return false
  if (pollType.value === 'GENERIC') {
    return genericOptions.value.length >= 2 && genericOptions.value.every(o => o.label.trim())
  }
  if (dateoptions.value.length < 2) return false
  if (isDateTime.value) {
    return dateoptions.value.every(o => {
      const time = o.timeOverridden ? o.time : globalTime.value
      return !!time
    })
  }
  return true
})

function resolveTime(opt: OptionDraft): string | null {
  if (!isDateTime.value) return null
  return opt.timeOverridden ? opt.time : globalTime.value
}

async function submit() {
  submitError.value = null
  if (!valid.value) {
    submitError.value = t('create.errors.titleRequired')
    return
  }
  submitting.value = true
  try {
    const payload: CreatePollPayload = {
      title: title.value.trim(),
      description: description.value.trim() || null,
      location: location.value.trim() || null,
      type: pollType.value,
      timezone: timezone.value,
      options: pollType.value === 'GENERIC'
        ? genericOptions.value.map(o => ({ startAt: null, endAt: null, label: o.label.trim() }))
        : dateoptions.value.map(o => {
            const time = resolveTime(o)
            const startAt = time ? `${o.date}T${time}:00` : `${o.date}T00:00:00`
            return { startAt: new Date(startAt).toISOString(), endAt: null, label: null }
          }),
      settings: {
        allowMaybe: allowMaybe.value,
        hideResultsUntilVoted: hideResults.value,
        requireParticipantName: requireName.value,
        allowMultiple: true
      },
      retentionDays: retentionDays.value
    }
    const res = await createPoll(payload)
    router.push({ name: 'created', params: { adminToken: res.adminToken } })
  } catch (e) {
    if (e instanceof ApiError) submitError.value = e.message
    else submitError.value = t('common.error')
  } finally {
    submitting.value = false
  }
}
</script>

<template>
  <section class="container-narrow py-8 sm:py-12">
    <header class="mb-8">
      <p class="chip">{{ t('create.title') }}</p>
      <h1 class="font-serif text-4xl sm:text-5xl mt-3">{{ t('create.section.basics') }}</h1>
      <p class="mt-2 text-[var(--color-ink-100)] dark:text-[var(--color-sand-300)]">{{ t('create.sub') }}</p>
    </header>

    <form class="space-y-10" @submit.prevent="submit">
      <!-- Basics -->
      <div class="surface p-5 sm:p-6 space-y-5">
        <div>
          <label class="field-label" for="poll-title">{{ t('create.field.title') }}</label>
          <input id="poll-title" v-model="title" class="input" type="text"
                 maxlength="200" :placeholder="t('create.field.titlePlaceholder')" required />
        </div>
        <div>
          <label class="field-label" for="poll-description">{{ t('create.field.description') }}</label>
          <textarea id="poll-description" v-model="description" class="textarea"
                    maxlength="5000" :placeholder="t('create.field.descriptionPlaceholder')" />
        </div>
        <div>
          <label class="field-label" for="poll-location">{{ t('create.field.location') }}</label>
          <input id="poll-location" v-model="location" class="input" type="text"
                 maxlength="255" :placeholder="t('create.field.locationPlaceholder')" />
        </div>

        <!-- Poll type -->
        <div>
          <label class="field-label">{{ t('create.field.type') }}</label>
          <div class="flex flex-wrap gap-2">
            <button v-for="opt in (['DATE_ONLY', 'DATE_TIME', 'GENERIC'] as PollType[])"
                    :key="opt"
                    type="button"
                    class="btn"
                    :class="pollType === opt ? 'btn-primary' : 'btn-outline'"
                    @click="pollType = opt">
              {{ t(opt === 'DATE_TIME' ? 'create.type.dateTime' : opt === 'DATE_ONLY' ? 'create.type.dateOnly' : 'create.type.generic') }}
            </button>
          </div>
        </div>

        <!-- Time controls (slide in for DATE_TIME) -->
        <Transition
          enter-active-class="transition-all duration-300 ease-out overflow-hidden"
          leave-active-class="transition-all duration-200 ease-in overflow-hidden"
          enter-from-class="max-h-0 opacity-0"
          enter-to-class="max-h-40 opacity-100"
          leave-from-class="max-h-40 opacity-100"
          leave-to-class="max-h-0 opacity-0"
        >
          <div v-if="isDateTime" class="space-y-2 pt-1">
            <label class="flex items-center gap-2 cursor-pointer">
              <input type="checkbox" v-model="sameTimeForAll" class="w-4 h-4 accent-[var(--color-clay-500)]" />
              <span class="text-sm">{{ t('create.time.sameForAll') }}</span>
            </label>
            <Transition
              enter-active-class="transition-all duration-200 ease-out overflow-hidden"
              leave-active-class="transition-all duration-150 ease-in overflow-hidden"
              enter-from-class="max-h-0 opacity-0"
              enter-to-class="max-h-20 opacity-100"
              leave-from-class="max-h-20 opacity-100"
              leave-to-class="max-h-0 opacity-0"
            >
              <TimeSlotPicker
                v-if="sameTimeForAll"
                v-model="globalTime"
              />
            </Transition>
          </div>
        </Transition>
      </div>

      <!-- Options -->
      <div class="surface p-5 sm:p-6 space-y-4">
        <h2 class="font-serif text-2xl">{{ t('create.section.options') }}</h2>

        <!-- Date picker (DATE_ONLY or DATE_TIME) -->
        <template v-if="isDateType">
          <DatePicker
            :selected="selectedDates"
            @toggle="toggleDate"
          />
          <div v-if="dateoptions.length > 0">
            <p class="field-label mb-2">{{ t('create.options.selected') }}</p>
            <OptionsList
              :options="dateoptions"
              :show-time="isDateTime"
              :global-time="globalTime"
              :same-time-for-all="sameTimeForAll"
              @remove="removeOption"
              @set-time="setOptionTime"
            />
          </div>
          <p v-else class="text-sm text-[var(--color-ink-100)]">
            {{ t('create.options.noSelection') }}
          </p>
        </template>

        <!-- Generic text options -->
        <template v-else>
          <ul class="space-y-3">
            <li v-for="opt in genericOptions" :key="opt.id"
                class="surface-soft p-4 flex items-center gap-3">
              <input v-model="opt.label" class="input flex-1"
                     :placeholder="t('create.options.labelPlaceholder')" maxlength="200" />
              <button type="button" class="btn btn-ghost text-sm"
                      :disabled="genericOptions.length <= 2"
                      @click="removeGeneric(opt.id)">
                <span class="hidden sm:inline">{{ t('create.options.remove') }}</span>
              </button>
            </li>
          </ul>
          <button type="button" class="btn btn-outline mt-2" @click="addGeneric">
            <PlusIcon class="w-5 h-5" />
            {{ t('create.options.add') }}
          </button>
        </template>
      </div>

      <!-- Settings -->
      <div class="surface p-5 sm:p-6 space-y-4">
        <h2 class="font-serif text-2xl mb-2">{{ t('create.section.settings') }}</h2>
        <label class="flex items-start gap-3 cursor-pointer">
          <input type="checkbox" v-model="allowMaybe" class="mt-1 w-5 h-5 accent-[var(--color-clay-500)]" />
          <span>
            <span class="font-medium">{{ t('create.settings.allowMaybe.title') }}</span>
            <span class="block text-sm text-[var(--color-ink-100)] dark:text-[var(--color-sand-300)]">
              {{ t('create.settings.allowMaybe.body') }}
            </span>
          </span>
        </label>
        <label class="flex items-start gap-3 cursor-pointer">
          <input type="checkbox" v-model="hideResults" class="mt-1 w-5 h-5 accent-[var(--color-clay-500)]" />
          <span>
            <span class="font-medium">{{ t('create.settings.hideResults.title') }}</span>
            <span class="block text-sm text-[var(--color-ink-100)] dark:text-[var(--color-sand-300)]">
              {{ t('create.settings.hideResults.body') }}
            </span>
          </span>
        </label>
        <label class="flex items-start gap-3 cursor-pointer">
          <input type="checkbox" v-model="requireName" class="mt-1 w-5 h-5 accent-[var(--color-clay-500)]" />
          <span>
            <span class="font-medium">{{ t('create.settings.requireName.title') }}</span>
            <span class="block text-sm text-[var(--color-ink-100)] dark:text-[var(--color-sand-300)]">
              {{ t('create.settings.requireName.body') }}
            </span>
          </span>
        </label>
        <div class="pt-2">
          <label class="field-label" for="retention">{{ t('create.settings.retention.title') }}</label>
          <div class="flex items-center gap-2">
            <input id="retention" v-model.number="retentionDays" type="number" min="1" max="365"
                   class="input max-w-[7rem]" />
            <span class="text-sm">{{ t('create.settings.retentionUnit') }}</span>
          </div>
          <p class="text-sm text-[var(--color-ink-100)] dark:text-[var(--color-sand-300)] mt-1">
            {{ t('create.settings.retention.body') }}
          </p>
        </div>
      </div>

      <!-- Submit -->
      <div class="flex flex-col sm:flex-row items-center gap-4">
        <button type="submit" class="btn btn-accent text-base px-6 py-3"
                :disabled="!valid || submitting">
          {{ submitting ? t('create.submitting') : t('create.submit') }}
          <ArrowRightIcon v-if="!submitting" class="w-5 h-5" />
        </button>
        <p v-if="submitError" class="text-sm text-[var(--color-clay-700)] font-medium">
          {{ submitError }}
        </p>
      </div>
    </form>
  </section>
</template>
