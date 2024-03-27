import {QueryClient, QueryClientProvider} from "@tanstack/react-query";
import Header from "./views/Header/Header";
import TaskView from "./views/TaskView/TaskView";
import {UserProfileContext} from "./providers/UserProfileContext/UserProfileContext";
import useFetchUserProfile from "./hooks/fetchUserProfile";
import {useState} from "react";
import {UserProfile} from "./types/UserProfile";

const client = new QueryClient();

function App() {
  const [userProfile, setUserProfile] = useState<UserProfile | undefined>(
    undefined
  );
  const {isPending, data, error} = useFetchUserProfile(client);

  if (data && !userProfile) {
    setUserProfile(data);
  }

  return (
    <>
      <UserProfileContext.Provider
        value={{userProfile, setUserProfile, isPending, error}}
      >
        <QueryClientProvider client={client}>
          <Header />
          <TaskView />
        </QueryClientProvider>
      </UserProfileContext.Provider>
    </>
  );
}

export default App;
