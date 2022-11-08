import Tag from "@/types/Tag";
import { ref } from "vue";
import { useToast } from "primevue/usetoast";
import Page from "@/types/Page";
import authHeader from "./Auth-header";
import apiClient from "@/http-commons";

export default class TagService {
    toast = useToast();

    getTags = () => {
        const page = ref<Page<Tag>>();
        const totalRecords = ref(0)
        const tags = ref<Tag[]>([])
        const error = ref(null)
        const load = async (params = '') => {
            await apiClient.get('/tags?' + params).then(response => {
                page.value = response.data
                tags.value = page.value!.content
                totalRecords.value = page.value!.totalElements
            }).catch((e) => {
                error.value = e;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            })
        }
        return { page, tags, totalRecords, error, load}
    }

    addTag = () => {
        const addedTag = ref<Tag>({id: 0, name: ''})
        const errorAdd = ref(null)
        const loadAddTag = async (tag: Tag) => {
            await apiClient.post('/tags', tag).then(response => {
                addedTag.value = response.data
                this.toast.add({severity:'success', summary: 'Successful', detail: 'Tag Added', life: 3000});
            }).catch((e) => {
                errorAdd.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            })
        }
        return { addedTag, errorAdd, loadAddTag }
    }

    editTag = () => {
        const editedTag = ref<Tag>({id: 0, name: ''})
        const errorEdit = ref(null)
        const loadEditTag = async (tagId: number, tag: Tag) => {
            await apiClient.put('/tags/' + tagId, tag).then(response => {
                editedTag.value = response.data
                this.toast.add({severity:'success', summary: 'Successful', detail: 'Tag Edited', life: 3000});
            }).catch((e) => {
                errorEdit.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            })
        }
        return { editedTag, errorEdit, loadEditTag }
    }

    deleteTag = () => {
        const resp = ref(null)
        const errorDelete = ref(null)
        const loadDeleteTag = async (tagId: number) => {
            await apiClient.delete('/tags/' + tagId).then(response => {
                resp.value = response.data
                this.toast.add({severity:'success', summary: 'Successful', detail: 'Tag Deleted', life: 3000});
            }).catch((e) => {
                errorDelete.value = e.message;
                this.toast.add({severity:'error', summary: 'Error', detail:e.message, life: 3000});
            })
        }
        return {resp, errorDelete, loadDeleteTag }
    }
}