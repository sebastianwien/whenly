<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount, computed } from 'vue'
import { useI18n } from 'vue-i18n'
import { useRouter } from 'vue-router'
import { getPoll, getSuggest, castVote, removeVote, addComment } from '@/api/polls'
import { useParticipantTokens } from '@/stores/participantToken'
import { ApiError } from '@/api/client'
import type { Poll, SuggestResult, VoteValue } from '@/types'
import VoteGrid from '@/components/VoteGrid.vue'
import { formatOptionLabel, formatRelative } from '@/lib/format'
import {
  SparklesIcon,
  ClipboardDocumentIcon,
  CheckIcon,
  ArrowDownTrayIcon,
  TrashIcon,
  ChatBubbleLeftEllipsisIcon
} from '@heroicons/vue/24/outline'

const props = defineProps<{ publicId: string }>()
const { t, locale } = useI18n()
const router = useRouter()
const tokens = useParticipantTokens()

const poll = ref<Poll | null>(null)
const suggest = ref<SuggestResult | null>(null)
const loading = ref(true)
const error = ref<string | null>(null)

const myName = ref('')
const myVotes = ref<Map<string, VoteValue>>(new Map())
const editing = ref(false)
const saving = ref(false)
const saveError = ref<string | null>(null)

const commentAuthor = ref('')
const commentBody = ref('')
const commentOption = ref<string | null>(null)
const commentPosting = ref(false)

const copied = ref(false)
let pollTimer: number | null = null

const myToken = computed(() => tokens.tokenFor(props.publicId))

const finalizedOption = computed(() =>
  poll.value && poll.value.finalizedOptionId
    ? poll.value.options.find(o => o.id === poll.value!.finalizedOptionId) ?? null
    : null
)

const suggestedOption = computed(() =>
  suggest.value?.bestOptionId
    ? poll.value?.options.find(o => o.id === suggest.value!.bestOptionId) ?? null
    : null
)

