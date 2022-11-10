<template>
    <div class="table">
        <Toolbar class="mb-4">
            <template #start>
                <Button label="New" icon="pi pi-plus" class="p-button p-button-success" @click="openNew" />
            </template>
        </Toolbar>

        <DataTable :key="datatableKey" :value="tags" showGridlines stripedRows responsiveLayout="scroll"
            :scrollable="true" scrollHeight="flex" :rows="size" @sort="onSort($event)" v-model:filters="filters1"
            filterDisplay="row">
            <template #header>
                <div class="table-header-footer">
                    Tags
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
            <Column :exportable="false" style="max-width:12rem ">
                <template #body="slotProps">
                    <Button icon="pi pi-pencil" class="p-button-rounded p-button-success"
                        @click="openEdit(slotProps.data)" />
                    <Button icon="pi pi-trash" class="p-button-rounded p-button-danger"
                        @click="confirmDeleteTag(slotProps.data.id)" />
                </template>
            </Column>
            <template #footer>
                <Paginator :first="offset" :rows="size" :totalRecords="totalRecords" @page="onPage($event)"
                    :rowsPerPageOptions="[5,10,25,50,100]"
                    currentPageReportTemplate="Showing {first} - {last} of {totalRecords} tags"
                    template="CurrentPageReport FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink RowsPerPageDropdown" />
            </template>
        </DataTable>

        <Dialog v-model:visible="tagDialog" :style="{width: '450px'}" header="Tag Details" :modal="true" :closable="false"
            class="p-fluid">
            <div class="field">
                <label for="name">Name</label>
                <InputText id="name" v-model.trim="tag.name" required="true" autofocus />
                <small class="p-error" v-if="submitted && !tag.name">Name is required.</small>
            </div>
            <template #footer>
                <Button v-if="isEditing" label="Save" icon="pi pi-check " class="p-button" @click="editTag" />
                <Button v-else-if="!isEditing" label="Save" icon="pi pi-check " class="p-button p-button-success"
                    @click="saveTag" />
                    <Button label="Cancel" icon="pi pi-times" class="p-button p-button-danger" @click="hideDialog" />
            </template>
        </Dialog>

        <Dialog v-model:visible="deleteTagDialog" :style="{width: '450px'}" header="Confirm" :modal="true" :closable="false">
            <div class="confirmation-content">
                <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem; margin-right: 0.5em;" />
                <span v-if="tag">Are you sure you want to delete <b>{{tag.name}}</b>?</span>
            </div>
            <template #footer>
                <Button label="Yes" icon="pi pi-check" class="p-button p-button-success" @click="deleteTagById" />
                <Button label="No" icon="pi pi-times" class="p-button p-button-danger" @click="deleteTagDialog = false" />
            </template>
        </Dialog>
    </div>
</template>

<script lang="ts">
import { onMounted, ref, watch } from 'vue';
import { defineComponent } from 'vue';
import Tag from '@/types/Tag';
import TagService from '../services/TagService';
import { FilterMatchMode } from 'primevue/api';

export default defineComponent({
    setup() {
        const tagDialog = ref(false);
        const datatableKey = ref(0)
        const offset = ref(0)
        const currentPage = ref(0)
        const size = ref(5)
        const pageParam = ref('page=' + currentPage.value)
        const sizeParam = ref('size=' + size.value)
        const sortParam = ref('')
        const filterNameParam = ref('')
        const params = ref<string>(pageParam.value + '&' + sizeParam.value + '&' + sortParam.value + '&' + filterNameParam.value)
        const tagService = ref(new TagService());
        const { tags, totalRecords, error, loadGetTags } = tagService.value.getTags()
        const isEditing = ref(false);
        const deleteTagDialog = ref(false)
        const submitted = ref(false)
        const renderComponent = ref(true)
        const tag = ref<Tag>({ id: 0, name: ''})

        onMounted(() => {
            loadGetTags(params.value);
        })
        
        watch(tags, (taaag) => {
            console.log(taaag)
        })
        const filters1 = ref({
            'name': { value: null, matchMode: FilterMatchMode.CONTAINS }
        });

        watch(size, (s) => {
            sizeParam.value = 'size=' + s
        })
        watch(currentPage, (cp) => {
            pageParam.value = 'page=' + cp
        })
        watch([sizeParam, pageParam, sortParam, filterNameParam], (p) => {
            params.value = p.join('&')
            loadGetTags(params.value)
        })

        
        const openNew = () => {
            tagDialog.value = true;
        };

        const hideDialog = () => {
            tagDialog.value = false;
            tag.value = { id: 0, name: ''};
        };

        const saveTag = () => {
            const { addedTag, loadAddTag } = tagService.value.addTag();
            loadAddTag(tag.value);
            watch(addedTag, () => {
                loadGetTags(params.value)
            })
            tag.value = { id: 0, name: ''};
            tagDialog.value = false;
        }
        const openEdit = (t: Tag) => {
            isEditing.value = true;
            tagDialog.value = true;
            tag.value = { ...t };
        };

        const editTag = () => {
            const { editedTag, loadEditTag } = tagService.value.editTag();
            loadEditTag(tag.value.id, tag.value);
            watch(editedTag, () => {
                loadGetTags(params.value)
            })
            tag.value = { id: 0, name: ''};
            tagDialog.value = false;
            isEditing.value = false;
        }

        const confirmDeleteTag = (c: number) => {
            tag.value.id = c;
            deleteTagDialog.value = true;
        };

        const deleteTagById = () => {
            const { resp, loadDeleteTag } = tagService.value.deleteTag();
            loadDeleteTag(tag.value.id)
            watch(resp, () => {
                loadGetTags(params.value);
            })
            deleteTagDialog.value = false;
            tag.value = { id: 0, name: ''};
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

        const onFilter = (prop: 'name', input: string) => {
            if (input === null) {
                return;
            }
            currentPage.value = 0
            filterNameParam.value = prop + '=' + input
        }

        const clearFilters = () => {
            currentPage.value = 0
            offset.value = 0
            sortParam.value = ''
            filterNameParam.value = ''
            datatableKey.value++
        }

        return {
            tags, error, currentPage, size, totalRecords, submitted, tag, isEditing, tagDialog,
            openNew, openEdit, hideDialog, saveTag, renderComponent, deleteTagDialog,
            confirmDeleteTag, deleteTagById, editTag, onPage, onSort, offset, filters1, onFilter, clearFilters, datatableKey
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
