<template>
    <ProjectUsersTable :project-id="projId" />
</template>
  
  <script lang="ts">
  
  import ProjectUsersTable from '@/components/tables/ProjectUsersTable.vue'
  import { defineComponent, ref} from 'vue';
  export default defineComponent({
    props: {
        projectId: String
    },
  components: {
    ProjectUsersTable
  },
  setup(props){
    const projId = ref(Number(props.projectId))
    return {projId}
  }
  })
  </script>
  <style scoped>
  
  </style>