import { ref } from "vue";

const deleteClient = () => {
    const errorDelete = ref(null)
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
        } catch(e: any){
            errorDelete.value = e.message;
            console.log('error while deleting client')
        }
    }
    return { errorDelete, loadDeleteClient }
}
export default deleteClient;