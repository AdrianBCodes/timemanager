<template>
    <div class="table">
        <Toolbar class="mb-4">
            <template #start>
                <Button label="Back" icon="pi pi-angle-left" class="p-button p-button-primary" @click="backToProjects()"/>
            </template>
        </Toolbar>

        <DataTable :key="datatableKey" :value="tasks" showGridlines stripedRows responsiveLayout="scroll"
            :scrollable="true" scrollHeight="flex" :rows="size" @sort="onSort($event)" v-model:filters="filters1"
            filterDisplay="row">
            <template #header>
                <div class="table-header">
                    <div class="table-header-group-left" >
                        Tasks
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
            <Column field="description" header="Description" :sortable="true" fielterField="description" :showFilterMenu="false"
                :show-clear-button="false">
                <template #filter="{filterModel}">
                    <InputText type="text" v-model="filterModel.value"
                        @keydown.enter="onFilter('description', filterModel.value)" class="p-column-filter"
                        :placeholder="`Search by description `" v-tooltip.top.focus="'Hit enter key to filter'" />
                </template>
            </Column>
            <Column header="Tags" filterField="tags" :showFilterMenu="false" :showClearButton="false" style="max-width:12rem">
                    <template #body="{data}">
                        <div style="display: flex; flex-wrap: wrap" >
                            <div v-for="tag in data.tags" :key="tag.id" >
                                <Tag style="margin-right:0.3rem">{{tag.name}}</Tag>
                            </div>
                        </div>
                    </template>
                    <template #filter="{filterModel}">
                        <MultiSelect v-model="filterModel.value" :filter="true" @change="onTagsFilter(filterModel.value)" :options="tags" optionLabel="name" placeholder="Choose Tags" class="p-column-filter">
                            <template #option="slotProps">
                                <div>
                                    <span>{{slotProps.option.name}}</span>
                                </div>
                            </template>
                        </MultiSelect>
                    </template>
            </Column>
            <Column :exportable="false" style="max-width:12rem ">
                <template #body="slotProps">
                    <Button icon="pi pi-pencil" class="p-button-rounded p-button-success"
                        @click="openEdit(slotProps.data)" />
                    <Button icon="pi pi-trash" class="p-button-rounded p-button-danger"
                        @click="confirmDeleteTask(slotProps.data.id)" />
                </template>
            </Column>
            <template #footer>
                <Paginator :first="offset" :rows="size" :totalRecords="totalRecords" @page="onPage($event)"
                    :rowsPerPageOptions="[5,10,25,50,100]"
                    currentPageReportTemplate="Showing {first} - {last} of {totalRecords} tasks"
                    template="CurrentPageReport FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink RowsPerPageDropdown" />
            </template>
        </DataTable>

        <Dialog v-model:visible="taskDialog" :style="{width: '450px'}" header="Task Details" :modal="true" :closable="false"
            class="p-fluid">
            <div class="field">
                <label for="name">Name</label>
                <InputText id="name" v-model.trim="task.name" required="true" autofocus />
                <small class="p-error" v-if="submitted && !task.name">Name is required.</small>
            </div>
            <div class="field">
                <label for="description">Description</label>
                <Textarea id="description" v-model="task.description" required="true" rows="3" cols="20" />
            </div>
            <div class="field">
                <MultiSelect v-model="selectedTags" :filter="true" :options="tags" optionLabel="name" placeholder="Choose Tags" class="p-column-filter">
                    <template #option="slotProps">
                        <div>
                            <span>{{slotProps.option.name}}</span>
                        </div>
                    </template>
                </MultiSelect>
            </div>
            <template #footer>
                <Button v-if="isEditing" label="Save" icon="pi pi-check " class="p-button" @click="editTask" />
                <Button v-else-if="!isEditing" label="Save" icon="pi pi-check " class="p-button p-button-success"
                    @click="saveTask" />
                    <Button label="Cancel" icon="pi pi-times" class="p-button p-button-danger" @click="hideDialog" />
            </template>
        </Dialog>

        <Dialog v-model:visible="deleteTaskDialog" :style="{width: '450px'}" header="Confirm" :modal="true" :closable="false">
            <div class="confirmation-content">
                <i class="pi pi-exclamation-triangle" style="font-size: 2rem; margin-right: 0.5em;" />
                <span v-if="task">Are you sure you want to delete <b>{{task.name}}</b>?</span>
            </div>
            <template #footer>
                <Button label="Yes" icon="pi pi-check" class="p-button p-button-success" @click="deleteTaskById" />
                <Button label="No" icon="pi pi-times" class="p-button p-button-danger" @click="deleteTaskDialog = false" />
            </template>
        </Dialog>
    </div>
</template>

<script lang="ts">
import { onMounted, ref, watch } from 'vue';
import { defineComponent } from 'vue';
import Task from '@/types/Task';
import TaskService from '../services/TaskService';
import { FilterMatchMode } from 'primevue/api';
import TagService from '../services/TagService';
import Tag from '@/types/Tag';
import { useRouter } from 'vue-router';

