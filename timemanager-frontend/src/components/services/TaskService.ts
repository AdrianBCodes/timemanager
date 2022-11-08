import Task from "@/types/Task";
import { ref } from "vue";
import { useToast } from "primevue/usetoast";
import Page from "@/types/Page";
import authHeader from "./Auth-header";
import apiClient from "@/http-commons";

export default class TaskService {
    toast = useToast();

    getTasks = () => {
        const page = ref<Page<Task>>();
        const totalRecords = ref(0)
        const tasks = ref<Task[]>([])
        const errorGetTasks = ref(null)
        const loadGetTasks = async (params = '') => {
            await apiClient.get('/tasks?' + params).then(response => {
                page.value = response.data
                tasks.value = page.value!.content
                totalRecords.value = page.value!.totalElements
            }).catch((e) => {
                errorGetTasks.value = e;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            })
        }
        return { page, tasks, totalRecords, errorGetTasks, loadGetTasks}
    }

    addTask = () => {
        const addedTask = ref<Task>({
            id: 0,
            name: '',
            description: '',
            projectId: 0,
            tags: []
        })
        const errorAddTask = ref(null)
        const loadAddTask = async (task: Task) => {
            await apiClient.post('/tasks', task).then(response => {
                addedTask.value = response.data
                this.toast.add({severity:'success', summary: 'Successful', detail: 'Task Added', life: 3000});
            }).catch((e) => {
                errorAddTask.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            })
        }
        return { addedTask, errorAddTask, loadAddTask }
    }

    editTask = () => {
        const editedTask = ref<Task>({
            id: 0,
            name: '',
            description: '',
            projectId: 0,
            tags: []
        })
        const errorEditTask = ref(null)
        const loadEditTask = async (taskId: number, task: Task) => {
            await apiClient.put('/tasks/' + taskId, task).then(response => {
                editedTask.value = response.data
                this.toast.add({severity:'success', summary: 'Successful', detail: 'Task Edited', life: 3000});
            }).catch((e) => {
                errorEditTask.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            })
        }
        return { editedTask, errorEditTask, loadEditTask }
    }

    deleteTask = () => {
        const resp = ref(null)
        const errorDeleteTask = ref(null)
        const loadDeleteTask = async (taskId: number) => {
            await apiClient.put('/tasks/' + taskId).then(response => {
                resp.value = response.data
                this.toast.add({severity:'success', summary: 'Successful', detail: 'Task Deleted', life: 3000});
            }).catch((e) => {
                errorDeleteTask.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            })
        }
        return {resp, errorDeleteTask, loadDeleteTask }
    }
}