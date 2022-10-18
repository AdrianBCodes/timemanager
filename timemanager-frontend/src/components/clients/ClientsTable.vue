<template>
    <div>
      <Toolbar class="mb-4">
                <template #start>
                    <Button label="New" icon="pi pi-plus" class="p-button p-button-success" @click="openNew"/>
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
                        <Button icon="pi pi-pencil" class="p-button-rounded p-button-success" @click="openEdit(slotProps.data)"/>
                        <Button icon="pi pi-trash" class="p-button-rounded p-button-warning" @click="confirmDeleteClient(slotProps.data.id)" />
               </template>
          </Column>

          <template #footer>
            <div class="table-header-footer">
                In total there are {{clients ? clients['length'] : 0 }} clients.
            </div>
          </template>
        </DataTable>

        <Dialog v-model:visible="clientDialog" :style="{width: '450px'}" header="Client Details" :modal="true" class="p-fluid">
            <div class="field">
                <label for="name">Name</label>
                <InputText id="name" v-model.trim="client.name" required="true" autofocus />
                <small class="p-error" v-if="submitted && !client.name">Name is required.</small>
            </div>
            <div class="field">
                <label for="note">Note</label>
                <Textarea id="note" v-model="client.note" required="true" rows="3" cols="20" />
            </div>
            <template #footer>
                <Button label="Cancel" icon="pi pi-times" class="p-button-text" @click="hideDialog"/>
                <Button v-if="isEditing" label="Save" icon="pi pi-check" class="p-button-text" @click="editClient" />
                <Button v-else-if="!isEditing" label="Save" icon="pi pi-check" class="p-button-text" @click="saveClient" />

            </template>
        </Dialog>
        <Dialog v-model:visible="deleteClientDialog" :style="{width: '450px'}" header="Confirm" :modal="true">
            <div class="confirmation-content">
                <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem" />
                <span v-if="client">Are you sure you want to delete <b>{{client.name}}</b>?</span>
            </div>
            <template #footer>
                <Button label="No" icon="pi pi-times" class="p-button-text" @click="deleteClientDialog = false"/>
                <Button label="Yes" icon="pi pi-check" class="p-button-text" @click="deleteClientById" />
            </template>
        </Dialog>
    </div>
</template>

<script lang="ts">
import {ref, watch } from 'vue';
import { defineComponent} from 'vue';
import Client from '@/types/Client';
import { useToast } from "primevue/usetoast";
import ClientService from '../services/ClientService';

export default defineComponent({
  setup() {
    const clientService = ref(new ClientService());
    const {clients, error, load} = clientService.value.getClients()
    load();
    watch(error, () => {
        console.log('error from watch in clients')
    })
    const isEditing = ref(false);
    const toast = useToast();
    const deleteClientDialog = ref(false)
    const submitted = ref(false)
    const renderComponent = ref(true)
    const client = ref<Client>({
        id: 0,
        name: '',
        note: ''
    })

    const columns = ref([
            {field: 'id', header: 'Id'},
            {field: 'name', header: 'Name'},
            {field: 'note', header: 'Note'}
        ]);

    const clientDialog = ref(false);
    const openNew = () => {
        clientDialog.value = true;
    };

    const hideDialog = () => {
        clientDialog.value = false;
    };

    const saveClient = () => {
        const { addedClient, errorAdd, loadAddClient } = clientService.value.addClient();
        loadAddClient(client.value);
        watch(errorAdd, (e) => {
            toast.add({severity:'warn', summary: 'Warn Message', detail: e, life: 3000});
        })
        watch(addedClient, (c) => {
            load()
        })
        client.value = {
        id: 0,
        name: '',
        note: ''
        };
        clientDialog.value = false;
    }
    const openEdit = (cli: Client) => {
        isEditing.value = true;
        clientDialog.value = true;
        client.value = {...cli};
    };

    const editClient = () => {
        const { editedClient, errorEdit, loadEditClient } = clientService.value.editClient();
        loadEditClient(client.value.id, client.value);
        watch(errorEdit, (e) => {
            toast.add({severity:'warn', summary: 'Warn Message', detail: e, life: 3000});
        })
        watch(editedClient, (c) => {
            load()
        })
        client.value = {
        id: 0,
        name: '',
        note: ''
        };
        clientDialog.value = false;
        isEditing.value = false;
    }

    const confirmDeleteClient = (c: number) => {
            client.value.id = c;
            deleteClientDialog.value = true;
        };

    const deleteClientById = () => {
        const { resp, errorDelete, loadDeleteClient } = clientService.value.deleteClient();
        console.log(client.value.id)
        loadDeleteClient(client.value.id)
        watch(errorDelete, (e) => {
            toast.add({severity:'warn', summary: 'Warn Message', detail: e, life: 3000});
        })
        watch(resp, () => {
            load();
        })
        deleteClientDialog.value = false;
        client.value = {
        id: 0,
        name: '',
        note: ''
        };
        toast.add({severity:'success', summary: 'Successful', detail: 'Client Deleted', life: 3000});
    };
    return {clients, error, columns, submitted, client, isEditing, clientDialog, openNew, openEdit, hideDialog, saveClient, renderComponent, deleteClientDialog, confirmDeleteClient, deleteClientById, editClient}
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