async function load(force = false) {
  try {
    poll.value = await getPoll(props.publicId, myToken.value)
    if (force || !suggest.value) {
      try { suggest.value = await getSuggest(props.publicId) } catch { /* tolerate */ }
    }
    seedMyVotes()
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

function seedMyVotes() {
  if (!poll.value || editing.value) return
  const me = poll.value.viewerParticipantId
    ? poll.value.participants.find(p => p.id === poll.value!.viewerParticipantId)
    : null
  if (!me) { myVotes.value = new Map(); myName.value = ''; return }
  const next = new Map<string, VoteValue>()
  for (const v of me.votes) next.set(v.optionId, v.value)
  myVotes.value = next
  if (!myName.value) myName.value = me.name
}

async function save() {
  if (!poll.value) return
  if (myVotes.value.size === 0) return
  saving.value = true
  saveError.value = null
  try {
    const result = await castVote(
      props.publicId,
      {
        name: myName.value,
        votes: Array.from(myVotes.value.entries()).map(([optionId, value]) => ({ optionId, value }))
      },
      myToken.value
    )
    tokens.remember(props.publicId, result.participantToken)
    editing.value = false
    await load(true)
  } catch (e) {
    if (e instanceof ApiError) saveError.value = e.message
    else saveError.value = t('common.error')
  } finally {
    saving.value = false
  }
}

async function withdraw() {
  if (!myToken.value) return
  if (!confirm(t('poll.confirmRemove'))) return
  try {
    await removeVote(props.publicId, myToken.value)
    tokens.forget(props.publicId)
    myVotes.value = new Map()
    myName.value = ''
    await load(true)
  } catch { /* ignore */ }
}

function onVoteChange(optionId: string, value: VoteValue) {
  if (poll.value?.closedAt) return
  editing.value = true
  myVotes.value.set(optionId, value)
}

async function postComment() {
  if (!commentBody.value.trim()) return
  commentPosting.value = true
  try {
    await addComment(props.publicId, {
      authorName: (commentAuthor.value || myName.value || 'Anonymous').trim(),
      body: commentBody.value.trim(),
      optionId: commentOption.value ?? null,
      participantToken: myToken.value ?? null
    })
    commentBody.value = ''
    commentOption.value = null
    await load(true)
  } catch { /* ignore */ }
  finally { commentPosting.value = false }
}

async function copyShare() {
  await navigator.clipboard.writeText(window.location.origin + '/p/' + props.publicId)
  copied.value = true
  setTimeout(() => copied.value = false, 1500)
}

const retentionRelative = computed(() =>
  poll.value ? formatRelative(poll.value.retentionUntil, locale.value) : ''
)

onMounted(async () => {
  await load(true)
  pollTimer = window.setInterval(() => load(false), 10_000)
})
onBeforeUnmount(() => { if (pollTimer) clearInterval(pollTimer) })
</script>

<template>
  <section class="container-narrow py-6 sm:py-10">
    <p v-if="loading" class="text-center py-12">{{ t('common.loading') }}</p>
    <p v-else-if="error" class="text-center py-12 text-[var(--color-clay-700)]">{{ error }}</p>

    <template v-else-if="poll">
      <!-- Title -->
      <header class="mb-6">
        <div class="flex flex-wrap items-center gap-2 mb-2">
          <span v-if="poll.closedAt" class="chip chip-accent">{{ t('poll.closed') }}</span>
          <span v-if="poll.location" class="chip">{{ poll.location }}</span>
          <span class="chip">{{ poll.participantCount }} {{ t('poll.results.participants') }}</span>
        </div>
        <h1 class="font-serif text-3xl sm:text-5xl leading-tight">{{ poll.title }}</h1>
        <p v-if="poll.description" class="mt-2 text-[var(--color-ink-100)] dark:text-[var(--color-sand-300)] whitespace-pre-line">
          {{ poll.description }}
        </p>
      </header>

      <!-- Finalized banner -->
      <div v-if="finalizedOption" class="surface bg-[var(--color-sage-400)] dark:!bg-[var(--color-sage-600)] p-5 mb-6">
        <p class="chip chip-sage uppercase mb-2 inline-flex">{{ t('poll.finalized') }}</p>
        <p class="font-serif text-2xl">{{ formatOptionLabel(finalizedOption, poll.type, poll.timezone, locale) }}</p>
        <div class="mt-3 flex flex-wrap gap-2">
          <a :href="`/api/polls/${poll.publicId}/ics`" class="btn btn-outline" target="_blank" rel="noreferrer">
            <ArrowDownTrayIcon class="w-5 h-5" /> .ics
          </a>
        </div>
      </div>

      <!-- Smart suggest -->
      <div v-else-if="suggest && suggestedOption && suggest.participantCount > 0"
           class="surface p-5 mb-6 border-[var(--color-clay-500)]" style="border-color: var(--color-clay-500)">
        <p class="chip chip-accent inline-flex items-center gap-1.5 mb-2">
          <SparklesIcon class="w-4 h-4" /> {{ t('poll.smartSuggest.heading') }}
        </p>
        <p class="font-serif text-2xl">{{ formatOptionLabel(suggestedOption, poll.type, poll.timezone, locale) }}</p>
        <p class="text-sm mt-1 text-[var(--color-ink-100)] dark:text-[var(--color-sand-300)]">
          {{ suggest.yesCount }} yes · {{ suggest.maybeCount }} maybe · {{ suggest.noCount }} no
        </p>
      </div>

      <!-- Vote section -->
      <section v-if="!poll.closedAt" class="mb-8">
        <h2 class="font-serif text-2xl mb-3">{{ t('poll.voteHeading') }}</h2>
        <div v-if="poll.settings.requireParticipantName || editing || !myToken" class="mb-3">
          <label class="field-label" for="my-name">{{ t('poll.yourName') }}</label>
          <input id="my-name" v-model="myName" class="input max-w-md" :placeholder="t('poll.namePlaceholder')"
                 :required="poll.settings.requireParticipantName" maxlength="120" />
        </div>
        <VoteGrid :poll="poll" :my-votes="myVotes" :editable="!poll.closedAt"
                  @change="onVoteChange" />
        <div class="mt-4 flex flex-wrap gap-3 items-center">
          <button class="btn btn-accent" :disabled="saving || myVotes.size === 0" @click="save">
            {{ saving ? t('common.loading') : myToken ? t('poll.updateVote') : t('poll.saveVote') }}
          </button>
          <button v-if="myToken" class="btn btn-ghost text-sm" @click="withdraw">
            <TrashIcon class="w-4 h-4" /> {{ t('poll.removeVote') }}
          </button>
          <p v-if="saveError" class="text-sm text-[var(--color-clay-700)] font-medium">{{ saveError }}</p>
        </div>
      </section>

      <!-- Results -->
      <section v-if="!poll.resultsHidden" class="mb-8">
        <h2 class="font-serif text-2xl mb-3">{{ t('poll.results.heading') }}</h2>
        <div v-if="poll.participants.length === 0" class="text-[var(--color-ink-100)] dark:text-[var(--color-sand-300)]">
          {{ t('poll.results.noVotesYet') }}
        </div>
        <div v-else class="overflow-x-auto -mx-1">
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
      <div v-else class="surface-soft p-4 mb-8 text-center">{{ t('poll.results.hidden') }}</div>

      <!-- Share -->
      <section class="mb-8">
        <h2 class="font-serif text-2xl mb-3">{{ t('poll.share.heading') }}</h2>
        <div class="flex flex-col sm:flex-row gap-3 items-start">
          <button class="btn btn-outline" @click="copyShare">
            <CheckIcon v-if="copied" class="w-5 h-5" />
            <ClipboardDocumentIcon v-else class="w-5 h-5" />
            {{ copied ? t('poll.share.copied') : t('poll.share.copy') }}
          </button>
          <img :src="`/api/polls/${poll.publicId}/qr`" :alt="poll.title" class="w-28 h-28 border-2 border-[var(--color-ink-900)] bg-white p-1.5" />
        </div>
      </section>

      <!-- Comments -->
      <section class="mb-12">
        <h2 class="font-serif text-2xl mb-3 flex items-center gap-2">
          <ChatBubbleLeftEllipsisIcon class="w-6 h-6" />
          {{ t('poll.comments.heading') }}
        </h2>
        <div v-if="poll.comments.length === 0" class="text-[var(--color-ink-100)] dark:text-[var(--color-sand-300)] mb-4">
          {{ t('poll.comments.empty') }}
        </div>
        <ul v-else class="space-y-3 mb-6">
          <li v-for="c in poll.comments" :key="c.id" class="surface-soft p-3">
            <p class="text-sm">
              <strong>{{ c.authorName }}</strong>
              <span v-if="c.optionId" class="chip ml-2 text-[10px]">
                {{ formatOptionLabel(poll.options.find(o => o.id === c.optionId)!, poll.type, poll.timezone, locale) }}
              </span>
              <span class="text-xs text-[var(--color-ink-50)] ml-2">{{ formatRelative(c.createdAt, locale) }}</span>
            </p>
            <p class="text-sm whitespace-pre-line mt-0.5">{{ c.body }}</p>
          </li>
        </ul>
        <div class="surface p-4 space-y-3">
          <h3 class="font-medium">{{ t('poll.comments.addHeading') }}</h3>
          <div>
            <label class="field-label" for="comment-author">{{ t('poll.comments.authorPlaceholder') }}</label>
            <input id="comment-author" v-model="commentAuthor" class="input"
                   :placeholder="t('poll.comments.authorPlaceholder')" maxlength="120" />
          </div>
          <div>
            <label class="field-label" for="comment-body">{{ t('poll.comments.heading') }}</label>
            <textarea id="comment-body" v-model="commentBody" class="textarea"
                      :placeholder="t('poll.comments.bodyPlaceholder')" maxlength="2000" />
          </div>
          <div>
            <label class="field-label" for="comment-option">{{ t('poll.comments.attach') }}</label>
            <select id="comment-option" v-model="commentOption" class="select">
              <option :value="null">{{ t('poll.comments.attachNone') }}</option>
              <option v-for="opt in poll.options" :key="opt.id" :value="opt.id">
                {{ formatOptionLabel(opt, poll.type, poll.timezone, locale) }}
              </option>
            </select>
          </div>
          <button class="btn btn-primary" :disabled="!commentBody.trim() || commentPosting" @click="postComment">
            {{ commentPosting ? t('common.loading') : t('poll.comments.post') }}
          </button>
        </div>
      </section>

      <p class="text-xs text-[var(--color-ink-50)] text-center">
        {{ t('create.settings.retention.title') }} {{ retentionRelative }}
      </p>
    </template>
  </section>
</template>
