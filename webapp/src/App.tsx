import UserProfileDisplay from "./components/UserProfile/Display";
import {QueryClient, QueryClientProvider} from "@tanstack/react-query";
import Header from "./views/Header/Header";
import {Row} from "./components/common/Row";

const client = new QueryClient();

function App() {
  return (
    <>
      <QueryClientProvider client={client}>
        <Header />
        <Row hAlign="flex-end">
          <UserProfileDisplay />
        </Row>
        {/* // TODO - use env for the URL */}
        <a href="http://localhost:8080/auth/logout">Logout</a>
      </QueryClientProvider>
    </>
  );
}

export default App;
