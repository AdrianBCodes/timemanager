import Client from "@/types/Client";
import { ref } from "vue";

const addClient = () => {
    const addedClient = ref<Client>({
        id: 0,
        name: '',
        note: ''
    })
    const errorAdd = ref(null)
    const loadAddClient = async (client: Client) => {
        const requestOptions = {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(client)
          };
        try{
            const data = await fetch('http://localhost:8080/api/v1/clients', requestOptions)
            if(!data.ok){
                throw Error('Adding client failed')
            }
            addedClient.value = await data.json()
        } catch(e: any){
            errorAdd.value = e.message;
            console.log('error while adding client')
        }
    }
    return { addedClient, errorAdd, loadAddClient }
}
export default addClient;