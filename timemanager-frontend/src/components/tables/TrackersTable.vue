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

        <Toolbar style="margin-top: 0.5rem; padding: 1rem; overflow: auto;">
            <template #start>
                <Calendar inputId="icon" v-model="selectedDate" :showIcon="true" style="max-width:11rem; margin-right: 1.2rem" dateFormat="dd/mm/yy"/>
                <Dropdown v-model="selectedProject" :options="projectsToTrack" optionLabel="name" :filter="true" placeholder="Select Project" :showClear="true" style="width: 24rem; margin-right: 0.8rem">
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
                <Dropdown v-model="selectedTask" :options="tasks" optionLabel="name" :filter="true" 
                            placeholder="Select Task" :showClear="true" style="width: 24rem; margin-right: 0.8rem" 
                            :disabled="selectedProject === null">
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
                <InputMask v-model="duration" mask="99:99" placeholder="00:00" style="max-width:6rem; margin-right: 1.5rem " />
                <Button label="Add" icon="pi pi-plus" class="p-button p-button-primary" @click="addTrackerEvent" />
            </template>
        </Toolbar>
    </div>
</template>

<script lang="ts">
import Project from '@/types/Project';
import Task from '@/types/Task';
import TrackerEventWriteModel from '@/types/TrackerEventWriteModel';
import { defineComponent, onMounted, ref, watch } from 'vue';
import { useStore } from 'vuex';
import TaskService from '../services/TaskService';
import TrackerEventService from '../services/TrackerEventService';


export default defineComponent({
    setup() {
        const store = useStore()
        const trackerEventService = new TrackerEventService()
        const taskService = new TaskService()
        const {trackerEvents, loadGetTrackerEvents} = trackerEventService.getTrackerEvents()
        const {projectsToTrack, loadGetProjectsToTrack} = trackerEventService.getProjectsToTrack()
        const {tasks, loadGetTasksByProjectId} = taskService.getTasksByProjectId()
        const description = ref<string>('')
        const duration = ref<string>('')
        const selectedDate = ref()
        const selectedProject = ref<Project | null>()
        const selectedTask = ref<Task | null>()
        const currentUserId = () => {
            return store.getters.getUserId;
        }

        onMounted(() => {
            loadGetTrackerEvents()
            loadGetProjectsToTrack()
        })

        watch(selectedProject, () => {
            if(selectedProject.value !== null){
                loadGetTasksByProjectId(selectedProject.value!.id)
            }
        })

        watch(description, () => {
            console.log(description.value)
        })

        const addTrackerEvent = () => {
            const { addedTrackerEvent, errorAddTrackerEvent, loadAddTrackerEvent } = trackerEventService.addTrackerEvent();
            const trackerEventToAdd = ref<TrackerEventWriteModel>({
                description: description.value,
                projectId: selectedProject.value!.id,
                taskId: selectedTask.value!.id,
                duration: durationStringToMinutes(duration.value),
                date: selectedDate.value,
                userId: currentUserId()
            })
            loadAddTrackerEvent(trackerEventToAdd.value);

            watch(addedTrackerEvent, () => {
                loadGetTrackerEvents();
            })

            description.value = ''
            selectedProject.value = null
            selectedTask.value = null
            duration.value = ''
            selectedDate.value = null
        }

        const durationStringToMinutes = (duration = '') =>{
            const splittedDuration = duration.split(':');
            const hours = Number(splittedDuration[0])
            const minutes = Number(splittedDuration[1])
            return 60 * hours + minutes;
        }

        return {
            trackerEvents, selectedDate, projectsToTrack, selectedProject, tasks, selectedTask, description, duration, addTrackerEvent
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
