import Header from "./components/Header";
import Trgovine from "./components/Trgovine";
import Trgovina from "./components/Trgovina";
import Etaze from "./components/Etaze";
import Etaza from "./components/Etaza";
import {createBrowserRouter, Outlet, RouterProvider} from "react-router-dom";
import "./App.css";

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
        },
        {
          path: "/etaze",
          element: <Etaze/>,
          children: []
        },
        {
          path: "/etaza/:id",
          element: <Etaza/>,
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
