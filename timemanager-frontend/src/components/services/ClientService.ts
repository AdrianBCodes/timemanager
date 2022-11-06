import Client from "@/types/Client";
import { ref } from "vue";
import { useToast } from "primevue/usetoast";
import Page from "@/types/Page";
import authHeader from "./Auth-header";

export default class ClientService {
    toast = useToast();

    getClients = () => {
        const page = ref<Page<Client>>();
        const totalRecords = ref(0)
        const clients = ref<Client[]>([])
        const errorGetClients = ref()
        const headers = new Headers()
        console.log(authHeader())
        headers.append('Authorization', authHeader())
        headers.append("Content-Type", "application/json")
        const requestOptions = {
            method: "GET",
            headers: headers
          };
        const loadGetClients = async (params = '') => {
            try{
                const res = await fetch('http://localhost:8080/api/v1/clients?' + params, requestOptions)
                if(!res.ok){
                    throw Error('Failed to fetch Clients')
                }
                page.value = await res.json()
                clients.value = page.value!.content
                totalRecords.value = page.value!.totalElements
            } catch(e: any){
                errorGetClients.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            }
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
            const requestOptions = {
                method: "POST",
                headers: { "Content-Type": "application/json"},
                body: JSON.stringify(client)
            };
            try{
                const data = await fetch('http://localhost:8080/api/v1/clients', requestOptions)
                if(!data.ok){
                    throw Error('Adding client failed')
                }
                addedClient.value = await data.json()
                this.toast.add({severity:'success', summary: 'Successful', detail: 'Client Added', life: 3000});
            } catch(e: any){
                errorAddClient.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            }
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
            const requestOptions = {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(client)
              };
            try{
                const data = await fetch('http://localhost:8080/api/v1/clients/' + clientId, requestOptions)
                if(!data.ok){
                    throw Error('Editing client failed')
                }
                editedClient.value = await data.json()
                this.toast.add({severity:'success', summary: 'Successful', detail: 'Client Edited', life: 3000});
            } catch(e: any){
                errorEditClient.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            }
        }
        return { editedClient, errorEditClient, loadEditClient }
    }

    deleteClient = () => {
        
        const resp = ref(null)
        const errorDeleteClient = ref(null)
        const loadDeleteClient = async (clientId: number) => {
            const requestOptions = {
                method: "DELETE",
                headers: { "Content-Type": "application/json" }
              };
            try{
                const data = await fetch('http://localhost:8080/api/v1/clients/' + clientId, requestOptions)
                if(!data.ok){
                    throw Error('Delete client failed')
                }
                resp.value = await data.json();
                this.toast.add({severity:'success', summary: 'Successful', detail: 'Client Deleted', life: 3000});
            } catch(e: any){
                errorDeleteClient.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            }
        }
        return {resp, errorDeleteClient, loadDeleteClient }
    }
}