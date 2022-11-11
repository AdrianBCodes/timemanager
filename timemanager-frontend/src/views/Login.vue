<template>
    <Card class="card">
        <template #title>
            <h2>Login</h2>
        </template>
        <template #content>
            <label for="username">Username</label>
            <InputText @keydown.enter="login" id="username" v-model="username" type="text"></InputText>
            <label for="password">Password</label>
            <InputText @keydown.enter="login" id="password" v-model="password" type="password"></InputText>
        </template>
        <template #footer>
        <Button @click="login" label="Login" class="p-button p-button-lg p-button-secondary" />
    </template>
    </Card>
</template>
  
  <script lang="ts">
import { computed, watch } from '@vue/runtime-core'
  import { useStore } from 'vuex'
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useToast } from 'primevue/usetoast'

    export default {
        setup () {
        const toast = useToast()
        const router = useRouter()
        const store = useStore()
        const isLoggedIn = computed(() => store.state.auth.status.loggedIn)
        const username = ref<string>("")
        const password = ref<string>("")
        const login = () => {
            store.dispatch('login', {username: username.value, password: password.value}).catch(() => {
                toast.add({severity:'error', summary: 'Error', detail:'Login failure', life: 3000});
            })
            username.value = ''
            password.value = ''
        }
        watch(isLoggedIn, (s) => {
            if(s === true){
                router.push({name: 'Tracker'})
            }
        })

        return { username, password, login }
        }
    }
  </script>
  
  <style scoped>
    .card {
    margin: auto;
    position: absolute;
    top: 30%;
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