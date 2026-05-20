<script setup lang="ts">
import { ref, computed } from 'vue'
import { useI18n } from 'vue-i18n'

defineProps<{
  modelValue: string | null
}>()
const emit = defineEmits<{
  'update:modelValue': [value: string]
}>()

const { t } = useI18n()

const SLOTS = ['08:00', '09:00', '10:00', '12:00', '14:00', '17:00', '18:00', '19:00', '20:00']

const customInput = ref('')
const showCustom = ref(false)

const customValid = computed(() => /^\d{2}:\d{2}$/.test(customInput.value))

function select(time: string) {
  emit('update:modelValue', time)
}

function commitCustom() {
  if (customValid.value) {
    emit('update:modelValue', customInput.value)
    showCustom.value = false
    customInput.value = ''
  }
}
</script>

<template>
  <div class="flex flex-wrap items-center gap-2">
    <button
      v-for="slot in SLOTS"
      :key="slot"
      type="button"
      class="px-3 py-1 rounded-full text-sm border transition-colors"
      :class="modelValue === slot
        ? 'bg-[var(--color-clay-500)] text-white border-[var(--color-clay-500)]'
        : 'border-[var(--color-sand-400)] text-[var(--color-ink-200)] hover:border-[var(--color-clay-400)]'"
      @click="select(slot)"
    >
      {{ slot }}
    </button>

    <template v-if="!showCustom">
      <button
        type="button"
        class="px-3 py-1 rounded-full text-sm border border-dashed border-[var(--color-sand-400)] text-[var(--color-ink-100)] hover:border-[var(--color-clay-400)] transition-colors"
        @click="showCustom = true"
      >
        {{ t('create.time.customTime') }}
      </button>
    </template>
    <template v-else>
      <input
        v-model="customInput"
        type="time"
        class="input py-1 px-2 text-sm w-28"
        autofocus
        @keydown.enter="commitCustom"
        @keydown.escape="showCustom = false"
        @blur="commitCustom"
      />
    </template>
  </div>
</template>
