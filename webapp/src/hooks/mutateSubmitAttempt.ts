import {useMutation} from "@tanstack/react-query";
import {Attempt, SubmitAttemptResponse} from "../types/Attempt";

export default function useMutateSubmitAttempt() {
  const {isPending, data, error, mutate} = useMutation<
    SubmitAttemptResponse,
    Error,
    Attempt
  >({
    mutationFn: async (attempt: Attempt) => {
      // TODO - use env for the URL
      const response = await fetch("http://localhost:8080/attempts", {
        method: "POST",
        redirect: "manual",
        credentials: "include",
        body: JSON.stringify(attempt),
      });
      const data = await response.json();
      return data;
    },
  });

  return {isPending, data, error, mutate};
}
