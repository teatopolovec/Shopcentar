import { useEffect, useState } from 'react';
import { Link } from "react-router-dom";
import "./Trgovine.css";

function Trgovine() {
  const [trgovine, setTrgovine] = useState([]);
  const [trazenaRijec, setTrazenaRijec] = useState("");

  useEffect(() => {
    fetch('/api/trgovina/popis')
      .then(res => {
        if (!res.ok) {
          throw new Error('Neuspješan dohvat trgovina');
        }
        return res.json();
      })
      .then(data => {
        const sortirano = data.sort((a, b) =>
          a.nazivTrgovine.localeCompare(b.nazivTrgovine, 'hr', { sensitivity: 'base' })
        );
        setTrgovine(sortirano);
      })
      .catch(err => alert('Greška pri dohvatu:', err));
  }, []);

  const filtriraneTrgovine = trgovine.filter(trgovina =>
    trgovina.nazivTrgovine.toLowerCase().includes(trazenaRijec.toLowerCase())
  );

  return (
    <div>
      <h2>Popis trgovina</h2>
      <Link to="/trgovina/novo" className="dodaj-button">+ dodaj trgovinu</Link>

      <div style={{margin: "0 auto", maxWidth: "700px"}}>
        <input
          type="text"
          placeholder="Pretraži trgovine..."
          value={trazenaRijec}
          onChange={(e) => setTrazenaRijec(e.target.value)}
          className="trazilica"
        />
      </div>

      <ul className="trgovine-container">
        {filtriraneTrgovine.map((trgovina) => (
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
