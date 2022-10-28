<template>
    <div class="table">
        <Toolbar class="mb-4">
            <template #start>
                <Button label="New" icon="pi pi-plus" class="p-button p-button-success" @click="openNew" />
            </template>
        </Toolbar>
        
        <DataTable :key="datatableKey" :value="projects" showGridlines stripedRows responsiveLayout="scroll"
            :scrollable="true" scrollHeight="flex" :rows="size" @sort="onSort($event)" v-model:filters="filters1"
            filterDisplay="row">
            <template #header>
                <div class="table-header-footer">
                    Projects
                    <span class="p-input-icon-left ">
                        <Button label="Clear filters" class="p-button-secondary" @click="clearFilters()"></Button>
                    </span>
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
            <Column field="client.name" header="Client" :sortable="true" fielterField="client.name" :showFilterMenu="false"
                :show-clear-button="false">
                <template #filter="{filterModel}">
                    <InputText type="text" v-model="filterModel.value"
                        @keydown.enter="onFilter('clientName', filterModel.value)" class="p-column-filter"
                        :placeholder="`Search by client `" v-tooltip.top.focus="'Hit enter key to filter'" />
                </template>
            </Column>
            <Column field="owner.name" header="Owner" :sortable="true" fielterField="owner.name" :showFilterMenu="false"
                :show-clear-button="false">
                <template #filter="{filterModel}">
                    <InputText type="text" v-model="filterModel.value"
                        @keydown.enter="onFilter('ownerFullName', filterModel.value)" class="p-column-filter"
                        :placeholder="`Search by owner `" v-tooltip.top.focus="'Hit enter key to filter'" />
                </template>
            </Column>
            <Column :exportable="false" style="max-width:18rem ">
                <template #body="slotProps">
                    <Button label="Tasks" class="p-button"
                        @click="goToTasks(slotProps.data.id)" ></Button>
                        <Button label="Users" class="p-button"
                        @click="goToProjectUsers(slotProps.data.id)" ></Button>
                    <Button icon="pi pi-pencil" class="p-button-rounded p-button-success"
                        @click="openEdit(slotProps.data)" />
                    <Button icon="pi pi-trash" class="p-button-rounded p-button-danger"
                        @click="confirmDeleteProject(slotProps.data.id)" />
                </template>
            </Column>
            <template #footer>
                <Paginator :first="offset" :rows="size" :totalRecords="totalRecords" @page="onPage($event)"
                    :rowsPerPageOptions="[5,10,25,50,100]"
                    currentPageReportTemplate="Showing {first} - {last} of {totalRecords} projects"
                    template="CurrentPageReport FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink RowsPerPageDropdown" />
            </template>
        </DataTable>

        <Dialog v-model:visible="projectDialog" :style="{width: '450px'}" header="Project Details" :modal="true" :closable="false"
            class="p-fluid">
            <div class="field">
                <label for="name">Name</label>
                <InputText id="name" v-model.trim="project.name" required="true" autofocus />
                <small class="p-error" v-if="submitted && !project.name">Name is required.</small>
            </div>
            <div class="field">
                <label for="name">Client</label>
                <Dropdown v-model="selectedClient" :options="clients" optionLabel="name" :filter="true" placeholder="Select a Client" :showClear="true">
                    <template #value="slotProps">
                        <div v-if="slotProps.value">
                            <div>{{slotProps.value.name}}</div>
                        </div>
                        <span v-else>
                            {{slotProps.placeholder}}
                        </span>
                    </template>
                    <template #option="slotProps">
                        <div>
                            <div>{{slotProps.option.name}}</div>
                        </div>
                    </template>
                </Dropdown>
            </div>
            <div class="field">
                <label for="owner.name">Owner</label>
                <Dropdown v-model="selectedOwner" :options="users" optionLabel="owner.name" :filter="true" placeholder="Select a Owner" :showClear="true">
                    <template #value="slotProps">
                        <div v-if="slotProps.value">
                            <div>{{slotProps.value.name}}</div>
                        </div>
                        <span v-else>
                            {{slotProps.placeholder}}
                        </span>
                    </template>
                    <template #option="slotProps">
                        <div>
                            <div>{{slotProps.option.name}}</div>
                        </div>
                    </template>
                </Dropdown>
            </div>
            <template #footer>
                <Button v-if="isEditing" label="Save" icon="pi pi-check " class="p-button" @click="editProject" />
                <Button v-else-if="!isEditing" label="Save" icon="pi pi-check " class="p-button p-button-success"
                    @click="saveProject" />
                    <Button label="Cancel" icon="pi pi-times" class="p-button p-button-danger" @click="hideDialog" />
            </template>
        </Dialog>

        <Dialog v-model:visible="deleteProjectDialog" :style="{width: '450px'}" header="Confirm" :modal="true" :closable="false">
            <div class="confirmation-content">
                <i class="pi pi-exclamation-triangle" style="font-size: 2rem; margin-right: 0.5em;" />
                <span v-if="project">Are you sure you want to delete <b>{{project.name}}</b>?</span>
            </div>
            <template #footer>
                <Button label="Yes" icon="pi pi-check" class="p-button p-button-success" @click="deleteProjectById" />
                <Button label="No" icon="pi pi-times" class="p-button p-button-danger" @click="deleteProjectDialog = false" />
            </template>
        </Dialog>
    </div>
