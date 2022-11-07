import Project from "@/types/Project";
import { ref } from "vue";
import { useToast } from "primevue/usetoast";
import Page from "@/types/Page";
import ProjectWriteModel from "@/types/ProjectWriteModel";
import authHeader from "./Auth-header";

export default class ProjectService {
    toast = useToast();

    getProjects = () => {
        const page = ref<Page<Project>>();
        const totalRecords = ref(0)
        const projects = ref<Project[]>([])
        const errorGetProjects = ref(null)
        const headers = new Headers()
        headers.append('Authorization', authHeader())
        headers.append("Content-Type", "text/plain")
        const requestOptions = {
            method: "GET",
            headers: headers
          };
        const loadGetProjects = async (params = '') => {
            try{
                const res = await fetch('http://localhost:8080/api/v1/projects?' + params, requestOptions)
                if(!res.ok){
                    throw Error('Failed to fetch Projects')
                }
                page.value = await res.json()
                projects.value = page.value!.content
                totalRecords.value = page.value!.totalElements
            } catch(e: any){
                errorGetProjects.value = e;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            }
        }
        return { page, projects, totalRecords, errorGetProjects, loadGetProjects}
    }

    addProject = () => {
        
        const addedProject = ref<Project>({id: 0, name: '', client:{id:0, name: '', note: ''}, owner:{id:0, name:'', surname: '', email: '',}})
        const errorAddProject = ref(null)
        const loadAddProject = async (projectWM: ProjectWriteModel) => {
            const headers = new Headers()
            headers.append('Authorization', authHeader())
            headers.append("Content-Type", "application/json")
            const requestOptions = {
                method: "POST",
                headers: headers,
                body: JSON.stringify(projectWM)
            };
            try{
                const data = await fetch('http://localhost:8080/api/v1/projects', requestOptions)
                if(!data.ok){
                    throw Error('Adding project failed')
                }
                addedProject.value = await data.json()
                this.toast.add({severity:'success', summary: 'Successful', detail: 'Project Added', life: 3000});
            } catch(e: any){
                errorAddProject.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            }
        }
        return { addedProject, errorAddProject, loadAddProject }
    }

    editProject = () => {
        const editedProject = ref<Project>({id: 0, name: '', client:{id:0, name: '', note: ''}, owner:{id:0, name:'', surname: '', email: '',}})
        const errorEditProject = ref(null)
        const loadEditProject = async (projectId: number, project: ProjectWriteModel) => {
            const headers = new Headers()
            headers.append('Authorization', authHeader())
            headers.append("Content-Type", "application/json")
            const requestOptions = {
                method: "PUT",
                headers: headers,
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
                errorEditProject.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            }
        }
        return { editedProject, errorEditProject, loadEditProject }
    }

    deleteProject = () => {
        
        const resp = ref(null)
        const errorDeleteProject = ref(null)
        const loadDeleteProject = async (projectId: number) => {
            const headers = new Headers()
            headers.append('Authorization', authHeader())
            const requestOptions = {
                method: "DELETE",
                headers: headers
              };
            try{
                const data = await fetch('http://localhost:8080/api/v1/projects/' + projectId, requestOptions)
                if(!data.ok){
                    throw Error('Delete project failed')
                }
                resp.value = await data.json();
                this.toast.add({severity:'success', summary: 'Successful', detail: 'Project Deleted', life: 3000});
            } catch(e: any){
                errorDeleteProject.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            }
        }
        return {resp, errorDeleteProject, loadDeleteProject }
    }
}