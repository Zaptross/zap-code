export type Logs = {
  id: string;
  userId: string;
  taskId: string;
  logs: string[];
  tests: string[];
};

export type LogsSearch = {
  id: string;
  taskId: string;
};
