<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useI18n } from 'vue-i18n'
import { useRouter } from 'vue-router'
import { ClipboardDocumentIcon, CheckIcon, ArrowTopRightOnSquareIcon, ExclamationTriangleIcon } from '@heroicons/vue/24/outline'
import { getPollAsAdmin } from '@/api/polls'
import type { Poll } from '@/types'

const props = defineProps<{ adminToken: string }>()
const { t } = useI18n()
const router = useRouter()

const poll = ref<Poll | null>(null)
const error = ref<string | null>(null)

const shareUrl = computed(() => poll.value ? `${window.location.origin}/p/${poll.value.publicId}` : '')
const adminUrl = computed(() => `${window.location.origin}/admin/${props.adminToken}`)
const qrSrc = computed(() => poll.value ? `/api/polls/${poll.value.publicId}/qr` : '')

const copied = ref<'share' | 'admin' | null>(null)

async function copy(value: string, which: 'share' | 'admin') {
  await navigator.clipboard.writeText(value)
  copied.value = which
  setTimeout(() => { if (copied.value === which) copied.value = null }, 1800)
}

function openPoll() {
  if (poll.value) router.push({ name: 'poll', params: { publicId: poll.value.publicId } })
}

onMounted(async () => {
  try {
    poll.value = await getPollAsAdmin(props.adminToken)
  } catch {
    error.value = t('common.error')
  }
})
</script>

<template>
  <section class="container-narrow py-10 sm:py-16">
    <p class="chip chip-sage">{{ poll?.title ?? '…' }}</p>
    <h1 class="font-serif text-4xl sm:text-5xl mt-3">{{ t('created.heading') }}</h1>
    <p class="mt-3 text-[var(--color-ink-100)] dark:text-[var(--color-sand-300)]">
      {{ t('created.sub') }}
    </p>

    <div v-if="error" class="mt-6 text-[var(--color-clay-700)]">{{ error }}</div>

    <div v-else class="mt-10 grid lg:grid-cols-[1fr_auto] gap-8 items-start">
      <div class="space-y-6">
        <div class="surface p-5">
          <label class="field-label">{{ t('created.publicLink') }}</label>
          <div class="flex flex-col sm:flex-row gap-2">
            <input :value="shareUrl" readonly class="input font-mono text-sm" />
            <button class="btn btn-primary" type="button" @click="copy(shareUrl, 'share')">
              <CheckIcon v-if="copied === 'share'" class="w-5 h-5" />
              <ClipboardDocumentIcon v-else class="w-5 h-5" />
              {{ copied === 'share' ? t('created.copied') : t('created.copy') }}
            </button>
          </div>
        </div>

        <div class="surface p-5 border-[var(--color-clay-500)]" style="border-color: var(--color-clay-500)">
          <div class="flex items-start gap-2 mb-2">
            <ExclamationTriangleIcon class="w-5 h-5 text-[var(--color-clay-600)] mt-0.5 shrink-0" />
            <p class="text-sm font-medium">{{ t('created.saveWarning') }}</p>
          </div>
          <label class="field-label">{{ t('created.adminLink') }}</label>
          <div class="flex flex-col sm:flex-row gap-2">
            <input :value="adminUrl" readonly class="input font-mono text-sm" />
            <button class="btn btn-outline" type="button" @click="copy(adminUrl, 'admin')">
              <CheckIcon v-if="copied === 'admin'" class="w-5 h-5" />
              <ClipboardDocumentIcon v-else class="w-5 h-5" />
              {{ copied === 'admin' ? t('created.copied') : t('created.copy') }}
            </button>
          </div>
        </div>

        <button class="btn btn-accent w-full sm:w-auto" @click="openPoll" :disabled="!poll">
          {{ t('created.next') }}
          <ArrowTopRightOnSquareIcon class="w-5 h-5" />
        </button>
      </div>

      <div v-if="poll" class="text-center">
        <label class="field-label">{{ t('created.qrLabel') }}</label>
        <div class="surface inline-block p-3 bg-white">
          <img :src="qrSrc" :alt="poll.title" class="w-48 h-48" />
        </div>
      </div>
    </div>
  </section>
</template>
