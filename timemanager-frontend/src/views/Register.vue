<template>
    <Card class="card">
        <template #title>
            <h2>Register</h2>
        </template>
        <template #content>
            <label for="email">Email</label>
            <InputText @keydown.enter="register" id="email" v-model="email" type="text"></InputText>
            <label for="username">Username</label>
            <InputText @keydown.enter="register" id="username" v-model="username" type="text"></InputText>
            <label for="password">Password</label>
            <InputText @keydown.enter="register" id="password" v-model="password" type="password"></InputText>
        </template>
        <template #footer>
            <Button @click="register" label="Register" class="p-button p-button-lg p-button-secondary" />
        </template>
    </Card>
</template>
  
  <script lang="ts">
  import { useStore } from 'vuex'
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useToast } from 'primevue/usetoast'

    export default {
        setup () {
        const toast = useToast()
        const router = useRouter()
        const store = useStore()
        const email = ref<string>("")
        const username = ref<string>("")
        const password = ref<string>("")
        const register = () => {
            store.dispatch('register', {username: username.value, password: password.value, email: email.value})
                .then(() => {
                    toast.add({severity:'info', summary: 'Success', detail:'Registration was successfull', life: 3000})
                    router.push({name: 'Login'})
                })
                .catch(() => toast.add({severity:'error', summary: 'Error', detail:'Registration failure', life: 3000}))
            username.value = ''
            password.value = ''
            email.value = ''
            
        }

        return { username, password, email, register }
        }
    }
  </script>
  
  <style scoped>
    .card {
    margin: auto;
    position: absolute;
    top: 35%;
    left: 50%;
    -ms-transform: translate(-50%, -50%);
    transform: translate(-50%, -50%);
    width: 30%;
    padding: 3px;
    border: 3px solid var(--surface-hover);
    }
    .flex {
    display: block;
    }

    input[type=text], input[type=password] {
    width: 100%;
    padding: 12px 20px;
    margin: 8px 0;
    display: inline-block;
    border: 1px solid var(--surface-hover);
    box-sizing: border-box;
    }
    label {
        font-size: 24px;
    }
  </style>