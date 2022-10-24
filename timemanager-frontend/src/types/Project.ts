import Client from "./Client";
import User from "./User";

type Project = {
    id: number;
    name: string;
    client: Client;
    owner: User;
}

export default Project;