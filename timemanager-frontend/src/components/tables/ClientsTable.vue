<template>
    <div class="table">
        <DataTable :key="datatableKey" :value="clients" showGridlines stripedRows responsiveLayout="scroll"
            :scrollable="true" scrollHeight="flex" :rows="size" @sort="onSort($event)" v-model:filters="filters1"
            filterDisplay="row">
            <template #header>
                <div class="table-header">
                    <div class="table-header-group-left" >
                        Clients
                        <div>
                            <Button label="New" icon="pi pi-plus" class="p-button p-button-success" @click="openNew" style="margin-left: 1rem"/>
                        </div>
                    </div>
                    <div class="table-header-group-right" >
                        <span>
                            <Button label="Clear filters" class="p-button-secondary" @click="clearFilters()"></Button>
                        </span>
                    </div>
                </div>
            </template>
            <Column field="name" header="Name" :sortable="true" filterField="name" :showFilterMenu="false"
                :show-clear-button="false">
                <template #filter="{filterModel}">
                    <InputText type="text" v-model="filterModel.value"
                        @keydown.enter="onFilter('name', filterModel.value)" class="p-column-filter"
                        :placeholder="`Search by name `" v-tooltip.top.focus="'Hit enter key to filter'" />
                </template>
            </Column>
            <Column field="note" header="Note" :sortable="true" fielterField="note" :showFilterMenu="false"
                :show-clear-button="false">
                <template #filter="{filterModel}">
                    <InputText type="text" v-model="filterModel.value"
                        @keydown.enter="onFilter('note', filterModel.value)" class="p-column-filter"
                        :placeholder="`Search by note `" v-tooltip.top.focus="'Hit enter key to filter'" />
                </template>
            </Column>
            <Column :exportable="false" style="max-width:12rem ">
                <template #body="slotProps">
                    <Button icon="pi pi-pencil" class="p-button-rounded p-button-success"
                        @click="openEdit(slotProps.data)" />
                    <Button icon="pi pi-trash" class="p-button-rounded p-button-danger"
                        @click="confirmDeleteClient(slotProps.data.id)" />
                </template>
            </Column>
            <template #footer>
                <Paginator :first="offset" :rows="size" :totalRecords="totalRecords" @page="onPage($event)"
                    :rowsPerPageOptions="[5,10,25,50,100]"
                    currentPageReportTemplate="Showing {first} - {last} of {totalRecords} clients"
                    template="CurrentPageReport FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink RowsPerPageDropdown" />
            </template>
        </DataTable>

        <Dialog v-model:visible="clientDialog" :style="{width: '450px'}" header="Client Details" :modal="true" :closable="false"
            class="p-fluid">
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
                <Button v-if="isEditing" label="Save" icon="pi pi-check " class="p-button" @click="editClient" />
                <Button v-else-if="!isEditing" label="Save" icon="pi pi-check " class="p-button p-button-success"
                    @click="saveClient" />
                    <Button label="Cancel" icon="pi pi-times" class="p-button p-button-danger" @click="hideDialog" />
            </template>
        </Dialog>

        <Dialog v-model:visible="deleteClientDialog" :style="{width: '450px'}" header="Confirm" :modal="true" :closable="false">
            <div class="confirmation-content">
                <i class="pi pi-exclamation-triangle" style="font-size: 2rem; margin-right: 0.5em;" />
                <span v-if="client">Are you sure you want to delete <b>{{client.name}}</b>?</span>
            </div>
            <template #footer>
                <Button label="Yes" icon="pi pi-check" class="p-button p-button-success" @click="deleteClientById" />
                <Button label="No" icon="pi pi-times" class="p-button p-button-danger" @click="deleteClientDialog = false" />
            </template>
        </Dialog>
    </div>
</template>

<script lang="ts">
import { onMounted, ref, watch } from 'vue';
import { defineComponent } from 'vue';
import Client from '@/types/Client';
import ClientService from '../services/ClientService';
import { FilterMatchMode } from 'primevue/api';

