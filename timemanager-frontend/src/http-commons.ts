import axios from "axios";
import router from "./router";

export const baseURL = "http://localhost:8080/api/v1";

axios.interceptors.response.use(function (response) {
  return response
}, function (error) {
  if (error.response.status === 403) {
    router.push({name: 'Denied'})
  }
  if (error.response.status === 401) {
    router.push({name: 'Login'})
  }
  return Promise.reject(error)
})