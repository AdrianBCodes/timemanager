<template>
  <NavBar class="fixed-header" v-if="showBars"/>
  <Sidebar style="padding-top: 30px;" v-if="showBars"/>
  <div class="content" :style="{'margin-left': sidebarWidth}">
  <router-view />
  </div>
  <Toast position="bottom-right"/>
</template>

<script lang="ts">
import { computed, defineComponent} from 'vue';
import { useRoute } from 'vue-router';

import NavBar from './components/NavBar.vue'
import Sidebar from './components/sidebar/Sidebar.vue'
import {sidebarWidth} from './components/sidebar/state'
export default defineComponent({
  components: { 
    NavBar,
    Sidebar 
  },
  setup(){
    const route = useRoute()
    const showBars = computed(() => route.name !== 'Login') 
    return {sidebarWidth, showBars}
  }
})
</script>


<style>
.content {
  padding-left: 1%;
  padding-top: 1%;
}
.fixed-header {
    position: relative;
    width: 100%;
    z-index: 1000;
    left:0;
    right:0;
    top:0;
}
body {
      margin: 0;
      height: 100%;
      overflow-x: hidden;
      overflow-y: auto;
      background-color: var(--surface-ground);
      font-family: var(--font-family);
      font-weight: 400;
      color: var(--text-color);
   }
:root {
    --sidebar-bg-color: var(--surface-e);
    --sidebar-item-hover: var(--surface-hover);
    --sidebar-item-active: var(--surface-b);
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
}
</style>
