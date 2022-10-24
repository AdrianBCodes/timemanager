import Tag from "@/types/Tag";
import { ref } from "vue";
import { useToast } from "primevue/usetoast";
import Page from "@/types/Page";

export default class TagService {
    toast = useToast();

    getTags = () => {
        const page = ref<Page<Tag>>();
        const totalRecords = ref(0)
        const tags = ref<Tag[]>([])
        const error = ref(null)
        const requestOptions = {
            method: "GET",
            headers: { "Content-Type": "text/plain" }
          };
        const load = async (params?: string) => {
            try{
                const res = await fetch('http://localhost:8080/api/v1/tags?' + params, requestOptions)
                if(!res.ok){
                    throw Error('Failed to fetch Tags')
                }
                page.value = await res.json()
                tags.value = page.value!.content
                totalRecords.value = page.value!.totalElements
            } catch(e: any){
                error.value = e;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            }
        }
        return { page, tags, totalRecords, error, load}
    }

    addTag = () => {
        const addedTag = ref<Tag>({id: 0, name: ''})
        const errorAdd = ref(null)
        const loadAddTag = async (tag: Tag) => {
            const requestOptions = {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(tag)
            };
            try{
                const data = await fetch('http://localhost:8080/api/v1/tags', requestOptions)
                if(!data.ok){
                    throw Error('Adding tag failed')
                }
                addedTag.value = await data.json()
                this.toast.add({severity:'success', summary: 'Successful', detail: 'Tag Added', life: 3000});
            } catch(e: any){
                errorAdd.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            }
        }
        return { addedTag, errorAdd, loadAddTag }
    }

    editTag = () => {
        const editedTag = ref<Tag>({id: 0, name: ''})
        const errorEdit = ref(null)
        const loadEditTag = async (tagId: number, tag: Tag) => {
            const requestOptions = {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(tag)
              };
            try{
                const data = await fetch('http://localhost:8080/api/v1/tags/' + tagId, requestOptions)
                if(!data.ok){
                    throw Error('Editing tag failed')
                }
                editedTag.value = await data.json()
                this.toast.add({severity:'success', summary: 'Successful', detail: 'Tag Edited', life: 3000});
            } catch(e: any){
                errorEdit.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            }
        }
        return { editedTag, errorEdit, loadEditTag }
    }

    deleteTag = () => {
        
        const resp = ref(null)
        const errorDelete = ref(null)
        const loadDeleteTag = async (tagId: number) => {
            const requestOptions = {
                method: "DELETE",
                headers: { "Content-Type": "application/json" }
              };
            try{
                const data = await fetch('http://localhost:8080/api/v1/tags/' + tagId, requestOptions)
                if(!data.ok){
                    throw Error('Delete tag failed')
                }
                resp.value = await data.json();
                this.toast.add({severity:'success', summary: 'Successful', detail: 'Tag Deleted', life: 3000});
            } catch(e: any){
                errorDelete.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            }
        }
        return {resp, errorDelete, loadDeleteTag }
    }
}