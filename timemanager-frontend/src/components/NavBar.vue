<template>
    <Toolbar class="p-toolbar-changes">
        <template #end>
            <div v-if="!isLoggedIn()">
            <Button label="Login" class="p-button" @click="goToLogin"></Button>
            <Button label="Register" class="p-button" @click="goToRegister"></Button>
            </div>
            <div v-else>
            <Button label="Profile" class="p-button" @click="goToProfile"></Button>
            <Button label="Logout" class="p-button" @click="logout"></Button>
            </div>
        </template>
    </Toolbar>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import Toolbar from 'primevue/toolbar';
import { useRouter } from 'vue-router';
import { useStore } from 'vuex';

export default defineComponent({
    components:{
        Toolbar
    },
    setup() {
        const store = useStore()
        const router = useRouter()
        const goToLogin = () => {
            router.push({name: 'Login'})
        }
        const goToRegister = () => {
            router.push({name: 'Register'})
        }

        const goToProfile = () => {
            router.push({name: 'Profile'})
        }

        const logout = () => {
            store.dispatch('logout')
            router.push({name: 'Login'})
        }

        const isLoggedIn = () => {
            return store.getters.getLoggedIn
        }

        // console.log(isLoggedIn)

        return {
            goToLogin, goToRegister, goToProfile, logout, isLoggedIn
        }
    }
})
</script>

<style scoped>
.p-toolbar-changes {
    border-right: none;
    border-left: none;
    border-top: none;
    border-radius: 0%;
    height: 50px;
    padding: 0px;
    padding-right: 10px;
}
.p-button {
    margin-left: 5px;
}
</style>