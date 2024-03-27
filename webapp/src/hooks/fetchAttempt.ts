import {useQuery} from "@tanstack/react-query";
import {Attempt} from "../types/Attempt";

export default function useFetchAttempt(attemptId?: string) {
  const {isPending, data, error, refetch} = useQuery<Attempt>({
    enabled: !!attemptId, // only fetch logs if attemptId is provided
    retry: 3,
    retryDelay: 1000,
    queryKey: ["attempts", attemptId],
    queryFn: async () => {
      // TODO - use env for the URL
      const response = await fetch(
        `http://localhost:8080/attempts/${attemptId}`,
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
