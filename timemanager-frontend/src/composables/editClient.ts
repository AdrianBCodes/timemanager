import Client from "@/types/Client";
import { ref } from "vue";

const editClient = () => {
    const editedClient = ref<Client>({
        id: 0,
        name: '',
        note: ''
    })
    const errorEdit = ref(null)
    const loadEditClient = async (clientId: number, client: Client) => {
        const requestOptions = {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(client)
          };
        try{
            const data = await fetch('http://localhost:8080/api/v1/clients/' + clientId, requestOptions)
            if(!data.ok){
                throw Error('Editing client failed')
            }
            editedClient.value = await data.json()
        } catch(e: any){
            errorEdit.value = e.message;
            console.log('error while editing client')
        }
    }
    return { editedClient, errorEdit, loadEditClient }
}
export default editClient;