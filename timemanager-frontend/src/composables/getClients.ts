import Client from "@/types/Client";
import { ref } from "vue";
import { useToast } from "primevue/usetoast";
import Page from "@/types/Page";


const getClients = (params?: string) => {
    const page = ref<Page<Client>>();
    const toast = useToast();
    const clients = ref<Client[]>([])
    const error = ref(null)
    const requestOptions = {
        method: "GET",
        headers: { "Content-Type": "application/json" }
      };
    const load = async () => {
        try{
            const res = await fetch('http://localhost:8080/api/v1/clients?' + params, requestOptions)
            if(!res.ok){
                throw Error('Failed to fetch Clients')
            }
            page.value = await res.json()
            clients.value = page.value!.content
        } catch(e: any){
            error.value = e;
            toast.add({severity:'warn', summary: 'Warn Message', detail:e.message, life: 3000});
        }
    }
    return { clients, error, load}
}
export default getClients;