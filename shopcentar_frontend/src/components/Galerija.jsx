import React, { useState, useEffect } from 'react';
import "./Galerija.css";

function SlikeTrgovine({ idTrgovine, refreshKey, noviId }) {
  const [slike, setSlike] = useState([]);

  useEffect(() => {
    if (!(idTrgovine === "novo" && !noviId)){
      fetch(`/api/sliketrgovine/${noviId}`)
        .then(res => res.json())
        .then(data => setSlike(data))
        .catch(err => console.error("Greška pri dohvatu slika:", err));
    }
  }, [idTrgovine, refreshKey]);

  const obrisiSliku = (naziv) => {
    fetch(`/api/sliketrgovine/${noviId}/${encodeURIComponent(naziv)}`, {
      method: 'DELETE'
    })
    .then(res => {
      if (!res.ok) throw new Error("Brisanje nije uspjelo");
      setSlike(prev => prev.filter(s => s !== naziv));
    })
    .catch(err => alert("Greška: " + err.message));
  };

  return (
    <div className="slike-container">
      {slike.length === 0 ? (
        <p className="bez-slika-poruka">Nema fotografija za ovu trgovinu.</p>
      ) : (
        slike.map(naziv => (
          <div key={naziv} className="slika-wrapper">
            <img src={`http://localhost:8080/slike/${noviId}/${naziv}`} alt={naziv} />
            <button type="button" className="delete-button" onClick={() => obrisiSliku(naziv)}>×</button>
          </div>
        ))
      )}
    </div>
  );

}

export default SlikeTrgovine;
