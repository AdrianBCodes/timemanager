import AuthService from '@/components/services/AuthService';
import { useToastService } from '@/components/services/ToastService';
import { useToast } from 'primevue/usetoast';
import { Commit } from 'vuex'

const storedUser = localStorage.getItem('user');
let user = null;
if(storedUser !== null){
  user = JSON.parse(storedUser);
}
const initialState = user
  ? { status: { loggedIn: true }, user }
  : { status: { loggedIn: false }, user: null };



export const auth = {
  // namespaced: true,
  state: initialState,
  getters: {
    getLoggedIn(state: any){
      return state.status.loggedIn
    }
  },
  actions: {
    login({ commit }: { commit: Commit }, user: any) {
      return AuthService.login(user).then(
        user => {
          commit('loginSuccess', user);
          return Promise.resolve(user);
        },
        error => {
          commit('loginFailure');
          return Promise.reject(error);
        }
      );
    },
    logout({ commit }: {commit : Commit }) {
      AuthService.logout();
      commit('logout');
    },
    register({ commit }: { commit: Commit }, user: any) {
      return AuthService.register(user).then(
        response => {
          commit('registerSuccess');
          return Promise.resolve(response);
        },
        error => {
          commit('registerFailure');
          return Promise.reject(error);
        }
      );
    }
  },
  mutations: {
    loginSuccess(state : any, user: any) {
      state.status.loggedIn = true;
      state.user = user;
    },
    loginFailure(state: any) {
      state.status.loggedIn = false;
      state.user = null;
    },
    logout(state: any) {
      state.status.loggedIn = false;
      state.user = null;
    },
    registerSuccess(state: any) {
      state.status.loggedIn = false;
    },
    registerFailure(state: any) {
      state.status.loggedIn = false;
    }
  }
};