import {useQuery} from "@tanstack/react-query";
import {Logs} from "../types/Logs";

export default function useFetchAttemptLogs(attemptId?: string) {
  const {isPending, data, error, refetch} = useQuery<Logs>({
    enabled: !!attemptId, // only fetch logs if attemptId is provided
    retry: 3,
    retryDelay: 1000,
    queryKey: ["logs", "attempt", attemptId],
    queryFn: async () => {
      // TODO - use env for the URL
      const response = await fetch(`http://localhost:8080/logs/${attemptId}`, {
        redirect: "manual",
        credentials: "include",
      });
      const data = await response.json();
      return data;
    },
  });

  return {isPending, data, error, refetch};
}
