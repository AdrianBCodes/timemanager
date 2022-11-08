import axios, { AxiosInstance } from "axios";
import router from "./router";
import authHeader from "./components/services/Auth-header";

const apiClient: AxiosInstance = axios.create({
  baseURL: "http://localhost:8080/api/v1",
  headers: {
    "Accept": "application/json",
    "Content-Type": "application/json",
    "Authorization": authHeader()
  },
});

apiClient.interceptors.response.use(function (response) {
  return response
}, function (error) {
  if (error.response.status === 403) {
    router.push({name: 'Denied'})
  }
  return Promise.reject(error)
})

export default apiClient;