export default defineComponent({
    props: {
        projectId: Number
    },
    setup(props) {
        const router = useRouter()
        const taskDialog = ref(false);
        const datatableKey = ref(0)
        const offset = ref(0)
        const currentPage = ref(0)
        const size = ref(5)
        const pageParam = ref('page=' + currentPage.value)
        const sizeParam = ref('size=' + size.value)
        const sortParam = ref('')
        const projectIdParam = ref('projectId=' + props.projectId)
        const filterNameParam = ref('')
        const filterTagsParam = ref('')
        const filterDescriptionParam = ref('')
        const params = ref<string>(pageParam.value + '&' + sizeParam.value + '&' + projectIdParam.value + '&' + sortParam.value + '&' + filterNameParam.value + '&' + filterDescriptionParam.value  + '&' + filterTagsParam.value)
        const taskService = new TaskService();
        const { tasks , totalRecords, errorGetTasks, loadGetTasks } = taskService.getTasks()
        const isEditing = ref(false);
        const deleteTaskDialog = ref(false)
        const submitted = ref(false)
        const renderComponent = ref(true)
        const task = ref<Task>({
            id: 0,
            name: '',
            description: '',
            projectId: props.projectId,
            tags: []
        })

        // TODO - add multiselect to dialogs
        const tagService = ref(new TagService());
        const { tags, loadGetTags } = tagService.value.getTags()
        const selectedTags = ref()

        onMounted(() => {
            loadGetTasks(params.value);
            loadGetTags()
        })

        const filters1 = ref({
            'name': { value: null, matchMode: FilterMatchMode.CONTAINS },
            'description': { value: null, matchMode: FilterMatchMode.CONTAINS },
            'tags': {value: null, matchMode: FilterMatchMode.IN}
        });

        watch(size, (s) => {
            sizeParam.value = 'size=' + s
        })
        watch(currentPage, (cp) => {
            pageParam.value = 'page=' + cp
        })
        watch([sizeParam, pageParam, projectIdParam, sortParam, filterNameParam, filterDescriptionParam], (p) => {
            params.value = p.join('&')
            loadGetTasks(params.value)
        })

        watch(selectedTags, (s) => {
            task.value.tags = s
        })

        
        const openNew = () => {
            taskDialog.value = true;
            loadGetTags()
        };

        const hideDialog = () => {
            taskDialog.value = false;
            task.value = {
            id: 0,
            name: '',
            description: '',
            projectId: props.projectId,
            tags: []
        };
        };

        const saveTask = () => {
            const { addedTask, loadAddTask } = taskService.addTask();
            loadAddTask(task.value);
            watch(addedTask, () => {
                loadGetTasks(params.value)
            })
            task.value = {
            id: 0,
            name: '',
            description: '',
            projectId: props.projectId,
            tags: []
        };
            taskDialog.value = false;
        }
        const openEdit = (cli: Task) => {
            isEditing.value = true;
            taskDialog.value = true;
            task.value = { ...cli };
        };

        const editTask = () => {
            const { editedTask, loadEditTask } = taskService.editTask();
            loadEditTask(task.value.id, task.value);
            watch(editedTask, () => {
                loadGetTasks(params.value)
            })
            task.value = {
            id: 0,
            name: '',
            description: '',
            projectId: props.projectId,
            tags: []
        };
            taskDialog.value = false;
            isEditing.value = false;
        }

        const confirmDeleteTask = (c: number) => {
            task.value.id = c;
            deleteTaskDialog.value = true;
        };

        const deleteTaskById = () => {
            const { resp, loadDeleteTask } = taskService.deleteTask();
            loadDeleteTask(task.value.id)
            watch(resp, () => {
                loadGetTasks(params.value);
            })
            deleteTaskDialog.value = false;
            task.value = {
            id: 0,
            name: '',
            description: '',
            projectId: props.projectId,
            tags: []
        };
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

        const onFilter = (prop: 'name' | 'description', input: string) => {
            if (input === null) {
                return;
            }
            currentPage.value = 0
            if(prop === 'name'){
                filterNameParam.value = prop + '=' + input
            }
            if(prop === 'description'){
                filterDescriptionParam.value = prop + '=' + input
            }
        }

        const onTagsFilter = (input: Tag[]) => {
            if (input === null) {
                return;
            }
            currentPage.value = 0
            filterTagsParam.value = 'tagsIds='
            input.forEach(c => {
                filterTagsParam.value = filterTagsParam.value.concat(c.id.toString())
            })
        }

        const clearFilters = () => {
            currentPage.value = 0
            offset.value = 0
            sortParam.value = ''
            filterNameParam.value = ''
            filterDescriptionParam.value = ''
            datatableKey.value++
        }

        const backToProjects = () => {
            router.push({name: 'Projects'})
        }

        return {
            tasks, errorGetTasks, currentPage, size, totalRecords, submitted, task, isEditing, taskDialog,
            openNew, openEdit, hideDialog, saveTask, renderComponent, deleteTaskDialog, onTagsFilter, backToProjects,
            confirmDeleteTask, deleteTaskById, editTask, onPage, onSort, offset, filters1, onFilter, clearFilters, datatableKey, tags, selectedTags
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
