import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { createHead } from '@unhead/vue/client'
import App from './App.vue'
import { router } from './router'
import { i18n } from './i18n'
import './styles/main.css'

const app = createApp(App)
app.use(createPinia())
app.use(router)
app.use(i18n)
app.use(createHead())

// Wait for the initial route's lazy chunk to resolve before mounting,
// otherwise a direct deep-link hit (e.g. /new) renders an empty body
// until the chunk arrives.
router.isReady().then(() => app.mount('#app'))
