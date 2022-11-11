import Project from "./Project";
import Task from "./Task";

type TrackerEvent = {
    id: number;
    description: string;
    project: Project;
    task: Task;
    duration: number;
    date: Date;
    userId: number;
}

export default TrackerEvent;