<template>
    <div>
      <Toolbar class="mb-4">
                <template #start>
                    <Button label="New" icon="pi pi-plus" class="p-button p-button-success"/>
                </template>
      </Toolbar>

        <DataTable :value="clients" showGridlines stripedRows responsiveLayout="scroll" :scrollable="true" scrollHeight="flex">
          <template #header>
                <div class="table-header-footer">
                    Clients
                </div>
            </template>
          <Column v-for="col of columns" :field="col.field" :header="col.header" :key="col.field"></Column>
          <Column :exportable="false" style="min-width:8rem">
              <template #body="slotProps">
                        <Button icon="pi pi-pencil" class="p-button-rounded p-button-success" />
                        <Button icon="pi pi-trash" class="p-button-rounded p-button-warning" />
               </template>
          </Column>

          <template #footer>
            <div class="table-header-footer">
                In total there are {{clients ? clients.length : 0 }} clients.
            </div>
          </template>
        </DataTable>
    </div>
</template>

<script lang="ts">
import { ref } from 'vue';
import getClients from '@/composables/getClients';
import { defineComponent} from 'vue';
export default defineComponent({
  setup() {
    const {clients, error, load} = getClients()
    load()

    const columns = ref([
            {field: 'id', header: 'Id'},
            {field: 'name', header: 'Name'},
            {field: 'note', header: 'Note'},
            {field: 'projectNames', header: 'Projects'}
        ]);
    return {clients, error, columns}
  },
})
</script>
<style scoped>
.p-button{
  margin-right: 0.25em;
}
.table-header-footer {
    display: flex;
    align-items: center;
    justify-content: space-between;
}
</style>