export default defineComponent({
    setup() {
        const clientDialog = ref(false);
        const datatableKey = ref(0)
        const offset = ref(0)
        const currentPage = ref(0)
        const size = ref(5)
        const pageParam = ref('page=' + currentPage.value)
        const sizeParam = ref('size=' + size.value)
        const sortParam = ref('')
        const filterNameParam = ref('')
        const filterNoteParam = ref('')
        const params = ref<string>(pageParam.value + '&' + sizeParam.value + '&' + sortParam.value + '&' + filterNameParam.value + '&' + filterNoteParam.value)
        const clientService = new ClientService();
        const { clients, totalRecords, errorGetClients, loadGetClients } = clientService.getClients()
        const isEditing = ref(false);
        const deleteClientDialog = ref(false)
        const submitted = ref(false)
        const renderComponent = ref(true)
        const client = ref<Client>({ id: 0, name: '', note: '' })

        onMounted(() => {
            loadGetClients(params.value);
        })
        watch(errorGetClients, () => {
            console.log(errorGetClients.value)
        })

        const filters1 = ref({
            'name': { value: null, matchMode: FilterMatchMode.CONTAINS },
            'note': { value: null, matchMode: FilterMatchMode.CONTAINS }
        });

        watch(size, (s) => {
            sizeParam.value = 'size=' + s
        })
        watch(currentPage, (cp) => {
            pageParam.value = 'page=' + cp
        })
        watch([sizeParam, pageParam, sortParam, filterNameParam, filterNoteParam], (p) => {
            params.value = p.join('&')
            loadGetClients(params.value)
        })

        
        const openNew = () => {
            clientDialog.value = true;
        };

        const hideDialog = () => {
            clientDialog.value = false;
            client.value = { id: 0, name: '', note: '' };
        };

        const saveClient = () => {
            const { addedClient, loadAddClient } = clientService.addClient();
            loadAddClient(client.value);
            watch(addedClient, () => {
                loadGetClients(params.value)
            })
            client.value = { id: 0, name: '', note: '' };
            clientDialog.value = false;
        }
        const openEdit = (cli: Client) => {
            isEditing.value = true;
            clientDialog.value = true;
            client.value = { ...cli };
        };

        const editClient = () => {
            const { editedClient, loadEditClient } = clientService.editClient();
            loadEditClient(client.value.id, client.value);
            watch(editedClient, () => {
                loadGetClients(params.value)
            })
            client.value = { id: 0, name: '', note: '' };
            clientDialog.value = false;
            isEditing.value = false;
        }

        const confirmDeleteClient = (c: number) => {
            client.value.id = c;
            deleteClientDialog.value = true;
        };

        const deleteClientById = () => {
            const { resp, loadDeleteClient } = clientService.deleteClient();
            loadDeleteClient(client.value.id)
            watch(resp, () => {
                loadGetClients(params.value);
            })
            deleteClientDialog.value = false;
            client.value = { id: 0, name: '', note: '' };
        };

        const onPage = (event: any) => {
            currentPage.value = event.page
            size.value = event.rows
            offset.value = event.rows * event.page
        }
        const onSort = (event: any) => {
            if (event.sortField === null) {
                sortParam.value = 'sort='
                return;
            }
            if (event.sortOrder === 1) {
                sortParam.value = 'sort=' + event.sortField + ',asc'
            } else {
                sortParam.value = 'sort=' + event.sortField + ',desc'
            }
            currentPage.value = 0
            offset.value = 0
        }

        const onFilter = (prop: 'name' | 'note', input: string) => {
            if (input === null) {
                return;
            }
            currentPage.value = 0
            if(prop === 'name'){
                filterNameParam.value = prop + '=' + input
            }
            if(prop === 'note'){
                filterNoteParam.value = prop + '=' + input
            }
        }

        const clearFilters = () => {
            currentPage.value = 0
            offset.value = 0
            sortParam.value = ''
            filterNameParam.value = ''
            filterNoteParam.value = ''
            datatableKey.value++
        }

        return {
            clients, errorGetClients, currentPage, size, totalRecords, submitted, client, isEditing, clientDialog,
            openNew, openEdit, hideDialog, saveClient, renderComponent, deleteClientDialog,
            confirmDeleteClient, deleteClientById, editClient, onPage, onSort, offset, filters1, onFilter, clearFilters, datatableKey
        }
    },
})
</script>

<style scoped>
.table {
    margin-right: 0.7vw;
}
.p-button {
    margin-right: 0.25em;
}

.table-header {
    flex-wrap: wrap;
    justify-content: space-between;
}
.table-header, .table-header-group-right, .table-header-group-left {
    display: flex;
    align-items: center;
}
</style>
