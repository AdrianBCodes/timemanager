import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import Clients from '../views/Clients.vue'
import Projects from '../views/Projects.vue'
import Users from '../views/Users.vue'
import Tags from '../views/Tags.vue'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/clients',
    name: 'Clients',
    component: Clients
  },
  {
    path: '/projects',
    name: 'Projects',
    component: Projects
  },
  {
    path: '/users',
    name: 'Users',
    component: Users
  },
  {
    path: '/tags',
    name: 'Tags',
    component: Tags
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
