import Client from "@/types/Client";
import { ref } from "vue";
import { useToast } from "primevue/usetoast";
import Page from "@/types/Page";
import authHeader from "./Auth-header";
import apiClient from "@/http-commons";

export default class ClientService {
    toast = useToast();

    getClients = () => {
        const page = ref<Page<Client>>();
        const totalRecords = ref(0)
        const clients = ref<Client[]>([])
        const errorGetClients = ref()
        const loadGetClients = async (params = '') => {
            await apiClient.get('/clients?' + params).then(response => {
                page.value = response.data;
                clients.value = page.value!.content;
                totalRecords.value = page.value!.totalElements;
            }).catch((e) => {
                errorGetClients.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail: 'Error fetching clients', life: 3000});
            })
        }
        return { page, clients, totalRecords, errorGetClients, loadGetClients}
    }

    addClient = () => {
        const addedClient = ref<Client>({
            id: 0,
            name: '',
            note: ''
        })
        const errorAddClient = ref(null)
        const loadAddClient = async (client: Client) => {
            await apiClient.post('/clients', client).then(response => {
                addedClient.value = response.data
                this.toast.add({severity:'success', summary: 'Successful', detail: 'Client Added', life: 3000});
            }).catch(e => {
                errorAddClient.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail: e.message, life: 3000});
            })
        }
        return { addedClient, errorAddClient, loadAddClient }
    }

    editClient = () => {
        const editedClient = ref<Client>({
            id: 0,
            name: '',
            note: ''
        })
        const errorEditClient = ref(null)
        const loadEditClient = async (clientId: number, client: Client) => {
            apiClient.put('/clients/' + clientId, client).then(response => {
                editedClient.value = response.data
                this.toast.add({severity:'success', summary: 'Successful', detail: 'Client Edited', life: 3000});
            }).catch(e => {
                errorEditClient.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            })
        }
        return { editedClient, errorEditClient, loadEditClient }
    }

    deleteClient = () => {
        
        const resp = ref(null)
        const errorDeleteClient = ref(null)
        const loadDeleteClient = async (clientId: number) => {
            await apiClient.delete('/clients/' + clientId).then(response => {
                resp.value = response.data
                this.toast.add({severity:'success', summary: 'Successful', detail: 'Client Deleted', life: 3000});
            }).catch(e => {
                errorDeleteClient.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            })
        }
        return {resp, errorDeleteClient, loadDeleteClient }
    }
}