import User from "@/types/User";
import { ref } from "vue";
import { useToast } from "primevue/usetoast";
import Page from "@/types/Page";
import axios from "axios";
import authHeader from "./Auth-header";
import { baseURL } from "@/http-commons";

export default class UserService {
    toast = useToast();

    getUsers = () => {
        const page = ref<Page<User>>();
        const totalRecords = ref(0)
        const users = ref<User[]>([])
        const errorGetUsers = ref(null)
        const loadGetUsers = async (params = '') => {
            await axios.get(baseURL + '/users?' + params, {
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json",
                    "Authorization": authHeader()
                  }
            }).then(response => {
                page.value = response.data
                users.value = page.value!.content
                totalRecords.value = page.value!.totalElements
            }).catch((e) => {
                errorGetUsers.value = e;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            })
        }
        return { page, users, totalRecords, errorGetUsers, loadGetUsers}
    }

    addUser = () => {
        const addedUser = ref<User>({id: 0, name: '', surname: '', email: ''})
        const errorAddUser = ref(null)
        const loadAddUser = async (user: User) => {
            await axios.post(baseURL + '/users', user, {
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json",
                    "Authorization": authHeader()
                  }
            }).then(response => {
                addedUser.value = response.data
                this.toast.add({severity:'success', summary: 'Successful', detail: 'User Added', life: 3000});
            }).catch((e) => {
                errorAddUser.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            })
        }
        return { addedUser, errorAddUser, loadAddUser }
    }

    editUser = () => {
        const editedUser = ref<User>({id: 0, name: '', surname: '', email: ''})
        const errorEditUser = ref(null)
        const loadEditUser = async (userId: number, user: User) => {
            await axios.put(baseURL + '/users/' + userId, user, {
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json",
                    "Authorization": authHeader()
                  }
            }).then(response => {
                editedUser.value = response.data
                this.toast.add({severity:'success', summary: 'Successful', detail: 'User Edited', life: 3000});
            }).catch((e) => {
                errorEditUser.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            })
        }
        return { editedUser, errorEditUser, loadEditUser }
    }

    deleteUser = () => {
        const resp = ref(null)
        const errorDeleteUser = ref(null)
        const loadDeleteUser = async (userId: number) => {
            await axios.delete(baseURL + '/users/' + userId, {
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json",
                    "Authorization": authHeader()
                  }
            }).then(response => {
                resp.value = response.data
                this.toast.add({severity:'success', summary: 'Successful', detail: 'User Deleted', life: 3000});
            }).catch((e) => {
                errorDeleteUser.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            })
        }
        return {resp, errorDeleteUser, loadDeleteUser }
    }
}