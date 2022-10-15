<template>
    <router-link :to="to" class="link" :class="{ active: isActive }">
      <i class="icon" :class="icon" />
      <transition name="fade">
        <span v-if="!collapsed">
          <slot />
        </span>
      </transition>
    </router-link>
  </template>

<script lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { collapsed } from './state'
import { defineComponent } from 'vue'

export default defineComponent({
    props: {
    to: { type: String, required: true },
    icon: { type: String, required: true }
    },
    setup(props) {
    const route = useRoute()
    const isActive = computed(() => route.path === props.to)
    return { isActive, collapsed }
    },
})
</script>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.1s;
}
.fade-enter,
.fade-leave-to {
  opacity: 0;
}
.link {
  font-size: 1.3vw;
  display: flex;
  align-items: center;
  cursor: pointer;
  position: relative;
  user-select: none;
  padding: 0.4em;
  height: 10%;
  color: white;
  text-decoration: none;
}
.link:hover {
  background-color: var(--sidebar-item-hover);
}
.link.active {
  background-color: var(--sidebar-item-active);
}
.link .icon {
  font-size: 1vw;
  flex-shrink: 0;
  width: 25px;
  margin-right: 0.7vw;
}
</style>