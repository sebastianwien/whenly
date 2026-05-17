<script setup lang="ts">
import AppHeader from '@/components/AppHeader.vue'
import AppFooter from '@/components/AppFooter.vue'
</script>

<template>
  <div class="min-h-screen flex flex-col">
    <AppHeader />
    <main class="flex-1 pb-16 pt-6">
      <!-- Suspense lets an async route component resolve before Vue swaps it
           in, so a deep-link hit on /new etc. doesn't flash an empty body. -->
      <RouterView v-slot="{ Component, route }">
        <Suspense :timeout="0">
          <Transition name="fade" mode="out-in">
            <component :is="Component" :key="route.fullPath" />
          </Transition>
        </Suspense>
      </RouterView>
    </main>
    <AppFooter />
  </div>
</template>

<style>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 140ms ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
