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
            <Column :exportable="false" style="max-width:12rem ">
                <template #body="slotProps">
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

        <Dialog v-model:visible="projectDialog" :style="{width: '450px'}" header="Project Details" :modal="true"
            class="p-fluid">
            <div class="field">
                <label for="name">Name</label>
                <InputText id="name" v-model.trim="project.name" required="true" autofocus />
                <small class="p-error" v-if="submitted && !project.name">Name is required.</small>
            </div>
            <!-- TODO dropdown for clients and owners -->
            <template #footer>
                <Button v-if="isEditing" label="Save" icon="pi pi-check " class="p-button" @click="editProject" />
                <Button v-else-if="!isEditing" label="Save" icon="pi pi-check " class="p-button p-button-success"
                    @click="saveProject" />
                    <Button label="Cancel" icon="pi pi-times" class="p-button p-button-danger" @click="hideDialog" />
            </template>
        </Dialog>

        <Dialog v-model:visible="deleteProjectDialog" :style="{width: '450px'}" header="Confirm" :modal="true">
            <div class="confirmation-content">
                <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem" />
                <span v-if="project">Are you sure you want to delete <b>{{project.name}}</b>?</span>
            </div>
            <template #footer>
                <Button label="No" icon="pi pi-times" class="p-button-text" @click="deleteProjectDialog = false" />
                <Button label="Yes" icon="pi pi-check" class="p-button-text" @click="deleteProjectById" />
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

export default defineComponent({
    setup() {
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
        const { projects, totalRecords, error, load } = projectService.value.getProjects()
        const isEditing = ref(false);
        const deleteProjectDialog = ref(false)
        const submitted = ref(false)
        const renderComponent = ref(true)
        const project = ref<Project>({id: 0, name: '', client:{id:0, name: '', note: ''}, owner:{id:0, name:'', surname: '', email: '',}})

        onMounted(() => {
            load(params.value);
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
            load(params.value)
        })

        
        const openNew = () => {
            projectDialog.value = true;
        };

        const hideDialog = () => {
            projectDialog.value = false;
        };

        const saveProject = () => {
            const { addedProject, loadAddProject } = projectService.value.addProject();
            loadAddProject(project.value);
            watch(addedProject, () => {
                load(params.value)
            })
            project.value = {id: 0, name: '', client:{id:0, name: '', note: ''}, owner:{id:0, name:'', surname: '', email: '',}};
            projectDialog.value = false;
        }
        const openEdit = (cli: Project) => {
            isEditing.value = true;
            projectDialog.value = true;
            project.value = { ...cli };
        };

        const editProject = () => {
            const { editedProject, loadEditProject } = projectService.value.editProject();
            loadEditProject(project.value.id, project.value);
            watch(editedProject, () => {
                load(params.value)
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
                load(params.value);
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

        return {
            projects, error, currentPage, size, totalRecords, submitted, project, isEditing, projectDialog,
            openNew, openEdit, hideDialog, saveProject, renderComponent, deleteProjectDialog,
            confirmDeleteProject, deleteProjectById, editProject, onPage, onSort, offset, filters1, onFilter, clearFilters, datatableKey
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
