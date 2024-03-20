import {QueryClient, QueryClientProvider} from "@tanstack/react-query";
import Header from "./views/Header/Header";
import TaskView from "./views/TaskView/TaskView";

const client = new QueryClient();

function App() {
  return (
    <>
      <QueryClientProvider client={client}>
        <Header />
        <TaskView />
      </QueryClientProvider>
    </>
  );
}

export default App;
