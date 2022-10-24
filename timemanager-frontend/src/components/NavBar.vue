<template>
    <Toolbar class="p-toolbar-changes"/>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import Toolbar from 'primevue/toolbar';

export default defineComponent({
    components:{
        Toolbar
    }
})
</script>

<style scoped>
.p-toolbar-changes {
    border-right: none;
    border-left: none;
    border-top: none;
    border-radius: 0%;
}
</style>