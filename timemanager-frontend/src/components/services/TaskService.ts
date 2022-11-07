import Task from "@/types/Task";
import { ref } from "vue";
import { useToast } from "primevue/usetoast";
import Page from "@/types/Page";
import authHeader from "./Auth-header";

export default class TaskService {
    toast = useToast();

    getTasks = () => {
        const page = ref<Page<Task>>();
        const totalRecords = ref(0)
        const tasks = ref<Task[]>([])
        const errorGetTasks = ref(null)
        const headers = new Headers()
        headers.append('Authorization', authHeader())
        headers.append("Content-Type", "text/plain")
        const requestOptions = {
            method: "GET",
            headers: headers
          };
        const loadGetTasks = async (params = '') => {
            try{
                const res = await fetch('http://localhost:8080/api/v1/tasks?' + params, requestOptions)
                if(!res.ok){
                    throw Error('Failed to fetch Tasks')
                }
                page.value = await res.json()
                tasks.value = page.value!.content
                totalRecords.value = page.value!.totalElements
            } catch(e: any){
                errorGetTasks.value = e;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            }
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
            const headers = new Headers()
            headers.append('Authorization', authHeader())
            headers.append("Content-Type", "application/json")
            const requestOptions = {
                method: "POST",
                headers: headers,
                body: JSON.stringify(task)
            };
            try{
                const data = await fetch('http://localhost:8080/api/v1/tasks', requestOptions)
                if(!data.ok){
                    throw Error('Adding task failed')
                }
                addedTask.value = await data.json()
                this.toast.add({severity:'success', summary: 'Successful', detail: 'Task Added', life: 3000});
            } catch(e: any){
                errorAddTask.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            }
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
            const headers = new Headers()
            headers.append('Authorization', authHeader())
            headers.append("Content-Type", "application/json")
            const requestOptions = {
                method: "PUT",
                headers: headers,
                body: JSON.stringify(task)
              };
            try{
                const data = await fetch('http://localhost:8080/api/v1/tasks/' + taskId, requestOptions)
                if(!data.ok){
                    throw Error('Editing task failed')
                }
                editedTask.value = await data.json()
                this.toast.add({severity:'success', summary: 'Successful', detail: 'Task Edited', life: 3000});
            } catch(e: any){
                errorEditTask.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            }
        }
        return { editedTask, errorEditTask, loadEditTask }
    }

    deleteTask = () => {
        
        const resp = ref(null)
        const errorDeleteTask = ref(null)
        const loadDeleteTask = async (taskId: number) => {
            const headers = new Headers()
            headers.append('Authorization', authHeader())
            const requestOptions = {
                method: "DELETE",
                headers: headers
              };
            try{
                const data = await fetch('http://localhost:8080/api/v1/tasks/' + taskId, requestOptions)
                if(!data.ok){
                    throw Error('Delete task failed')
                }
                resp.value = await data.json();
                this.toast.add({severity:'success', summary: 'Successful', detail: 'Task Deleted', life: 3000});
            } catch(e: any){
                errorDeleteTask.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            }
        }
        return {resp, errorDeleteTask, loadDeleteTask }
    }
}