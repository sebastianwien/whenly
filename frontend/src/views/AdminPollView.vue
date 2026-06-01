<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useI18n } from 'vue-i18n'
import { useRouter } from 'vue-router'
import { CheckBadgeIcon, TrashIcon, ArrowUturnLeftIcon, PencilIcon, CheckIcon, XMarkIcon } from '@heroicons/vue/24/outline'
import { useSeoHead } from '@/composables/useSeoHead'

useSeoHead({ title: 'Admin - whenly', noindex: true })
import { getPollAsAdmin, finalizePoll, reopenPoll, deletePoll, deleteAdminOption, updateAdminOptionLabel } from '@/api/polls'
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
const editingOptionId = ref<string | null>(null)
const editLabel = ref('')

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

async function removeOption(optionId: string) {
  if (!confirm(t('admin.confirmDeleteOption'))) return
  acting.value = true
  try {
    await deleteAdminOption(props.adminToken, optionId)
    poll.value = await getPollAsAdmin(props.adminToken)
  } finally { acting.value = false }
}

function startEdit(optionId: string, currentLabel: string | null) {
  editingOptionId.value = optionId
  editLabel.value = currentLabel ?? ''
}

function cancelEdit() {
  editingOptionId.value = null
  editLabel.value = ''
}

async function saveEdit(optionId: string) {
  if (!editLabel.value.trim()) return
  acting.value = true
  try {
    const updated = await updateAdminOptionLabel(props.adminToken, optionId, editLabel.value)
    if (poll.value) {
      const idx = poll.value.options.findIndex(o => o.id === optionId)
      if (idx !== -1) poll.value.options[idx] = { ...poll.value.options[idx], label: updated.label }
    }
    editingOptionId.value = null
    editLabel.value = ''
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
      <h1 class="font-serif text-2xl sm:text-5xl mt-3">{{ poll.title }}</h1>
      <p class="mt-1.5 text-[var(--color-ink-100)] dark:text-[var(--color-sand-300)]">{{ t('admin.sub') }}</p>

      <div class="mt-6 flex flex-wrap gap-3">
        <RouterLink :to="{ name: 'poll', params: { publicId: poll.publicId } }" class="btn btn-outline">
          {{ t('created.next') }}
        </RouterLink>
        <button class="btn btn-ghost text-sm" :disabled="acting" @click="remove">
          <TrashIcon class="w-4 h-4" /> {{ t('admin.delete') }}
        </button>
      </div>

      <section class="mt-6 sm:mt-8">
        <h2 class="font-serif text-xl sm:text-2xl mb-2 sm:mb-3">{{ t('poll.results.heading') }}</h2>
        <ul class="space-y-2">
          <li v-for="opt in poll.options" :key="opt.id"
              class="surface-soft p-3 sm:p-4 grid gap-2 sm:grid-cols-[1fr_auto] items-center"
              :class="{ 'ring-2 ring-[var(--color-clay-500)] border-[var(--color-clay-500)]': poll.finalizedOptionId === opt.id }">
            <div>
              <div v-if="editingOptionId === opt.id" class="flex items-center gap-2">
                <input
                  v-model="editLabel"
                  class="input text-sm flex-1 min-w-0"
                  maxlength="200"
                  @keydown.enter="saveEdit(opt.id)"
                  @keydown.escape="cancelEdit"
                  autofocus
                />
                <button class="btn btn-accent btn-sm" :disabled="acting || !editLabel.trim()" @click="saveEdit(opt.id)">
                  <CheckIcon class="w-4 h-4" />
                </button>
                <button class="btn btn-ghost btn-sm" :disabled="acting" @click="cancelEdit">
                  <XMarkIcon class="w-4 h-4" />
                </button>
              </div>
              <template v-else>
                <p class="font-medium">{{ formatOptionLabel(opt, poll.type, poll.timezone, locale) }}</p>
                <p class="text-xs text-[var(--color-ink-100)] dark:text-[var(--color-sand-300)] mt-0.5">
                  {{ counts.get(opt.id)?.yes }} yes · {{ counts.get(opt.id)?.maybe }} maybe · {{ counts.get(opt.id)?.no }} no
                </p>
              </template>
            </div>
            <div v-if="editingOptionId !== opt.id" class="justify-self-end flex gap-1.5 sm:gap-2">
              <button
                v-if="opt.label !== null"
                class="btn btn-ghost text-sm"
                :disabled="acting"
                :aria-label="t('admin.editOption')"
                @click="startEdit(opt.id, opt.label)"
              >
                <PencilIcon class="w-4 h-4" />
              </button>
              <button v-if="poll.type === 'OPEN'" class="btn btn-ghost text-sm" :disabled="acting" @click="removeOption(opt.id)">
                <TrashIcon class="w-4 h-4" />
              </button>
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

      <section v-if="poll.participants.length > 0" class="mt-6 sm:mt-8">
        <div class="flex items-baseline gap-3 mb-2 sm:mb-3">
          <h2 class="font-serif text-xl sm:text-2xl">{{ t('poll.results.heading') }}</h2>
          <span class="text-sm text-[var(--color-ink-100)] dark:text-[var(--color-sand-300)]">
            {{ poll.participantCount }} {{ t('poll.results.participants') }}
          </span>
        </div>
        <div class="overflow-x-auto -mx-1">
          <table class="w-full border-collapse text-sm">
            <thead>
              <tr>
                <th class="text-left p-2 sticky left-0 bg-[var(--color-sand-100)] dark:bg-[var(--color-ink-900)]">&nbsp;</th>
                <th v-for="opt in poll.options" :key="opt.id" class="p-2 text-left font-medium font-serif">
                  {{ formatOptionLabel(opt, poll.type, poll.timezone, locale) }}
                </th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="p in poll.participants" :key="p.id" class="border-t border-[var(--color-sand-300)]">
                <td class="p-2 font-medium sticky left-0 bg-[var(--color-sand-100)] dark:bg-[var(--color-ink-900)]">{{ p.name }}</td>
                <td v-for="opt in poll.options" :key="opt.id" class="p-2">
                  <span class="vote-pill !h-7 !min-w-[2.25rem] !text-xs px-2"
                        :class="{
                          'vote-yes': p.votes.find(v => v.optionId === opt.id)?.value === 'YES',
                          'vote-maybe': p.votes.find(v => v.optionId === opt.id)?.value === 'MAYBE',
                          'vote-no': p.votes.find(v => v.optionId === opt.id)?.value === 'NO',
                          'vote-empty': !p.votes.find(v => v.optionId === opt.id)
                        }">
                    {{ p.votes.find(v => v.optionId === opt.id)?.value === 'YES' ? '✓'
                       : p.votes.find(v => v.optionId === opt.id)?.value === 'MAYBE' ? '~'
                       : p.votes.find(v => v.optionId === opt.id)?.value === 'NO' ? '✗' : '·' }}
                  </span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </section>
    </template>
  </section>
</template>
