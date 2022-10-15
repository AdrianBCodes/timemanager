import Client from "@/types/Client";
import { ref } from "vue";
import apiClient from "@/http-commons";

const getClients = () => {
    const clients = ref<Client[]>([]);
    const error = ref(null)
    const load = async () => {
        try{
            const res = await apiClient.get('/clients', {timeout: 1000})
            clients.value = res.data
        } catch(e: any){
            error.value = e.message;
            console.log('error in clients fetch')
        }
    }
    return { clients, error, load}
}
export default getClients;