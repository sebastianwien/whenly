<script setup lang="ts">
import { useI18n } from 'vue-i18n'
const { t } = useI18n()
</script>

<template>
  <section class="container-narrow py-20 text-center">
    <p class="font-serif text-7xl">404</p>
    <p class="mt-4 text-lg">{{ t('common.notFound') }}</p>
    <RouterLink to="/" class="btn btn-outline mt-8 inline-flex">←  whenly</RouterLink>
  </section>
</template>
