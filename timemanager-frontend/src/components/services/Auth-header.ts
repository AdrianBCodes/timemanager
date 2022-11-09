import store from "@/store";

export default function authHeader() {
  return 'Bearer ' + store.getters.getUserToken
}