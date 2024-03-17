import {useQuery} from "@tanstack/react-query";

type UserProfile = {
  id: string;
  username: string;
  avatarUrl: string;
};

export default function useFetchUserProfile() {
  return () => {
    const {isPending, data, error} = useQuery<UserProfile>({
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
    });

    return {isPending, data, error};
  };
}
