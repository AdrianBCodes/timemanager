import TrackerEvent from "@/types/TrackerEvent";
import { ref } from "vue";
import { useToast } from "primevue/usetoast";
import Page from "@/types/Page";
import authHeader from "./Auth-header";
import axios from "axios";
import { baseURL } from "@/http-commons";
import TrackerEventWriteModel from "@/types/TrackerEventWriteModel";
import Project from "@/types/Project";

export default class TrackerEventService {
    toast = useToast();

    getTrackerEvents = () => {
        const trackerEvents = ref<TrackerEvent[]>([])
        const errorGetTrackerEvents = ref()
        const loadGetTrackerEvents =async (date: Date) => {
            await axios.get(baseURL + '/trackerEvents/current',  {
                params:{
                    dateString: date.toLocaleDateString('en-GB') + ' 00:00'
                },
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json",
                    "Authorization": authHeader()
                  }
                }).then( response => {
                    trackerEvents.value = response.data
                }).catch((e) => {
                    errorGetTrackerEvents.value = e.message
                    this.toast.add({severity:'error', summary: 'Error', detail: 'Error fetching Tracker Events', life: 3000});
                })
        }
        return {trackerEvents, errorGetTrackerEvents, loadGetTrackerEvents}
    }

    getTrackerEventsPaged = () => {
        const page = ref<Page<TrackerEvent>>();
        const totalRecords = ref(0)
        const trackerEventsPaged = ref<TrackerEvent[]>([])
        const errorGetTrackerEventsPaged = ref()
        const loadGetTrackerEventsPaged = async (params = '') => {
            await axios.get(baseURL + '/trackerEvents/paged?' + params, {
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json",
                    "Authorization": authHeader()
                  }
            }).then(response => {
                page.value = response.data;
                trackerEventsPaged.value = page.value!.content;
                totalRecords.value = page.value!.totalElements;
            }).catch((e) => {
                errorGetTrackerEventsPaged.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail: 'Error fetching Tracker Events', life: 3000});
            })
        }
        return { page, trackerEventsPaged, totalRecords, errorGetTrackerEventsPaged, loadGetTrackerEventsPaged}
    }

    getProjectsToTrack = () => {
        const projectsToTrack = ref<Project[]>([])
        const errorGetProjectsToTrack = ref()
        const loadGetProjectsToTrack = async () => {
            await axios.get(baseURL + '/trackerEvents/projects', {
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json",
                    "Authorization": authHeader()
                  }
            }).then(response => {
                projectsToTrack.value = response.data;
            }).catch((e) => {
                errorGetProjectsToTrack.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail: 'Error fetching Tracker Events', life: 3000});
            })
        }
        return {projectsToTrack, errorGetProjectsToTrack, loadGetProjectsToTrack}
    }

    addTrackerEvent = () => {
        const addedTrackerEvent = ref<TrackerEvent>()
        const errorAddTrackerEvent = ref(null)
        const loadAddTrackerEvent = async (trackerEventWM?: TrackerEventWriteModel) => {
            await axios.post(baseURL + '/trackerEvents', trackerEventWM, {
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json",
                    "Authorization": authHeader()
                  }
                }).then(response => {
                addedTrackerEvent.value = response.data
                this.toast.add({severity:'success', summary: 'Successful', detail: 'Tracker Event Added', life: 3000});
            }).catch(e => {
                errorAddTrackerEvent.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail: e.message, life: 3000});
            })
        }
        return { addedTrackerEvent, errorAddTrackerEvent, loadAddTrackerEvent }
    }

    editTrackerEvent = () => {
        const editedTrackerEvent = ref<TrackerEvent>()
        const errorEditTrackerEvent = ref(null)
        const loadEditTrackerEvent = async (trackerId: number, trackerEventWM: TrackerEventWriteModel) => {
            await axios.put(baseURL + '/trackerEvents/' + trackerId, trackerEventWM, {
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json",
                    "Authorization": authHeader()
                  }
            }).then(response => {
                editedTrackerEvent.value = response.data
                this.toast.add({severity:'success', summary: 'Successful', detail: 'Tracker Event Edited', life: 3000});
            }).catch(e => {
                errorEditTrackerEvent.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            })
        }
        return { editedTrackerEvent, errorEditTrackerEvent, loadEditTrackerEvent }
    }

    deleteTrackerEvent = () => {
        
        const resp = ref(null)
        const errorDeleteTrackerEvent = ref(null)
        const loadDeleteTrackerEvent = async (trackerId: number) => {
            await axios.delete(baseURL + '/trackerEvents/' + trackerId, {
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json",
                    "Authorization": authHeader()
                  }
            }).then(response => {
                resp.value = response.data
                this.toast.add({severity:'success', summary: 'Successful', detail: 'Tracker Event Deleted', life: 3000});
            }).catch(e => {
                errorDeleteTrackerEvent.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            })
        }
        return {resp, errorDeleteTrackerEvent, loadDeleteTrackerEvent }
    }

    getTrackerEventsExport = () => {
        const resp = ref(null)
        const errorGetTrackerEventsExport = ref()
        const loadGetTrackerEventsExport =async (params = '') => {

            axios({
                url: baseURL + '/trackerEvents/reports/export?' + params,
                method: 'GET',
                responseType: 'blob',
            }).then((response) => {
                 const fileURL = window.URL.createObjectURL(new Blob([response.data]));
                 const fileLink = document.createElement('a');
    
                 fileLink.href = fileURL;
                 fileLink.setAttribute('download', 'reports.xlsx');
                 document.body.appendChild(fileLink);
    
                 fileLink.click();
            }).catch(e => {
                errorGetTrackerEventsExport.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            });

        }
        return {resp, errorGetTrackerEventsExport, loadGetTrackerEventsExport}
    }
}