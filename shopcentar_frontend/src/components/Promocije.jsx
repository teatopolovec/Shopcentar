import { useEffect, useState } from 'react';
import "./Promocije.css";
import PromocijaForma from './Promocija';

function PromocijeTrgovine({ idTrgovine }) {
  const [promocije, setPromocije] = useState([]);
  const [error, setError] = useState(null);
  const [promocijaZaUredjivanje, setPromocijaZaUredjivanje] = useState(null);
  const [trazenaRijec, setTrazenaRijec] = useState("");
  const [datumOd, setDatumOd] = useState("");
  const [datumDo, setDatumDo] = useState("");

  useEffect(() => {
    if (idTrgovine !== "novo"){
      fetch(`/api/promocija/popis/${idTrgovine}`)
        .then(res => {
          if (!res.ok) throw new Error('Greška pri dohvatu promocija');
          return res.json();
        })
        .then(data => {
          const sortirane = data.sort((a, b) => new Date(b.datumPočetkaProm) - new Date(a.datumPočetkaProm));
          setPromocije(sortirane);
        })
        .catch(err => setError(err.message));
    }
  }, [idTrgovine]);

  const handleDelete = (idPromocije) => {
    if (!window.confirm('Jeste li sigurni da želite obrisati ovu promociju?')) return;
    fetch(`/api/promocija/izbrisi/${idPromocije}`, { method: 'DELETE' })
      .then(async res => {
        if (!res.ok) throw new Error(await res.text());
        setPromocije(promocije.filter(p => p.idPromocije !== idPromocije));
      })
      .catch(err => alert(err.message));
  };

  const otvoriFormuZaUredjivanje = (promo) => {
    if (!promo) {
      promo = {
        idPromocije: null,
        naslovPromocije: "",
        tekstPromocije: "",
        slikaPromocije: "",
        datumPočetkaProm: "",
        datumKrajaProm: "",
        idTrgovine: idTrgovine,
      };
    }
    console.log(promo)
    setPromocijaZaUredjivanje(promo);
  };

  const zatvoriFormu = () => {
    setPromocijaZaUredjivanje(null);
  };

  const spremiPromociju = (azuriranaPromocija) => {
    setPromocije(prevPromocije => {
      const postoji = prevPromocije.some(p => p.idPromocije === azuriranaPromocija.idPromocije);
      if (postoji) {
        return prevPromocije.map(p => p.idPromocije === azuriranaPromocija.idPromocije ? azuriranaPromocija : p);
      } else {
        return [...prevPromocije, azuriranaPromocija];
      }
    });
    zatvoriFormu();
  };
  const filtriranePromocije = promocije.filter(p => {
    const naslovTekst = `${p.naslovPromocije} ${p.tekstPromocije}`.toLowerCase();
    const prolaziTekst = naslovTekst.includes(trazenaRijec.toLowerCase());

    const datumPocetka = new Date(p.datumPočetkaProm);
    const datumKraja = new Date(p.datumKrajaProm);
    const datumOdDate = datumOd ? new Date(datumOd) : null;
    const datumDoDate = datumDo ? new Date(datumDo) : null;

    const prolaziDatum =
      (!datumOdDate || datumPocetka >= datumOdDate) && (!datumDoDate || datumPocetka <= datumDoDate)
      ||
      (!datumOdDate || datumKraja >= datumOdDate) && (!datumDoDate || datumKraja <= datumDoDate);
    return prolaziTekst && prolaziDatum;
  });

  return (
    <div className="promocije-container">
      <h2>Promocije trgovine</h2>
      <span onClick={() => otvoriFormuZaUredjivanje(undefined)} className="dodaj-promociju-link">
        + dodaj promociju
      </span>
      <br></br>
      <input
        type="text"
        placeholder="Pretraži promocije..."
        value={trazenaRijec}
        onChange={(e) => setTrazenaRijec(e.target.value)}
        style={{ margin: '1rem', padding: '0.5rem', width: '50%' }}
      />
      <span style={{ marginBottom: '1rem' }}>
        <label>
          Od:{" "}
          <input
            type="date"
            value={datumOd}
            onChange={(e) => setDatumOd(e.target.value)}
          />
        </label>
        <label style={{ marginLeft: '1rem' }}>
          Do:{" "}
          <input
            type="date"
            value={datumDo}
            onChange={(e) => setDatumDo(e.target.value)}
          />
        </label>
      </span>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      {promocije.length === 0 && <p>Nema promocija za ovu trgovinu.</p>}

      <ul>
        {filtriranePromocije.map(promo => (
          <li key={promo.idPromocije} style={{ marginBottom: '1rem' }}>
            <strong>{promo.naslovPromocije}</strong><br />
            <small>
              {new Date(promo.datumPočetkaProm).toLocaleDateString()} - {new Date(promo.datumKrajaProm).toLocaleDateString()}
            </small>
            <button onClick={() => otvoriFormuZaUredjivanje(promo)} style={{ marginLeft: "1.5rem" }}>Uredi</button>{' '}
            <button onClick={() => handleDelete(promo.idPromocije)}>Izbriši</button>
          </li>
        ))}
      </ul>
      {promocijaZaUredjivanje && (
        <div className="overlay">
          <div className="modal">
            <PromocijaForma
              promocija={promocijaZaUredjivanje}
              onSubmit={spremiPromociju}
              onCancel={zatvoriFormu}
              idTrgovine={idTrgovine}
            />
          </div>
        </div>
      )}
    </div>
  );
}

export default PromocijeTrgovine;
