type TrackerEventWriteModel = {
    id: number;
    description: string;
    projectId: number;
    taskId: number;
    duration: number;
    date: Date;
    userId: number;
}

export default TrackerEventWriteModel;