<template>
    <TasksTable :project-id="projId" />
</template>
  
  <script lang="ts">
  
  import TasksTable from '@/components/tables/TasksTable.vue'
  import { defineComponent, ref} from 'vue';
  export default defineComponent({
    props: {
        projectId: String
    },
  components: {
    TasksTable
  },
  setup(props){
    const projId = ref(Number(props.projectId))
    return {projId}
  }
  })
  </script>
  <style scoped>
  
  </style>