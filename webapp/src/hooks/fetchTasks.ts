import {useQuery} from "@tanstack/react-query";
import type {Task} from "../types/Task";

export default function useFetchTasks() {
  const {isPending, data, error} = useQuery<Task[]>({
    retry: false,
    queryKey: ["tasks"],
    queryFn: async () => {
      // TODO - use env for the URL
      const response = await fetch("http://localhost:8080/tasks", {
        redirect: "manual",
        credentials: "include",
      });
      const data = await response.json();
      return data;
    },
  });

  return {isPending, data, error};
}
