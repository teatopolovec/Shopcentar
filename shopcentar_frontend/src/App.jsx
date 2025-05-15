import Header from "./components/Header";
import Trgovine from "./components/Trgovine"
import Trgovina from "./components/Trgovina"
import {createBrowserRouter, Outlet, RouterProvider} from "react-router-dom";
import "./App.css"

function App() {


  const router = createBrowserRouter([
    {
      path: "/",
      element: <AppContainer/>,
      children: [
        {
          path: "/trgovine",
          element: <Trgovine/>,
          children: []
        },
        {
          path: "/trgovina/:id",
          element: <Trgovina/>,
          children: []
        }
      ]
    }
  ]);
  return (
    <RouterProvider router={router}/>
  )
}

export default App

function AppContainer(props) {
  return (
    <div>
      <Header/>
      <div className="App">
        <Outlet/>
      </div>
    </div>
  )
}
