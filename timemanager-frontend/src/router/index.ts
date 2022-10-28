import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import Clients from '../views/Clients.vue'
import Projects from '../views/Projects.vue'
import Users from '../views/Users.vue'
import Tags from '../views/Tags.vue'
import Tasks from '../views/Tasks.vue'
import ProjectUsers from '../views/ProjectUsers.vue'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    redirect: '/clients',
    name: 'Home'
  },
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
  },
  {
    path: '/projects/:projectId/tasks',
    name: 'Tasks',
    component: Tasks,
    props: true
  },
  {
    path: '/projects/:projectId/users',
    name: 'ProjectUsers',
    component: ProjectUsers,
    props: true
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
