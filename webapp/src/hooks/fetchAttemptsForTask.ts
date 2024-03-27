import {useQuery} from "@tanstack/react-query";
import {Attempt} from "../types/Attempt";

export default function useFetchAttemptsForTask(taskId?: string) {
  const {isPending, data, error, refetch} = useQuery<Attempt[]>({
    enabled: !!taskId, // only fetch attempts if taskId is provided
    retry: false,
    queryKey: ["attempts", "search", taskId],
    queryFn: async () => {
      // TODO - use env for the URL
      const response = await fetch(
        `http://localhost:8080/attempts?taskId=${taskId}`,
        {
          redirect: "manual",
          credentials: "include",
        }
      );
      const data = await response.json();
      return data;
    },
  });

  return {isPending, data, error, refetch};
}
