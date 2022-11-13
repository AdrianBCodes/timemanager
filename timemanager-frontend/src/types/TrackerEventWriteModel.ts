type TrackerEventWriteModel = {
    description: string;
    projectId: number | null;
    taskId: number| null;
    duration: number | null;
    date: Date | null;
    userId: number | null;
}

export default TrackerEventWriteModel;