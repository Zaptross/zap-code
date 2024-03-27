import {QueryClient, useQuery} from "@tanstack/react-query";
import {UserProfile} from "../types/UserProfile";

export default function useFetchUserProfile(client: QueryClient) {
  const {isPending, data, error} = useQuery<UserProfile>(
    {
      retry: false,
      queryKey: ["userProfile"],
      queryFn: async () => {
        // TODO - use env for the URL
        const response = await fetch("http://localhost:8080/user", {
          redirect: "manual",
          credentials: "include",
        });
        const data = await response.json();
        return data;
      },
    },
    client
  );

  return {isPending, data, error};
}
