import Task from "@/types/Task";
import { ref } from "vue";
import { useToast } from "primevue/usetoast";
import Page from "@/types/Page";
import axios from "axios";
import authHeader from "./Auth-header";
import { baseURL } from "@/http-commons";

export default class TaskService {
    toast = useToast();

    getTasks = () => {
        const page = ref<Page<Task>>();
        const totalRecords = ref(0)
        const tasks = ref<Task[]>([])
        const errorGetTasks = ref(null)
        const loadGetTasks = async (params = '') => {
            await axios.get(baseURL + '/tasks?' + params, {
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json",
                    "Authorization": authHeader()
                  }
            }).then(response => {
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

    getTasksByProjectId = () => {
        const tasks = ref<Task[]>([])
        const errorGetTasksByProjectId = ref(null)
        const loadGetTasksByProjectId = async (projectId: number) => {
            await axios.get(baseURL + '/tasks/project/' + projectId, {
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json",
                    "Authorization": authHeader()
                  }
            }).then(response => {
                tasks.value = response.data
            }).catch(e => {
                errorGetTasksByProjectId.value = e;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            })
        }
        return {tasks, errorGetTasksByProjectId, loadGetTasksByProjectId}
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
            await axios.post(baseURL + '/tasks', task, {
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json",
                    "Authorization": authHeader()
                  }
            }).then(response => {
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
            await axios.put(baseURL + '/tasks/' + taskId, task, {
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json",
                    "Authorization": authHeader()
                  }
            }).then(response => {
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
            await axios.delete(baseURL + '/tasks/' + taskId, {
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json",
                    "Authorization": authHeader()
                  }
            }).then(response => {
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