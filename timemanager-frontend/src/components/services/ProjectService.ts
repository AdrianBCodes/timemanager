import Project from "@/types/Project";
import { ref } from "vue";
import { useToast } from "primevue/usetoast";
import Page from "@/types/Page";

export default class ProjectService {
    toast = useToast();

    getProjects = () => {
        const page = ref<Page<Project>>();
        const totalRecords = ref(0)
        const projects = ref<Project[]>([])
        const error = ref(null)
        const requestOptions = {
            method: "GET",
            headers: { "Content-Type": "text/plain" }
          };
        const load = async (params?: string) => {
            try{
                const res = await fetch('http://localhost:8080/api/v1/projects?' + params, requestOptions)
                if(!res.ok){
                    throw Error('Failed to fetch Projects')
                }
                page.value = await res.json()
                projects.value = page.value!.content
                totalRecords.value = page.value!.totalElements
            } catch(e: any){
                error.value = e;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            }
        }
        return { page, projects, totalRecords, error, load}
    }

    addProject = () => {
        const addedProject = ref<Project>({id: 0, name: '', client:{id:0, name: '', note: ''}, owner:{id:0, name:'', surname: '', email: '',}})
        const errorAdd = ref(null)
        const loadAddProject = async (project: Project) => {
            const requestOptions = {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(project)
            };
            try{
                const data = await fetch('http://localhost:8080/api/v1/projects', requestOptions)
                if(!data.ok){
                    throw Error('Adding project failed')
                }
                addedProject.value = await data.json()
                this.toast.add({severity:'success', summary: 'Successful', detail: 'Project Added', life: 3000});
            } catch(e: any){
                errorAdd.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            }
        }
        return { addedProject, errorAdd, loadAddProject }
    }

    editProject = () => {
        const editedProject = ref<Project>({id: 0, name: '', client:{id:0, name: '', note: ''}, owner:{id:0, name:'', surname: '', email: '',}})
        const errorEdit = ref(null)
        const loadEditProject = async (projectId: number, project: Project) => {
            const requestOptions = {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(project)
              };
            try{
                const data = await fetch('http://localhost:8080/api/v1/projects/' + projectId, requestOptions)
                if(!data.ok){
                    throw Error('Editing project failed')
                }
                editedProject.value = await data.json()
                this.toast.add({severity:'success', summary: 'Successful', detail: 'Project Edited', life: 3000});
            } catch(e: any){
                errorEdit.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            }
        }
        return { editedProject, errorEdit, loadEditProject }
    }

    deleteProject = () => {
        
        const resp = ref(null)
        const errorDelete = ref(null)
        const loadDeleteProject = async (projectId: number) => {
            const requestOptions = {
                method: "DELETE",
                headers: { "Content-Type": "application/json" }
              };
            try{
                const data = await fetch('http://localhost:8080/api/v1/projects/' + projectId, requestOptions)
                if(!data.ok){
                    throw Error('Delete project failed')
                }
                resp.value = await data.json();
                this.toast.add({severity:'success', summary: 'Successful', detail: 'Project Deleted', life: 3000});
            } catch(e: any){
                errorDelete.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            }
        }
        return {resp, errorDelete, loadDeleteProject }
    }
}