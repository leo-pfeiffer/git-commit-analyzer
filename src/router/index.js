import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/import/github',
    name: 'ImportGithub',
    component: () => import('../views/ImportGithub.vue')
  },
  {
    path: '/import/text',
    name: 'ImportText',
    component: () => import('../views/ImportText.vue')
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('../views/Dashboard.vue')
  },
]

const router = new VueRouter({
  mode: 'history',
  base: '/',
  routes
})

export default router
