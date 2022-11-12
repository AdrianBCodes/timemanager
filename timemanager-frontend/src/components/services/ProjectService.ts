import Project from "@/types/Project";
import { ref } from "vue";
import { useToast } from "primevue/usetoast";
import Page from "@/types/Page";
import ProjectWriteModel from "@/types/ProjectWriteModel";
import axios from "axios";
import authHeader from "./Auth-header";
import { baseURL } from "@/http-commons";

export default class ProjectService {
    toast = useToast();

    getProjects = () => {
        const page = ref<Page<Project>>();
        const totalRecords = ref(0)
        const projects = ref<Project[]>([])
        const errorGetProjects = ref(null)
        const loadGetProjects = async (params = '') => {
            await axios.get(baseURL + '/projects?' + params, {
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json",
                    "Authorization": authHeader()
                  }
            }).then(response => {
                page.value = response.data
                projects.value = page.value!.content
                totalRecords.value = page.value!.totalElements
            }).catch(e => {
                errorGetProjects.value = e;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            })
        }
        return { page, projects, totalRecords, errorGetProjects, loadGetProjects}
    }

    addProject = () => {
        const addedProject = ref<Project>({id: 0, name: '', client:{id:0, name: '', note: ''}, owner:{id:0, name:'', surname: '', email: '',}})
        const errorAddProject = ref(null)
        const loadAddProject = async (projectWM: ProjectWriteModel) => {
            await axios.post(baseURL + '/projects', projectWM, {
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json",
                    "Authorization": authHeader()
                  }
            }).then(response => {
                addedProject.value = response.data
                this.toast.add({severity:'success', summary: 'Successful', detail: 'Project Added', life: 3000});
            }).catch(e => {
                errorAddProject.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            })
        }
        return { addedProject, errorAddProject, loadAddProject }
    }

    editProject = () => {
        const editedProject = ref<Project>({id: 0, name: '', client:{id:0, name: '', note: ''}, owner:{id:0, name:'', surname: '', email: '',}})
        const errorEditProject = ref(null)
        const loadEditProject = async (projectId: number, project: ProjectWriteModel) => {
            await axios.put(baseURL + '/projects/' + projectId, project, {
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json",
                    "Authorization": authHeader()
                  }
            }).then(response => {
                editedProject.value = response.data
                this.toast.add({severity:'success', summary: 'Successful', detail: 'Project Edited', life: 3000});
            }).catch(e => {
                errorEditProject.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            })
        }
        return { editedProject, errorEditProject, loadEditProject }
    }

    deleteProject = () => {
        
        const resp = ref(null)
        const errorDeleteProject = ref(null)
        const loadDeleteProject = async (projectId: number) => {
            await axios.delete(baseURL + '/projects/' + projectId, {
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json",
                    "Authorization": authHeader()
                  }
            }).then(response => {
                resp.value = response.data
                this.toast.add({severity:'success', summary: 'Successful', detail: 'Project Deleted', life: 3000});
            }).catch(e => {
                errorDeleteProject.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            })
        }
        return {resp, errorDeleteProject, loadDeleteProject }
    }

    addParticipant = () => {
        const resp = ref(null)
        const errorAddParticipant = ref(null)
        const loadAddParticipant = async (projectId?: number, userId?: number) => {
            await axios.put(baseURL + '/projects/' + projectId + '/participants/' + userId + '/add', {
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json",
                    "Authorization": authHeader()
                  }
            }).then(response => {
                resp.value = response.data
                this.toast.add({severity:'success', summary: 'Successful', detail: 'Participant added', life: 3000});
            }).catch(e => {
                errorAddParticipant.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            })
        }
        return {resp, errorAddParticipant, loadAddParticipant }
    }

    removeParticipant = () => {
        const resp = ref(null)
        const errorRemoveParticipant = ref(null)
        const loadRemoveParticipant = async (projectId?: number, userId?: number) => {
            await axios.put(baseURL + '/projects/' + projectId + '/participants/' + userId + '/remove', {
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json",
                    "Authorization": authHeader()
                  }
            }).then(response => {
                resp.value = response.data
                this.toast.add({severity:'success', summary: 'Successful', detail: 'Participant removed', life: 3000});
            }).catch(e => {
                errorRemoveParticipant.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            })
        }
        return {resp, errorRemoveParticipant, loadRemoveParticipant }
    }
}