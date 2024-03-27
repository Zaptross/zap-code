export type Attempt = {
  id?: string;
  userId?: string;
  taskId: string;
  solution: string;
};

export type SubmitAttemptResponse = {
  attemptId: string;
};
