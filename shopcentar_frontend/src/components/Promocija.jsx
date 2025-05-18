import React, { useState } from 'react';
import "./Promocija.css";

function PromocijaForma({ promocija, onSubmit, onCancel, idTrgovine }) {
  const [slikaPromocije, setSlikaPromocije] = useState(null);
  const [izmijenjenaPromocija, setPromocija] = useState(promocija);
  const [logoPreview, setLogoPreview] = useState(null);
  const [poruka, setPoruka] = useState("");

  const handleChange = (e) => {
    const { name, value } = e.target;
    setPromocija(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    //Provjera naziva
    if (!izmijenjenaPromocija.naslovPromocije?.trim()) {
      alert("Naslov promocije je obavezan.");
      return;
    }

    //Provjera teksta
    if (!izmijenjenaPromocija.tekstPromocije?.trim()) {
      alert("Tekst promocije je obavezan.");
      return;
    }

    //Provjera slike
    if (slikaPromocije && !slikaPromocije.type.startsWith("image/")) {
      alert("Slika mora biti slika (jpg, png, gif...)");
      return;
    }

    if (!slikaPromocije && !izmijenjenaPromocija.idPromocije) {
      alert("Logo je obavezan kod dodavanja nove trgovine.");
      return;
    }

    //Provjera datuma
    const { datumPočetkaProm, datumKrajaProm } = izmijenjenaPromocija;
    const datumRegex = /^\d{4}-\d{2}-\d{2}$/;
    if (!datumRegex.test(datumPočetkaProm) || !datumRegex.test(datumKrajaProm)) {
      alert('Datumi moraju biti u formatu YYYY-MM-DD.');
      return;
    }
    const pocetak = new Date(datumPočetkaProm);
    const kraj = new Date(datumKrajaProm);

    if (kraj < pocetak) {
      alert('Datum kraja ne smije biti prije datuma početka.');
      return;
    }

    const formData = new FormData();
    formData.append(
      'dto',
      new Blob([JSON.stringify(izmijenjenaPromocija)], {
        type: 'application/json'
      })
    );
    if (slikaPromocije) {
      formData.append('slika', slikaPromocije);
    }
    try {
      var res;
      if(izmijenjenaPromocija.idPromocije) {
          res = await fetch(`/api/promocija/${izmijenjenaPromocija.idPromocije}`, {
          method: 'POST',
          body: formData,
        });
      } else {
          res = await fetch(`/api/promocija/`, {
          method: 'POST',
          body: formData,
        });
      }

      if (!res.ok) throw new Error(await res.text());
      setPoruka("Uspješno spremljeno!");
      const result = await res.json();
      onSubmit && onSubmit(result);
    } catch (err) {
      alert(err.message);
    }
  };

    const handleLogoChange = (e) => {
      const file = e.target.files[0];
      setSlikaPromocije(file);
      if (file) {
        setLogoPreview(URL.createObjectURL(file));
      } else {
        setLogoPreview(null);
      }
    };

  return (
    <form className="promocija-forma" onSubmit={handleSubmit} style={{ border: '1px solid #ccc', padding: '1rem', marginTop: '1rem' }}>
      <h3>Uredi promociju</h3>

      <label>
        Naslov promocije:<br />
        <input
          type="text"
          name="naslovPromocije"
          value={izmijenjenaPromocija.naslovPromocije}
          onChange={handleChange}
          required
        />
      </label>
      <br />

      <label>
        Tekst promocije:<br />
        <textarea
          name="tekstPromocije"
          value={izmijenjenaPromocija.tekstPromocije}
          onChange={handleChange}
          rows={4}
          required
        />
      </label>
      <br />

      <div className="form-row">
        <label>Slika:</label><br />
          {logoPreview ? (
            <img
              src={logoPreview}
              alt="Nova slika"
              style={{ maxWidth: '150px', marginLeft: '54px' }}
            />
          ) : (
            promocija.slikaPromocije && (
              <img
                src={`http://localhost:8080/slike/promocije/${idTrgovine}/${izmijenjenaPromocija.slikaPromocije}`}
                alt="Slika"
                style={{ maxWidth: '150px', marginLeft: '54px' }}
              />
            )
          )}
        <input type="file" accept="image/*" required={!izmijenjenaPromocija.idPromocije} onChange={handleLogoChange} />
      </div>

      <label>
        Datum početka promocije:<br />
        <input
          type="date"
          name="datumPočetkaProm"
          value={izmijenjenaPromocija.datumPočetkaProm}
          onChange={handleChange}
          required
        />
      </label>
      <br />

      <label>
        Datum kraja promocije:<br />
        <input
          type="date"
          name="datumKrajaProm"
          value={izmijenjenaPromocija.datumKrajaProm}
          onChange={handleChange}
          required
        />
      </label>
      <br />

      <div>
        <button type="submit">Spremi</button>{' '}
        <button type="button" onClick={onCancel}>Otkaži</button> 
      </div>


      {poruka && <p style={{ color: poruka.includes('Greška') ? 'red' : 'green' }}>{poruka}</p>}
    </form>
  );
}

export default PromocijaForma;
