import {QueryClient, QueryClientProvider} from "@tanstack/react-query";
import Header from "./views/Header/Header";
import QuadWindow from "./components/ResizeableWindows/QuadWindow";
const client = new QueryClient();

function App() {
  return (
    <>
      <QueryClientProvider client={client}>
        <Header />
        <QuadWindow>
          <p>Lorem ipsum dolor sit amet</p>
          <p>Lorem ipsum dolor sit amet</p>
          <p>Lorem ipsum dolor sit amet</p>
          <p>Lorem ipsum dolor sit amet</p>
        </QuadWindow>
      </QueryClientProvider>
    </>
  );
}

export default App;
