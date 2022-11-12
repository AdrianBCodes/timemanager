<template>
    <div class="table">
        <DataTable :key="datatableKey" :value="users" showGridlines stripedRows responsiveLayout="scroll"
            :scrollable="true" scrollHeight="flex" :rows="size" @sort="onSort($event)" v-model:filters="filters1"
            filterDisplay="row">
            <template #header>
                <div class="table-header">
                    <div class="table-header-group-left" >
                        Users
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
                    <Button icon="pi pi-pencil" class="p-button-rounded p-button-success"
                        @click="openEdit(slotProps.data)" />
                    <Button icon="pi pi-trash" class="p-button-rounded p-button-danger"
                        @click="confirmDeleteUser(slotProps.data.id)" />
                </template>
            </Column>
            <template #footer>
                <Paginator :first="offset" :rows="size" :totalRecords="totalRecords" @page="onPage($event)"
                    :rowsPerPageOptions="[5,10,25,50,100]"
                    currentPageReportTemplate="Showing {first} - {last} of {totalRecords} users"
                    template="CurrentPageReport FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink RowsPerPageDropdown" />
            </template>
        </DataTable>

        <Dialog v-model:visible="userDialog" :style="{width: '450px'}" header="User Details" :modal="true" :closable="false"
            class="p-fluid">
            <div class="field">
                <label for="name">Name</label>
                <InputText id="name" v-model.trim="user.name" required="true" autofocus />
                <small class="p-error" v-if="submitted && !user.name">Name is required.</small>
            </div>
            <div class="field">
                <label for="surname">Surname</label>
                <InputText id="surname" v-model.trim="user.surname" required="true" autofocus />
                <small class="p-error" v-if="submitted && !user.surname">Surname is required.</small>
            </div>
            <div class="field">
                <label for="email">Email</label>
                <InputText id="email" v-model.trim="user.email" required="true" autofocus />
                <small class="p-error" v-if="submitted && !user.email">Email is required.</small>
            </div>
            <template #footer>
                <Button v-if="isEditing" label="Save" icon="pi pi-check " class="p-button" @click="editUser" />
                <Button v-else-if="!isEditing" label="Save" icon="pi pi-check " class="p-button p-button-success"
                    @click="saveUser" />
                    <Button label="Cancel" icon="pi pi-times" class="p-button p-button-danger" @click="hideDialog" />
            </template>
        </Dialog>

        <Dialog v-model:visible="deleteUserDialog" :style="{width: '450px'}" header="Confirm" :modal="true" :closable="false">
            <div class="confirmation-content">
                <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem; margin-right: 0.5em;" />
                <span v-if="user">Are you sure you want to delete <b>{{user.name}}</b>?</span>
            </div>
            <template #footer>
                <Button label="Yes" icon="pi pi-check" class="p-button p-button-success" @click="deleteUserById" />
                <Button label="No" icon="pi pi-times" class="p-button p-button-danger" @click="deleteUserDialog = false" />
            </template>
        </Dialog>
    </div>
</template>

<script lang="ts">
import { onMounted, ref, watch } from 'vue';
import { defineComponent } from 'vue';
import User from '@/types/User';
import UserService from '../services/UserService';
import { FilterMatchMode } from 'primevue/api';

export default defineComponent({
    setup() {
        const userDialog = ref(false);
        const datatableKey = ref(0)
        const offset = ref(0)
        const currentPage = ref(0)
        const size = ref(5)
        const pageParam = ref('page=' + currentPage.value)
        const sizeParam = ref('size=' + size.value)
        const sortParam = ref('')
        const filterNameParam = ref('')
        const filterSurnameParam = ref('')
        const filterEmailParam = ref('')
        const params = ref<string>(pageParam.value + '&' + sizeParam.value + '&' + sortParam.value + '&' + filterNameParam.value + '&' + filterSurnameParam.value + '&' + filterEmailParam.value)
        const userService = ref(new UserService());
        const { users, totalRecords, errorGetUsers, loadGetUsers } = userService.value.getUsers()
        const isEditing = ref(false);
        const deleteUserDialog = ref(false)
        const submitted = ref(false)
        const renderComponent = ref(true)
        const user = ref<User>({id: 0, name: '', surname: '', email: ''})

        onMounted(() => {
            loadGetUsers(params.value);
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
        watch([sizeParam, pageParam, sortParam, filterNameParam, filterSurnameParam, filterEmailParam], (p) => {
            params.value = p.join('&')
            loadGetUsers(params.value)
        })

        
        const openNew = () => {
            userDialog.value = true;
        };

        const hideDialog = () => {
            userDialog.value = false;
            user.value = {id: 0, name: '', surname: '', email: ''}
        };

        const saveUser = () => {
            const { addedUser, loadAddUser } = userService.value.addUser();
            loadAddUser(user.value);
            watch(addedUser, () => {
                loadGetUsers(params.value)
            })
            user.value = {id: 0, name: '', surname: '', email: ''}
            userDialog.value = false;
        }
        const openEdit = (t: User) => {
            isEditing.value = true;
            userDialog.value = true;
            user.value = { ...t };
        };

        const editUser = () => {
            const { editedUser, loadEditUser } = userService.value.editUser();
            loadEditUser(user.value.id, user.value);
            watch(editedUser, () => {
                loadGetUsers(params.value)
            })
            user.value = {id: 0, name: '', surname: '', email: ''}
            userDialog.value = false;
            isEditing.value = false;
        }

        const confirmDeleteUser = (c: number) => {
            user.value.id = c;
            deleteUserDialog.value = true;
        };

        const deleteUserById = () => {
            const { resp, loadDeleteUser } = userService.value.deleteUser();
            loadDeleteUser(user.value.id)
            watch(resp, () => {
                loadGetUsers(params.value);
            })
            deleteUserDialog.value = false;
            user.value = {id: 0, name: '', surname: '', email: ''}
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

        return {
            users, errorGetUsers, currentPage, size, totalRecords, submitted, user, isEditing, userDialog,
            openNew, openEdit, hideDialog, saveUser, renderComponent, deleteUserDialog,
            confirmDeleteUser, deleteUserById, editUser, onPage, onSort, offset, filters1, onFilter, clearFilters, datatableKey
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
