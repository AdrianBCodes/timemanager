<template>
    <div class="table">
        <DataTable :key="datatableKey" :value="trackerEventsPaged" showGridlines stripedRows responsiveLayout="scroll"
            :scrollable="true" scrollHeight="flex" :rows="size" @sort="onSort($event)" v-model:filters="filters1"
            filterDisplay="row">
            <template #header>
                <div class="table-header">
                    <div class="table-header-group-left" >
                        Reports
                        <Button label="Export" class="p-button p-button-success" style="margin-left: 1rem" @click="exportToExcel"></Button>
                    </div>
                    <div class="table-header-group-right" >
                        <span>
                            <Button label="Clear filters" class="p-button-secondary" @click="clearFilters()"></Button>
                        </span>
                    </div>
                </div>
            </template>
            <Column field="project.client.name" header="Client" filterField="project.client" :showFilterMenu="false" :showClearButton="false">
                    <template #filter="{filterModel}">
                        <MultiSelect v-model="filterModel.value" :filter="true" @change="onClientFilter(filterModel.value)" :options="clients" optionLabel="name" placeholder="Choose clients" class="p-column-filter">
                            <template #option="slotProps">
                                <div>
                                    <span>{{slotProps.option.name}}</span>
                                </div>
                            </template>
                        </MultiSelect>
                    </template>
            </Column>
            <Column header="User" filterField="user.name" :showFilterMenu="false"
                :show-clear-button="false">
                <template #body="{data}">
                        <span>{{data.user.name + ' ' + data.user.surname}}</span>
                    </template>
                <template #filter="{filterModel}">
                    <MultiSelect v-model="filterModel.value" :filter="true" @change="onClientFilter(filterModel.value)" :options="users" optionLabel="name" placeholder="Choose user" class="p-column-filter">
                            <template #option="slotProps">
                                <div>
                                    <span>{{slotProps.option.name + ' ' + slotProps.option.surname}}</span>
                                </div>
                            </template>
                        </MultiSelect>
                </template>
            </Column>
            <Column field="project.name" header="Project" filterField="project.name" :showFilterMenu="false" :showClearButton="false">
                    <template #filter="{filterModel}">
                        <MultiSelect v-model="filterModel.value" :filter="true" @change="onClientFilter(filterModel.value)" :options="projects" optionLabel="name" placeholder="Choose projects" class="p-column-filter">
                            <template #option="slotProps">
                                <div>
                                    <span>{{slotProps.option.name}}</span>
                                </div>
                            </template>
                        </MultiSelect>
                    </template>
            </Column>
            <Column field="task.name" header="Task" filterField="task" :showFilterMenu="false" :showClearButton="false">
                    <template #filter="{filterModel}">
                        <MultiSelect v-model="filterModel.value" :filter="true" @change="onClientFilter(filterModel.value)" :options="tasks" optionLabel="name" placeholder="Choose tasks" class="p-column-filter">
                            <template #option="slotProps">
                                <div>
                                    <span>{{slotProps.option.name}}</span>
                                </div>
                            </template>
                        </MultiSelect>
                    </template>
            </Column>
            <Column field="description" header="Description" :sortable="true" filterField="description" :showFilterMenu="false"
                :show-clear-button="false">
                <template #filter="{filterModel}">
                    <InputText type="text" v-model="filterModel.value"
                        @keydown.enter="onFilter('description', filterModel.value)" class="p-column-filter"
                        :placeholder="`Search by description `" v-tooltip.top.focus="'Hit enter key to filter'" />
                </template>
            </Column>
            <Column field="date" header="Date" :sortable="true" filterField="date" :showFilterMenu="false"
                :show-clear-button="false">
                <template #filter="{}">
                    <div style="display: flex">
                    <Calendar style="max-width: 80%" v-model="selectedDate" dateFormat="dd/mm/yy" placeholder="dd/mm/yyyy"  />
                    <i class="pi pi-filter-slash" style="font-size: 1.3rem; margin: auto;" v-if="selectedDate" @click="clearSelectedDate" />
                    <!-- <Button style="max-width: 20%" v-if="selectedDate" @click="clearSelectedDate"></Button> -->
                    </div>
                </template>
            </Column>

            <Column field="duration" header="Duration" :sortable="true" filterField="duration" :showFilterMenu="false"
                :show-clear-button="false">
                <template #body="slotProps">
                    <span> {{ durationMinutesToString(slotProps.data.duration) }}</span>
                </template>
<!--                 TODO - filter for duration (better time picker and multiple filter options) -->
                <template #filter="{filterModel}">
                    <Calendar v-model="filterModel.value" :showTime="true" :timeOnly="true" :stepMinute="30"/>
                </template>
            </Column>
            <template #footer>
                <Paginator :first="offset" :rows="size" :totalRecords="totalRecords" @page="onPage($event)"
                    :rowsPerPageOptions="[5,10,25,50,100]"
                    currentPageReportTemplate="Showing {first} - {last} of {totalRecords} Tracker Events"
                    template="CurrentPageReport FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink RowsPerPageDropdown" />
            </template>
        </DataTable>
    </div>
</template>

<script lang="ts">
import Client from '@/types/Client';
import Project from '@/types/Project';
import User from '@/types/User';
import { FilterMatchMode } from 'primevue/api';
import { defineComponent, onMounted, ref, watch } from 'vue';
import ClientService from '../services/ClientService';
import ProjectService from '../services/ProjectService';
import TaskService from '../services/TaskService';
import TrackerEventService from '../services/TrackerEventService';
import UserService from '../services/UserService';

