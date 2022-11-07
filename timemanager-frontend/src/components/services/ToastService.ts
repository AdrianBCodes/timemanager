import { useToast } from "primevue/usetoast";

export const useToastService = () => {
  const toast = useToast();

  const showErrorToast = (detail: string) => {
    toast.add({ severity: 'error', summary: 'Error', detail: detail, life: 3000 });
  }

  return { showErrorToast };
};