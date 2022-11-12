<template>
    <div class="table">
        <DataTable :value="trackerEvents" responsiveLayout="scroll" showGridlines stripedRows
            :scrollable="true" scrollHeight="flex">

            <Column field="date" header="Date" style="max-width:13rem"></Column>
            <Column field="project.name" header="Project"></Column>
            <Column field="task.name" header="Task"></Column>
            <Column field="description" header="Description"></Column>
            <Column field="duration" header="Duration" style="max-width:8rem"></Column>
            <Column :exportable="false" style="max-width:12rem ">
                <!-- <template #body="slotProps"> -->
                <template>
                    <Button icon="pi pi-pencil" class="p-button-rounded p-button-success" />
                    <Button icon="pi pi-trash" class="p-button-rounded p-button-danger" />
                </template>
            </Column>
        <template #empty>
                Empty
        </template>
        </DataTable>

        <Toolbar style="margin-top: 0.5rem; padding: 1rem">
            <template #start>
                <Calendar inputId="icon" v-model="selectedDate" :showIcon="true" style="max-width:11rem; margin-right: 1.2rem" />
                <Dropdown v-model="selectedProject" :options="projects" optionLabel="name" :filter="true" placeholder="Select Project" :showClear="true" style="width: 24rem; margin-right: 0.8rem">
                    <template #value="slotProps" >
                        <div v-if="slotProps.value" style="padding-right: 12rem;" >
                            <div>{{slotProps.value.name}}</div>
                        </div>
                        <span v-else style="padding-right: 12rem;">
                            {{slotProps.placeholder}}
                        </span>
                    </template>
                    <template #option="slotProps">
                        <div>
                            <div>{{slotProps.option.name}}</div>
                        </div>
                    </template>
                </Dropdown>
                <Dropdown v-model="selectedTasks" :options="tasks" optionLabel="name" :filter="true" placeholder="Select Task" :showClear="true" style="width: 24rem; margin-right: 0.8rem">
                    <template #value="slotProps" >
                        <div v-if="slotProps.value" style="padding-right: 13rem;" >
                            <div>{{slotProps.value.name}}</div>
                        </div>
                        <span v-else style="padding-right: 13rem;">
                            {{slotProps.placeholder}}
                        </span>
                    </template>
                    <template #option="slotProps">
                        <div>
                            <div>{{slotProps.option.name}}</div>
                        </div>
                    </template>
                </Dropdown>
                <InputText type="text" v-model="description" style="width: 24rem; margin-right: 0.8rem"/>
                <InputMask v-model="duration" mask="99:99" placeholder="00:00" style="max-width:6rem; margin-right: 0.8rem " />
            </template>
        </Toolbar>
    </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue';
import TrackerEventService from '../services/TrackerEventService';


export default defineComponent({
    setup() {
        const trackerEventService = new TrackerEventService
        const {trackerEvents, loadGetTrackerEvents} = trackerEventService.getTrackerEvents()
        const projects = ref()
        const tasks = ref()
        const description = ref()
        const duration = ref()
        const selectedDate = ref()
        const selectedProject = ref()
        const selectedTasks = ref()

        onMounted(() => {
            loadGetTrackerEvents()
        })
        

        return {
            trackerEvents, selectedDate, projects, selectedProject, tasks, selectedTasks, description, duration
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