export default defineComponent({
    setup() {
        const datatableKey = ref(0)
        const offset = ref(0)
        const currentPage = ref(0)
        const size = ref(5)
        const pageParam = ref('page=' + currentPage.value)
        const sizeParam = ref('size=' + size.value)
        const sortParam = ref('')
        const selectedDate = ref<Date>()
        const trackerEventService = new TrackerEventService()
        const {trackerEventsPaged, totalRecords, loadGetTrackerEventsPaged} = trackerEventService.getTrackerEventsPaged()
        const filterDescriptionParam = ref('')
        const filterClientsParam = ref('')
        const filterUsersParam = ref('')
        const filterDateParam = ref('')
        const params = ref<string>(pageParam.value + '&' + sizeParam.value + '&' + sortParam.value + '&' + filterDescriptionParam.value + '&' + filterClientsParam.value + '&' + filterDateParam.value)
        const projectService = new ProjectService();
        const { projects, errorGetProjects, loadGetProjects } = projectService.getProjects()
        const isEditing = ref(false);
        const deleteProjectDialog = ref(false)
        const submitted = ref(false)
        const renderComponent = ref(true)
        const project = ref<Project>({id: 0, name: '', client:{id:0, name: '', note: ''}, owner:{id:0, name:'', surname: '', email: ''}})
        const selectedClient = ref<Client>()
        const selectedOwner = ref<User>()

        const taskService = new TaskService()
        const { tasks, errorGetTasks, loadGetTasks} = taskService.getTasks()
        // TODO - get all clients without paging 
        const clientService = new ClientService();
        const { clients, errorGetClients, loadGetClients } = clientService.getClients()
        const userService = new UserService();
        const { users, errorGetUsers, loadGetUsers } = userService.getUsers()

        onMounted(() => {
            loadGetTrackerEventsPaged(params.value);
            loadGetProjects();
            loadGetClients()
            loadGetUsers()
            loadGetTasks()
        })


        const filters1 = ref({
            'project.client': { value: null, matchMode: FilterMatchMode.IN },
            'task': { value: null, matchMode: FilterMatchMode.IN },
            'user.name': { value: null, matchMode: FilterMatchMode.IN },
            'project.name': { value: null, matchMode: FilterMatchMode.CONTAINS },
            'description': { value: null, matchMode: FilterMatchMode.CONTAINS },
            'duration': { value: null, matchMode: FilterMatchMode.CONTAINS },
            'date': { value: null, matchMode: FilterMatchMode.EQUALS },
        });

        watch(size, (s) => {
            sizeParam.value = 'size=' + s
        })
        watch(currentPage, (cp) => {
            pageParam.value = 'page=' + cp
        })
        watch([sizeParam, pageParam, sortParam, filterDescriptionParam, filterClientsParam, filterUsersParam, filterDateParam], (p) => {
            params.value = p.join('&')
            loadGetTrackerEventsPaged(params.value);
        })

        watch(selectedDate, () => {
            if(selectedDate.value != undefined){
                filterDateParam.value = 'date=' + selectedDate.value?.toLocaleDateString('en-GB') + ' 00:00'
            }
        })

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

        const onFilter = (prop: 'description', input: string) => {
            if (input === null) {
                return;
            }
            currentPage.value = 0
            if(prop === 'description'){
                filterDescriptionParam.value = prop + '=' + input
            }
        }

        const onClientFilter = (input: Client[]) => {
            if (input === null) {
                return;
            }
            currentPage.value = 0
            filterClientsParam.value = 'clientsIds='
            input.forEach(c => {
                filterClientsParam.value = filterClientsParam.value.concat(c.id.toString()) + ','
            })
        }

        const onOwnerFilter = (input: User[]) => {
            if (input === null) {
                return;
            }
            currentPage.value = 0
            filterUsersParam.value = 'ownersIds='
            input.forEach(o => {
                filterUsersParam.value = filterUsersParam.value.concat(o.id.toString()) + ','
            })
        }

        const durationMinutesToString = (minutes: number) => {
            const hours = Math.floor(minutes / 60)
            const min = minutes - hours * 60
            if(hours < 10) {
                return '0' + hours + ':' + min;
            }
            return hours + ':' + min;
        }

        const clearSelectedDate = () => {
            selectedDate.value = undefined
            filterDateParam.value = ''
        }

        const clearFilters = () => {
            currentPage.value = 0
            offset.value = 0
            sortParam.value = ''
            filterDescriptionParam.value = ''
            filterClientsParam.value = ''
            filterUsersParam.value = ''
            datatableKey.value++
        }

        const exportToExcel = () => {
            const {loadGetTrackerEventsExport} = trackerEventService.getTrackerEventsExport();
            loadGetTrackerEventsExport(params.value)
        }

        return {
            trackerEventsPaged, projects, errorGetProjects, currentPage, size, totalRecords, submitted, project, isEditing, 
            renderComponent, deleteProjectDialog,
            onPage, onSort, offset, filters1, onFilter, clearFilters, datatableKey, exportToExcel,
            clients, users, selectedClient, selectedOwner, onClientFilter, onOwnerFilter, tasks, durationMinutesToString, selectedDate, clearSelectedDate
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
