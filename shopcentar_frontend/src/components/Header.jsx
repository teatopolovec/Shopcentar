
import "./Header.css";
import {useNavigate} from "react-router-dom";

function Header() {
  const navigate = useNavigate();
  return (
    <>
      <header className="Header">
        <span className="teme" onClick={(e) => { e.preventDefault(); navigate("/trgovine")}}>Trgovine</span>
        <span className="teme" onClick={(e) => { e.preventDefault(); navigate("/etaze")}}>Etaže</span>
      </header>
    </>
  )
}

export default Header;




