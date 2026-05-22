<script setup lang="ts">
import { onMounted } from 'vue'
import { useI18n } from 'vue-i18n'

const { t } = useI18n()

const AD_SLOT = 'YYYYYYYYYY'
const isConfigured = !AD_SLOT.includes('Y')

onMounted(() => {
  if (!isConfigured) return
  try {
    // @ts-expect-error adsbygoogle is injected by AdSense script
    ;(window.adsbygoogle = window.adsbygoogle || []).push({})
  } catch {
    // AdSense not loaded (dev, adblocker) - silently ignore
  }
})
</script>

<template>
  <div v-if="isConfigured" class="mt-6 pt-4 border-t border-[var(--color-sand-300)] dark:border-[var(--color-ink-700)]">
    <p class="text-[10px] uppercase tracking-widest text-[var(--color-ink-50)] mb-2 select-none">
      {{ t('common.ad') }}
    </p>
    <ins
      class="adsbygoogle block"
      style="min-height: 50px"
      data-ad-client="ca-pub-9651598288655789"
      :data-ad-slot="AD_SLOT"
      data-ad-format="auto"
      data-full-width-responsive="true"
      data-adtest="off"
    />
  </div>
</template>
