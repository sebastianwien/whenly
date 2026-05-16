<script setup lang="ts">
import { ref, computed } from 'vue'
import { useI18n } from 'vue-i18n'
import { useRouter } from 'vue-router'
import { PlusIcon, MinusCircleIcon, ArrowRightIcon } from '@heroicons/vue/24/outline'
import { createPoll } from '@/api/polls'
import type { CreatePollPayload, PollType } from '@/types'
import { ApiError } from '@/api/client'

const { t } = useI18n()
const router = useRouter()

interface OptionDraft {
  id: number
  start: string
  end: string
  label: string
}

let nextId = 1
const make = (): OptionDraft => ({ id: nextId++, start: '', end: '', label: '' })

const title = ref('')
const description = ref('')
const location = ref('')
const pollType = ref<PollType>('DATE_TIME')
const options = ref<OptionDraft[]>([make(), make()])

const allowMaybe = ref(true)
const hideResults = ref(false)
const requireName = ref(true)
const retentionDays = ref(30)

const submitting = ref(false)
const submitError = ref<string | null>(null)

const timezone = computed(() => Intl.DateTimeFormat().resolvedOptions().timeZone || 'UTC')

function addOption() {
  options.value.push(make())
}
function removeOption(id: number) {
  if (options.value.length <= 2) return
  options.value = options.value.filter(o => o.id !== id)
}

function localToInstant(local: string): string | null {
  if (!local) return null
  const d = new Date(local)
  if (isNaN(d.getTime())) return null
  return d.toISOString()
}

const valid = computed(() => {
  if (!title.value.trim()) return false
  if (options.value.length < 2) return false
  if (pollType.value === 'GENERIC') {
    return options.value.every(o => o.label.trim().length > 0)
  }
  return options.value.every(o => !!localToInstant(o.start))
})

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
      options: options.value.map(o => ({
        startAt: pollType.value === 'GENERIC' ? null : localToInstant(o.start),
        endAt: pollType.value === 'GENERIC' ? null : localToInstant(o.end),
        label: o.label.trim() || null
      })),
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
        <div>
          <label class="field-label">{{ t('create.field.type') }}</label>
          <div class="flex flex-wrap gap-2">
            <button v-for="opt in ['DATE_TIME', 'DATE_ONLY', 'GENERIC'] as PollType[]"
                    :key="opt"
                    type="button"
                    class="btn"
                    :class="pollType === opt ? 'btn-primary' : 'btn-outline'"
                    @click="pollType = opt">
              {{ t(opt === 'DATE_TIME' ? 'create.type.dateTime' : opt === 'DATE_ONLY' ? 'create.type.dateOnly' : 'create.type.generic') }}
            </button>
          </div>
        </div>
      </div>

      <!-- Options -->
      <div class="surface p-5 sm:p-6">
        <h2 class="font-serif text-2xl mb-4">{{ t('create.section.options') }}</h2>
        <ul class="space-y-3">
          <li v-for="(opt, idx) in options" :key="opt.id"
              class="surface-soft p-4 grid gap-3"
              :class="pollType === 'GENERIC' ? 'sm:grid-cols-[1fr_auto]' : 'sm:grid-cols-[1fr_1fr_auto]'">
            <template v-if="pollType === 'GENERIC'">
              <input v-model="opt.label" class="input"
                     :placeholder="t('create.options.labelPlaceholder')" maxlength="200" />
            </template>
            <template v-else>
              <div>
                <label class="field-label">{{ t('create.options.start') }} #{{ idx + 1 }}</label>
                <input v-model="opt.start" class="input"
                       :type="pollType === 'DATE_ONLY' ? 'date' : 'datetime-local'" required />
              </div>
              <div>
                <label class="field-label">{{ t('create.options.end') }}</label>
                <input v-model="opt.end" class="input"
                       :type="pollType === 'DATE_ONLY' ? 'date' : 'datetime-local'" />
              </div>
            </template>
            <button type="button" class="btn btn-ghost self-end justify-self-end text-sm h-10"
                    :disabled="options.length <= 2"
                    @click="removeOption(opt.id)">
              <MinusCircleIcon class="w-5 h-5" /> <span class="hidden sm:inline">{{ t('create.options.remove') }}</span>
            </button>
          </li>
        </ul>
        <button type="button" class="btn btn-outline mt-4" @click="addOption">
          <PlusIcon class="w-5 h-5" />
          {{ t('create.options.add') }}
        </button>
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
