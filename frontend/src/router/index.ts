import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'landing',
    component: () => import('@/views/LandingView.vue'),
    meta: { title: 'whenly' }
  },
  {
    path: '/new',
    name: 'create',
    component: () => import('@/views/CreatePollView.vue'),
    meta: { title: 'New poll · whenly' }
  },
  {
    path: '/p/:publicId',
    name: 'poll',
    component: () => import('@/views/PollView.vue'),
    props: true,
    meta: { title: 'Poll · whenly' }
  },
  {
    path: '/admin/:adminToken',
    name: 'admin',
    component: () => import('@/views/AdminPollView.vue'),
    props: true,
    meta: { title: 'Admin · whenly' }
  },
  {
    path: '/created/:adminToken',
    name: 'created',
    component: () => import('@/views/PollCreatedView.vue'),
    props: true,
    meta: { title: 'Share your poll · whenly' }
  },
  {
    path: '/about',
    name: 'about',
    component: () => import('@/views/AboutView.vue'),
    meta: { title: 'About · whenly' }
  },
  {
    path: '/impressum',
    name: 'impressum',
    component: () => import('@/views/ImpressumView.vue'),
    meta: { title: 'Impressum · whenly' }
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'not-found',
    component: () => import('@/views/NotFoundView.vue')
  }
]

export const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() {
    return { top: 0 }
  }
})

