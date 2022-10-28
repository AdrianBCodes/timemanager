import User from "@/types/User";
import { ref } from "vue";
import { useToast } from "primevue/usetoast";
import Page from "@/types/Page";

export default class UserService {
    toast = useToast();

    getUsers = () => {
        const page = ref<Page<User>>();
        const totalRecords = ref(0)
        const users = ref<User[]>([])
        const errorGetUsers = ref(null)
        const requestOptions = {
            method: "GET",
            headers: { "Content-Type": "text/plain" }
          };
        const loadGetUsers = async (params = '') => {
            try{
                const res = await fetch('http://localhost:8080/api/v1/users?' + params, requestOptions)
                if(!res.ok){
                    throw Error('Failed to fetch Users')
                }
                page.value = await res.json()
                users.value = page.value!.content
                totalRecords.value = page.value!.totalElements
            } catch(e: any){
                errorGetUsers.value = e;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            }
        }
        return { page, users, totalRecords, errorGetUsers, loadGetUsers}
    }

    addUser = () => {
        const addedUser = ref<User>({id: 0, name: '', surname: '', email: ''})
        const errorAddUser = ref(null)
        const loadAddUser = async (user: User) => {
            const requestOptions = {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(user)
            };
            try{
                const data = await fetch('http://localhost:8080/api/v1/users', requestOptions)
                if(!data.ok){
                    throw Error('Adding user failed')
                }
                addedUser.value = await data.json()
                this.toast.add({severity:'success', summary: 'Successful', detail: 'User Added', life: 3000});
            } catch(e: any){
                errorAddUser.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            }
        }
        return { addedUser, errorAddUser, loadAddUser }
    }

    editUser = () => {
        const editedUser = ref<User>({id: 0, name: '', surname: '', email: ''})
        const errorEditUser = ref(null)
        const loadEditUser = async (userId: number, user: User) => {
            const requestOptions = {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(user)
              };
            try{
                const data = await fetch('http://localhost:8080/api/v1/users/' + userId, requestOptions)
                if(!data.ok){
                    throw Error('Editing user failed')
                }
                editedUser.value = await data.json()
                this.toast.add({severity:'success', summary: 'Successful', detail: 'User Edited', life: 3000});
            } catch(e: any){
                errorEditUser.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            }
        }
        return { editedUser, errorEditUser, loadEditUser }
    }

    deleteUser = () => {
        
        const resp = ref(null)
        const errorDeleteUser = ref(null)
        const loadDeleteUser = async (userId: number) => {
            const requestOptions = {
                method: "DELETE",
                headers: { "Content-Type": "application/json" }
              };
            try{
                const data = await fetch('http://localhost:8080/api/v1/users/' + userId, requestOptions)
                if(!data.ok){
                    throw Error('Delete user failed')
                }
                resp.value = await data.json();
                this.toast.add({severity:'success', summary: 'Successful', detail: 'User Deleted', life: 3000});
            } catch(e: any){
                errorDeleteUser.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            }
        }
        return {resp, errorDeleteUser, loadDeleteUser }
    }
}