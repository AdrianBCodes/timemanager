<template>
    <div class="table">
        <Toolbar class="mb-4">
            <template #start>
                <Button label="Back" icon="pi pi-angle-left" class="p-button p-button-primary" @click="backToProjects()"/>
            </template>
        </Toolbar>

        <DataTable :key="datatableKey" :value="users" showGridlines stripedRows responsiveLayout="scroll"
            :scrollable="true" scrollHeight="flex" :rows="size" @sort="onSort($event)" v-model:filters="filters1"
            filterDisplay="row">
            <template #header>
                <div class="table-header">
                    <div class="table-header-group-left" >
                        Project Users
                        <Dropdown v-model="selectedUser" :options="participantsToAdd" optionLabel="email" :filter="true" placeholder="Select User" :showClear="true" style="width: 16rem; margin-left: 1rem">
                            <template #value="slotProps" >
                                <div v-if="slotProps.value" style="padding-right: 6rem;" >
                                    <div>{{slotProps.value.email}}</div>
                                </div>
                                <span v-else style="padding-right: 6rem;">
                                    {{slotProps.placeholder}}
                                </span>
                            </template>
                            <template #option="slotProps">
                                <div>
                                    <div>{{slotProps.option.email}}</div>
                                </div>
                            </template>
                        </Dropdown>
                            <Button label="Add" icon="pi pi-plus" class="p-button p-button-success" style="margin-left: 1rem" @click="addParticipant"/>
                    </div>
                    <div class="table-header-group-right" >
                        <Button label="Clear filters" class="p-button-secondary" @click="clearFilters()"></Button>
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
            <Column field="surname" header="Surname" :sortable="true" filterField="surname" :showFilterMenu="false"
                :show-clear-button="false">
                <template #filter="{filterModel}">
                    <InputText type="text" v-model="filterModel.value"
                        @keydown.enter="onFilter('surname', filterModel.value)" class="p-column-filter"
                        :placeholder="`Search by surname `" v-tooltip.top.focus="'Hit enter key to filter'" />
                </template>
            </Column>
            <Column field="email" header="Email" :sortable="true" filterField="email" :showFilterMenu="false"
                :show-clear-button="false">
                <template #filter="{filterModel}">
                    <InputText type="text" v-model="filterModel.value"
                        @keydown.enter="onFilter('email', filterModel.value)" class="p-column-filter"
                        :placeholder="`Search by email `" v-tooltip.top.focus="'Hit enter key to filter'" />
                </template>
            </Column>
            <Column :exportable="false" style="max-width:12rem ">
                <template #body="slotProps">
                    <Button icon="pi pi-trash" class="p-button-rounded p-button-danger" @click="removeParticipant(slotProps.data.id)"/>
                </template>
            </Column>
            <template #footer>
                <Paginator :first="offset" :rows="size" :totalRecords="totalRecords" @page="onPage($event)"
                    :rowsPerPageOptions="[5,10,25,50,100]"
                    currentPageReportTemplate="Showing {first} - {last} of {totalRecords} users"
                    template="CurrentPageReport FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink RowsPerPageDropdown" />
            </template>
        </DataTable>
    </div>
</template>

<script lang="ts">
import { onMounted, ref, watch } from 'vue';
import { defineComponent } from 'vue';
import { FilterMatchMode } from 'primevue/api';
import User from '@/types/User';
import UserService from '../services/UserService';
import ProjectService from '../services/ProjectService';
import { useRouter } from 'vue-router';

export default defineComponent({
    props: {
        projectId: Number
    },
    setup(props) {
        const router = useRouter()
        const datatableKey = ref(0)
        const offset = ref(0)
        const currentPage = ref(0)
        const size = ref(5)
        const pageParam = ref('page=' + currentPage.value)
        const sizeParam = ref('size=' + size.value)
        const sortParam = ref('')
        const projectIdParam = ref('projectId=' + props.projectId)
        const filterNameParam = ref('')
        const filterSurnameParam = ref('')
        const filterEmailParam = ref('')
        const params = ref<string>(pageParam.value + '&' + sizeParam.value + '&' + sortParam.value + '&' + filterNameParam.value + '&' + filterSurnameParam.value + '&' + filterEmailParam.value + '&' + projectIdParam.value)
        const userService = new UserService();
        const projectService = new ProjectService();


        const { users, totalRecords, errorGetUsers, loadGetUsers } = userService.getUsers()
        const { participantsToAdd, loadGetParticipantsToAdd } = userService.getParticipantsToAdd()
        const removeParticipantDialog = ref(false)
        const submitted = ref(false)
        const renderComponent = ref(true)
        const selectedUser = ref<User>()

        onMounted(() => {
            loadGetUsers(params.value);
            loadGetParticipantsToAdd(props.projectId)
        })

        const filters1 = ref({
            'name': { value: null, matchMode: FilterMatchMode.CONTAINS },
            'surname': { value: null, matchMode: FilterMatchMode.CONTAINS },
            'email': { value: null, matchMode: FilterMatchMode.CONTAINS }
        });

        watch(size, (s) => {
            sizeParam.value = 'size=' + s
        })
        watch(currentPage, (cp) => {
            pageParam.value = 'page=' + cp
        })
        watch([sizeParam, pageParam, sortParam, filterNameParam, filterSurnameParam, filterEmailParam, projectIdParam], (p) => {
            params.value = p.join('&')
            loadGetUsers(params.value)
        })
        
        const addParticipant = () => {
            const {resp, loadAddParticipant} = projectService.addParticipant()
            loadAddParticipant(props.projectId, selectedUser.value!.id)
            watch(resp, () => {
                loadGetUsers(params.value);
                loadGetParticipantsToAdd(props.projectId)
            })
            selectedUser.value = undefined
        };

        const removeParticipant = (userId: number) => {
            const {resp, loadRemoveParticipant} = projectService.removeParticipant()
            loadRemoveParticipant(props.projectId, userId)
            watch(resp, () => {
                loadGetUsers(params.value);
                loadGetParticipantsToAdd(props.projectId)
            })
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

        const onFilter = (prop: 'name' | 'surname' | 'email', input: string) => {
            if (input === null) {
                return;
            }
            currentPage.value = 0
            if(prop === 'name'){
                filterNameParam.value = prop + '=' + input
            }
            if(prop === 'email'){
                filterSurnameParam.value = prop + '=' + input
            }
            if(prop === 'surname'){
                filterEmailParam.value = prop + '=' + input
            }
        }
        const clearFilters = () => {
            currentPage.value = 0
            offset.value = 0
            sortParam.value = ''
            filterNameParam.value = ''
            filterSurnameParam.value = ''
            filterEmailParam.value = ''
            datatableKey.value++
        }

        const backToProjects = () => {
            router.push({name: 'Projects'})
        }

        return {
            users, participantsToAdd, selectedUser, currentPage, size, totalRecords, submitted, task: selectedUser, renderComponent,
            onPage, onSort, offset, filters1, onFilter, clearFilters, datatableKey, backToProjects, addParticipant, removeParticipant
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
