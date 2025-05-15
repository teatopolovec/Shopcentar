import { useParams } from 'react-router-dom';
import { useEffect, useState } from 'react';

function TrgovinaDetalji() {
  const {id} = useParams();
  const [trgovina, setTrgovina] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetch(`/api/trgovina/${id}`)
      .then(res => {
        if (!res.ok) throw new Error('Neuspješan dohvat podataka');
        return res.json();
      })
      .then(data => {
        setTrgovina(data);
      })
      .catch(err => {
        setError(err.message);
      });
  }, [id]);

  if (error) return <p>Greška: {error}</p>;
  if (!trgovina) return <p>Trgovina nije pronađena</p>;

  return (
    <div>
      <h2>{trgovina.nazivTrgovine}</h2>
      <p>Radno vrijeme: {trgovina.radnoVrijeme}</p>
      <p>Email: {trgovina.emailTrgovine}</p>
      <p>Telefon: {trgovina.telefonTrgovine}</p>
      <p>Posljednje ažuriranje: {new Date(trgovina.posljednjeAžuriranje).toLocaleString()}</p>
    </div>
  );
}

export default TrgovinaDetalji;
