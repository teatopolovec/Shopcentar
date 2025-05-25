import { useEffect, useState } from 'react';
import { Link } from "react-router-dom";
import "./Etaze.css";


function Etaze() {
  const [etaze, setEtaze] = useState([]);

  useEffect(() => {
    fetch('/api/prostor/etaze')
      .then(res => {
        if (!res.ok) {
          throw new Error('Neuspješan dohvat etaza');
        }
        return res.json();
      })
      .then(data => {
        const sortirano = data.sort((a,b) =>
          a.brojEtaze - b.brojEtaze);
        setEtaze(sortirano);
      })
      .catch(err => alert('Greška pri dohvatu:', err));
  }, []);


  return (
    <div>
      <h2>Popis etaža</h2>
      <Link to="/etaza/novo" className="dodaj-button">+ dodaj etažu</Link>
      <table>
        <thead>
          <tr>
            <th>Broj etaže</th>
            <th>Opis</th>
          </tr>
        </thead>
        <tbody>
        {etaze.map((etaza) => (
          <tr key={etaza.idEtaze}>
            <td>
              <Link to={`/etaza/${etaza.idEtaze}`}>
                {etaza.brojEtaze}
              </Link>
            </td>
            <td>
              {etaza.opis}
            </td>
          </tr>
        ))}
        </tbody>
      </table>
    </div>
  );
}

export default Etaze;