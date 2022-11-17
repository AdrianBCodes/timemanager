import { createRouter, createWebHistory, RouteRecordRaw, useRouter } from 'vue-router'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Profile from '../views/Profile.vue'
import PageNotFound from '../views/PageNotFound.vue'
import Tracker from '../views/Tracker.vue'
import Reports from '../views/Reports.vue'
import Clients from '../views/Clients.vue'
import Projects from '../views/Projects.vue'
import Users from '../views/Users.vue'
import Tags from '../views/Tags.vue'
import Tasks from '../views/Tasks.vue'
import ProjectUsers from '../views/ProjectUsers.vue'
import PermissionDenied from '../views/PermissionDenied.vue'
import store from '@/store/index'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    redirect: '/tracker',
    name: 'Home'
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
  {
    path: '/profile',
    name: 'Profile',
    component: Profile,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/tracker',
    name: 'Tracker',
    component: Tracker,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/reports',
    name: 'Reports',
    component: Reports,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/clients',
    name: 'Clients',
    component: Clients,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/projects',
    name: 'Projects',
    component: Projects,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/users',
    name: 'Users',
    component: Users,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/tags',
    name: 'Tags',
    component: Tags,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/projects/:projectId/tasks',
    name: 'Tasks',
    component: Tasks,
    props: true,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/projects/:projectId/users',
    name: 'ProjectUsers',
    component: ProjectUsers,
    props: true,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/denied',
    name: 'Denied',
    component: PermissionDenied
  },
  { path: '/:pathMatch(.*)*',
    component: PageNotFound 
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!store.getters.getLoggedIn) {
      if(to.name === 'Register'){
        next({ name: 'Register' })
      } else{
        next({ name: 'Login'})
      }
    } else {
      next()
    }
  } else if(store.getters.getLoggedIn && (to.name === 'Login' || to.name === 'Register')) {
    next({name: 'Tracker'})
  } else {
    next()
  }
})


export default router
