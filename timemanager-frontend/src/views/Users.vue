<template>
    <UsersTable />
</template>
  
  <script lang="ts">
  
  import UsersTable from '@/components/tables/UsersTable.vue'
  import { defineComponent} from 'vue';
  export default defineComponent({
  components: {
    UsersTable
  }
  })
  </script>
  <style scoped>
  
  </style>
  