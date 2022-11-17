<template>
    <div class="dateManagementPanel">
    <Button label="Previous" @click="goToPreviousWeek()"></Button>
    <div>{{firstDay.toLocaleDateString('en-GB')}} - {{lastDay.toLocaleDateString('en-GB')}}</div>
    <Button label="Next" @click="goToNextWeek()"></Button>
    </div>
    <div class="weekPanel">
        <div :class="{highlight:date == selectedDate}" class ="dayOfWeek" v-for="date in dateTable" :key="dateTable.indexOf(date)" @click="selectedDate = date">
            {{date.toLocaleDateString('en-GB')}}
        </div>
    </div>
    
    <div class="table">
        <DataTable :value="trackerEvents" responsiveLayout="scroll" showGridlines stripedRows
            :scrollable="true" scrollHeight="flex">

            <Column field="date" header="Date" style="max-width:13rem"></Column>
            <Column field="project.name" header="Project"></Column>
            <Column field="task.name" header="Task"></Column>
            <Column field="description" header="Description"></Column>
            <Column field="duration" header="Duration" style="max-width:8rem">
                <template #body="slotProps">
                    <span> {{ durationMinutesToString(slotProps.data.duration) }}</span>
                </template>
            </Column>
            <Column :exportable="false" style="max-width:12rem ">
                <template #body="slotProps">
                    <!-- <Button icon="pi pi-pencil" class="p-button-rounded p-button-success" /> -->
                    <Button icon="pi pi-trash" class="p-button-rounded p-button-danger" @click="deleteTrackerEventById(slotProps.data.id)" />
                </template>
            </Column>
        <template #empty>
                None
        </template>
        </DataTable>
    </div>
    
    <Toolbar style="margin-top: 0.5rem; padding: 1rem; overflow: auto; margin-right: 0.7vw;">
            <template #start>
                <Calendar inputId="icon" v-model="selectedDate" :showIcon="true" disabled style="max-width:11rem; margin-right: 1.2rem" dateFormat="dd/mm/yy"/>
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
                            :disabled="selectedProject === null || selectedProject === undefined">
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
                <Button label="Add" :disabled="isNotReadyToAdd()" icon="pi pi-plus" class="p-button p-button-primary" @click="addTrackerEvent" />
            </template>
        </Toolbar>
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

        const addDaysToDate = (date: Date, days: number) => {
            const result = new Date(date)
            result.setDate(date.getDate() + days)
            return result;
        }

        const getFirstDayOfCurrentWeek = () => {
            const result = new Date()
            var day = result.getDay() || 7;  
            if( day !== 1 ) {
                result.setHours(-24 * (day - 1)); 
            }
            return result;

        }

        const store = useStore()
        const trackerEventService = new TrackerEventService()
        const taskService = new TaskService()
        const {trackerEvents, loadGetTrackerEvents} = trackerEventService.getTrackerEvents()
        const {projectsToTrack, loadGetProjectsToTrack} = trackerEventService.getProjectsToTrack()
        const {tasks, loadGetTasksByProjectId} = taskService.getTasksByProjectId()
        const description = ref<string>('')
        const duration = ref<string>('')
        const selectedProject = ref<Project | null>()
        const selectedTask = ref<Task | null>()
        const firstDay = ref(getFirstDayOfCurrentWeek())
        const lastDay = ref(addDaysToDate(firstDay.value, 6))
        const dateTable = ref<Date[]>([]);
        const selectedDate = ref()

        const currentUserId = () => {
            return store.getters.getUserId;
        }

        watch(dateTable, () => {
            selectedDate.value = dateTable.value[0]
        })

        onMounted(() => {
            loadDateTable()
            loadGetProjectsToTrack()
        })

        watch(selectedDate, () => {
            loadGetTrackerEvents(selectedDate.value)
        })

        watch(firstDay, () => {
            loadDateTable();
            lastDay.value = addDaysToDate(firstDay.value, 6);
        })

        watch(selectedProject, () => {
            if(selectedProject.value !== null){
                loadGetTasksByProjectId(selectedProject.value!.id)
            }
        })

        const loadDateTable = () => {
            dateTable.value = []
            for(let i=0; i < 7; i++){
                dateTable.value.push(addDaysToDate(firstDay.value, i));
            }
        }

        const addTrackerEvent = () => {
            const { addedTrackerEvent, errorAddTrackerEvent, loadAddTrackerEvent } = trackerEventService.addTrackerEvent();
            const dateToAdd = ref(new Date(Date.UTC(selectedDate.value.getFullYear(),selectedDate.value.getMonth(),selectedDate.value.getDate())));
            const trackerEventToAdd = ref<TrackerEventWriteModel>({
                description: description.value,
                projectId: selectedProject.value!.id,
                taskId: selectedTask.value?.id,
                duration: durationStringToMinutes(duration.value),
                date: dateToAdd.value,
                userId: currentUserId()
            })
            
            description.value = ''
            selectedProject.value = null
            selectedTask.value = null
            duration.value = ''
            loadAddTrackerEvent(trackerEventToAdd.value);

            watch(addedTrackerEvent, () => {
                loadGetTrackerEvents(selectedDate.value)
            })
        }

        const durationStringToMinutes = (duration = '') =>{
            const splittedDuration = duration.split(':');
            const hours = Number(splittedDuration[0])
            const minutes = Number(splittedDuration[1])
            return 60 * hours + minutes;
        }

        const durationMinutesToString = (minutes: number) => {
            const hours = Math.floor(minutes / 60)
            const min = minutes - hours * 60
            if(hours < 10) {
                return '0' + hours + ':' + min;
            }
            return hours + ':' + min;
        }

        const goToNextWeek = () => {
            firstDay.value = addDaysToDate(firstDay.value, 7);
        }

        const goToPreviousWeek = () => {
            firstDay.value = addDaysToDate(firstDay.value, -7);
        }

        const isNotReadyToAdd = () => {
            return description.value.length === 0 ||
            selectedProject.value == null ||
            duration.value.length === 0
        }

        const deleteTrackerEventById = (id: number) => {
            const { resp, loadDeleteTrackerEvent } = trackerEventService.deleteTrackerEvent();
            loadDeleteTrackerEvent(id)
            watch(resp, () => {
                loadGetTrackerEvents(selectedDate.value)
            })
        };


        return {
            trackerEvents, selectedDate, projectsToTrack, selectedProject, tasks, selectedTask, description, duration,
            addTrackerEvent, durationMinutesToString, dateTable, firstDay, lastDay, goToNextWeek, goToPreviousWeek, isNotReadyToAdd, deleteTrackerEventById
        }
    },
})
</script>

<style scoped>
.dateManagementPanel {
    align-items: center;
    margin-bottom: 1rem;
    display: flex;
    justify-content: space-between;
    margin-right: 0.7vw;
    font-size: large;
    padding: 1rem;
}

div.highlight {
    background-color: var(--sidebar-item-active);
}
.dayOfWeek {
    flex: 1;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: large;
}

.dayOfWeek:hover {
    background-color: var(--sidebar-item-hover);
    cursor: pointer;
}
.weekPanel {
    height: 5rem;
    margin-bottom: 0.5rem;
    margin-right: 0.7vw;
    display: flex;
    background: #2a323d;
    border: 1px solid #3f4b5b;
    border-radius: 4px;
}
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
