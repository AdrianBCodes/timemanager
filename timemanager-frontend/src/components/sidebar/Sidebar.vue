<template>
    <div class="p-component sidebar" :style="{ width: sidebarWidth}">
      <span class="collapse-icon"
      :class="{'rotate-180' : collapsed}"
      @click = "toggleSidebar">
        <i class="pi pi-angle-double-left"></i>
      </span>
      <SidebarLink to="/clients" icon="pi pi-users">Clients</SidebarLink>
      <SidebarLink to="/projects" icon="pi pi-briefcase">Projects</SidebarLink>
      <SidebarLink to="/users" icon="pi pi-user">Users</SidebarLink>
      <SidebarLink to="/tags" icon="pi pi-tag">Tags</SidebarLink>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import SidebarLink from './SidebarLink.vue'
import { collapsed, toggleSidebar, sidebarWidth } from './state'

export default defineComponent({
    components: {SidebarLink},
    setup() {
        return { collapsed, toggleSidebar, sidebarWidth }
    },
})
</script>

<style>

</style>

<style scoped>
.sidebar {
  border-right: 1px solid;
  border-color: var(--surface-border);
  color: white;
  background-color: var(--sidebar-bg-color);
  float: left;
  position: fixed;
  z-index: 1;
  top: 0;
  left: 0;
  bottom: 0;
  transition: 0.3s ease;
  display: flex;
  flex-direction: column;
}
.sidebar h1 {
  height: 2.5em;
}
.collapse-icon {
  position: absolute;
  bottom: 0;
  padding: 0.75em;
  color: rgba(255, 255, 255, 0.7);
  transition: 0.2s linear;
}
.rotate-180 {
  transform: rotate(180deg);
  transition: 0.2s linear;
}
</style>
