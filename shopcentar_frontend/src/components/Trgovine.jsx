import React, { useEffect, useState } from 'react';
import {Link} from "react-router-dom"
import "./Trgovine.css";
function Trgovine (){
    const [trgovine, setTrgovine] = useState([]);
    useEffect(() => {
        fetch('/api/trgovina/popis') 
        .then(res => {
            if (!res.ok) {
                throw new Error('Neuspješan dohvat trgovina');
            }
            return res.json();
        })
        .then(data => setTrgovine(data))
        .catch(err => console.error('Greška pri dohvatu:', err));
    }, []);

  return (
    <div>
      <h2>Popis trgovina</h2>
      <ul className="trgovine-container ">
        {trgovine.map((trgovina) => (
          <li key={trgovina.idTrgovine} className="trgovina-item">
            <Link to={`/trgovina/${trgovina.idTrgovine}`}>
              {trgovina.nazivTrgovine}
            </Link>
          </li>
        ))}
      </ul>
    </div>
  );
}
export default Trgovine;