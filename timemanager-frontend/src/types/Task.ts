import Tag from "./Tag";

type Task = {
    id: number;
    name: string;
    description: string;
    projectId?: number;
    tags: Tag[];
}

export default Task;