</template>

<script lang="ts">
import { onMounted, ref, watch } from 'vue';
import { defineComponent } from 'vue';
import Project from '@/types/Project';
import ProjectService from '../services/ProjectService';
import { FilterMatchMode } from 'primevue/api';
import Client from '@/types/Client';
import User from '@/types/User';
import ClientService from '../services/ClientService';
import UserService from '../services/UserService';
import ProjectWriteModel from '@/types/ProjectWriteModel';
import { useRouter } from 'vue-router'

export default defineComponent({
    setup() {
        const router = useRouter()

        const projectDialog = ref(false);
        const datatableKey = ref(0)
        const offset = ref(0)
        const currentPage = ref(0)
        const size = ref(5)
        const pageParam = ref('page=' + currentPage.value)
        const sizeParam = ref('size=' + size.value)
        const sortParam = ref('')
        const filterNameParam = ref('')
        const filterClientNameParam = ref('')
        const filterOwnerNameParam = ref('')
        const params = ref<string>(pageParam.value + '&' + sizeParam.value + '&' + sortParam.value + '&' + filterNameParam.value + '&' + filterClientNameParam.value + '&' + filterOwnerNameParam.value)
        const projectService = ref(new ProjectService());
        const { projects, totalRecords, errorGetProjects, loadGetProjects } = projectService.value.getProjects()
        const isEditing = ref(false);
        const deleteProjectDialog = ref(false)
        const submitted = ref(false)
        const renderComponent = ref(true)
        const project = ref<Project>({id: 0, name: '', client:{id:0, name: '', note: ''}, owner:{id:0, name:'', surname: '', email: ''}})
        const selectedClient = ref<Client>()
        const selectedOwner = ref<User>()
        const clientService = ref(new ClientService());
        const { clients, errorGetClients, loadGetClients } = clientService.value.getClients()
        const userService = ref(new UserService());
        const { users, errorGetUsers, loadGetUsers } = userService.value.getUsers()

        onMounted(() => {
            loadGetProjects(params.value);
        })

        const filters1 = ref({
            'name': { value: null, matchMode: FilterMatchMode.CONTAINS },
            'client.name': { value: null, matchMode: FilterMatchMode.CONTAINS },
            'owner.name': { value: null, matchMode: FilterMatchMode.CONTAINS }
        });

        watch(size, (s) => {
            sizeParam.value = 'size=' + s
        })
        watch(currentPage, (cp) => {
            pageParam.value = 'page=' + cp
        })
        watch([sizeParam, pageParam, sortParam, filterNameParam, filterClientNameParam, filterOwnerNameParam], (p) => {
            params.value = p.join('&')
            loadGetProjects(params.value)
        })

        
        const openNew = () => {
            projectDialog.value = true;
            loadGetClients()
            loadGetUsers()
        };

        const hideDialog = () => {
            selectedClient.value = undefined
            selectedOwner.value = undefined
            project.value = {id: 0, name: '', client:{id:0, name: '', note: ''}, owner:{id:0, name:'', surname: '', email: '',}};
            projectDialog.value = false;
        };

        const saveProject = () => {
            const projectToAdd = ref<ProjectWriteModel>({id:0, name: '', clientId: 0, ownerId: 0})
            projectToAdd.value.id = project.value.id
            projectToAdd.value.name = project.value.name
            projectToAdd.value.clientId = selectedClient.value!.id
            projectToAdd.value.ownerId = selectedOwner.value!.id
            const { addedProject, loadAddProject } = projectService.value.addProject();
            loadAddProject(projectToAdd.value);
            watch(addedProject, () => {
                loadGetProjects(params.value)
            })
            project.value = {id: 0, name: '', client:{id:0, name: '', note: ''}, owner:{id:0, name:'', surname: '', email: '',}};
            projectDialog.value = false;
            selectedClient.value = undefined
            selectedOwner.value = undefined
        }
        const openEdit = (proj: Project) => {
            loadGetClients()
            loadGetUsers()
            isEditing.value = true;
            projectDialog.value = true;
            project.value = { ...proj };
            selectedClient.value = proj.client
            selectedOwner.value = proj.owner
        };

        const editProject = () => {
            const projectToEdit = ref<ProjectWriteModel>({id:0, name: '', clientId: 0, ownerId: 0})
            projectToEdit.value.id = project.value.id
            projectToEdit.value.name = project.value.name
            projectToEdit.value.clientId = selectedClient.value!.id
            projectToEdit.value.ownerId = selectedOwner.value!.id
            const { editedProject, loadEditProject } = projectService.value.editProject();
            loadEditProject(project.value.id, projectToEdit.value);
            watch(editedProject, () => {
                loadGetProjects(params.value)
            })
            project.value = {id: 0, name: '', client:{id:0, name: '', note: ''}, owner:{id:0, name:'', surname: '', email: '',}};
            projectDialog.value = false;
            isEditing.value = false;
        }

        const confirmDeleteProject = (c: number) => {
            project.value.id = c;
            deleteProjectDialog.value = true;
        };

        const deleteProjectById = () => {
            const { resp, loadDeleteProject } = projectService.value.deleteProject();
            loadDeleteProject(project.value.id)
            watch(resp, () => {
                loadGetProjects(params.value);
            })
            deleteProjectDialog.value = false;
            project.value = {id: 0, name: '', client:{id:0, name: '', note: ''}, owner:{id:0, name:'', surname: '', email: '',}};
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

        const onFilter = (prop: 'name' | 'clientName' | 'ownerFullName', input: string) => {
            if (input === null) {
                return;
            }
            currentPage.value = 0
            if(prop === 'name'){
                filterNameParam.value = prop + '=' + input
            }
            if(prop === 'clientName'){
                filterClientNameParam.value = prop + '=' + input
            }
            if(prop === 'ownerFullName'){
                filterOwnerNameParam.value = prop + '=' + input
            }
        }

        const clearFilters = () => {
            currentPage.value = 0
            offset.value = 0
            sortParam.value = ''
            filterNameParam.value = ''
            filterClientNameParam.value = ''
            filterOwnerNameParam.value = ''
            datatableKey.value++
        }

        const goToTasks = (projId: number) => {
            router.push({name: 'Tasks', params: {projectId: projId}})
        }

        const goToProjectUsers = (projId: number) => {
            router.push({name: 'ProjectUsers', params: {projectId: projId}})
        }

        return {
            projects, errorGetProjects, currentPage, size, totalRecords, submitted, project, isEditing, projectDialog,
            openNew, openEdit, hideDialog, saveProject, renderComponent, deleteProjectDialog,
            confirmDeleteProject, deleteProjectById, editProject, onPage, onSort, offset, filters1, onFilter, clearFilters, datatableKey,
            clients, users, selectedClient, selectedOwner, goToTasks, goToProjectUsers
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

.table-header-footer {
    display: flex;
    align-items: center;
    justify-content: space-between;
}
</style>
