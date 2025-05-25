import { useParams } from 'react-router-dom';
import { useEffect, useState } from 'react';
import Galerija from "./Galerija";
import Promocije from "./Promocije";
import { useNavigate } from 'react-router-dom';
import Select from "react-dropdown-select";
import "./Trgovina.css";

function Trgovina() {
  const { id } = useParams();
  const [trgovina, setTrgovina] = useState(null);
  const [poruka, setPoruka] = useState("");
  const [logoFile, setLogoFile] = useState(null);
  const [logoPreview, setLogoPreview] = useState(null);
  const [kategorije, setKategorije] = useState([]);
  const [fotografije, setFotografije] = useState([]);
  const [refreshKey, setRefreshKey] = useState(0);
  const [noviId, setNoviId] = useState(null);
  const [katTrgovine, setKatTrgovine] = useState([]);
  const [upravitelji, setUpravitelji] = useState([]);
  const navigate = useNavigate();
  let loading = false;

  
    useEffect(() => {
      loading = true;
      fetch(id && id !== "novo" ? `/api/trgovina/kategorije/${id}` : `/api/trgovina/kategorije`)
      .then(res => {
        if (!res.ok) throw new Error("Neuspješan dohvat kategorija trgovine");
        return res.json();
      })
      .then(data => {
        const kategorijeTNiz = data.map((el) => ({
          value: parseInt(el.idKategorije),
          label: el.nazivKategorije,
        }));
        setKatTrgovine(kategorijeTNiz);
      })
      .catch(err => console.error("Greška kod dohvaćanja kategorija:", err));
      loading = false;
    }, []);
  

  useEffect(() => {
    loading = true;
    fetch(`/api/kategorija/popis`)
      .then(res => {
        if (!res.ok) throw new Error("Neuspješan dohvat kategorija");
        return res.json();
      })
      .then(data => {
        const kategorijeNiz = Object.entries(data).map((x) => ({
          value: parseInt(x[0]),
          label: x[1],
        }));
        setKategorije(kategorijeNiz);
      })
      .catch(err => console.error("Greška kod dohvaćanja kategorija:", err));
    loading = false;
  }, []);

  useEffect(() => {
    loading = true;
    fetch(`/api/osoba/upravitelji`)
      .then(res => {
        if (!res.ok) throw new Error("Neuspješan dohvat upravitelja");
        return res.json();
      })
      .then(data => {
        const upraviteljiNiz = data.map((x) => ({
          value: parseInt(x.idOsobe),
          label: x.emailOsobe,
        }));
        setUpravitelji(upraviteljiNiz);
      })
      .catch(err => console.error("Greška kod dohvaćanja kategorija:", err));
      loading = false;
  }, []);

  useEffect(() => {
    if (id === "novo") {
      setTrgovina(prev => {
        if (prev) return prev;
        return {
          nazivTrgovine: "",
          radnoVrijeme: "",
          emailTrgovine: "",
          telefonTrgovine: "",
          emailUpravitelj: "",
          logoTrgovine: "",
          posljednjeAžuriranje: new Date().toISOString(),
        };
      });
    } else {
      setNoviId(id);
      fetch(`/api/trgovina/${id}`)
        .then(res => {
          if (!res.ok) throw new Error('Neuspješan dohvat podataka');
          return res.json();
        })
        .then(data => {
          setTrgovina(data);})
        .catch(err => alert(err.message));
        
    }
  }, [id]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setTrgovina(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleChangeUp = (e) => {
    setTrgovina(prev => ({
      ...prev,
      emailUpravitelj: e[0].label
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setPoruka("");

    //Provjera naziva
    if (!trgovina.nazivTrgovine?.trim()) {
      alert("Naziv trgovine je obavezan.");
      return;
    }

    //Logo obavezan ako je novo
    if ((id === "novo" || !id) && !logoFile) {
      alert("Logo je obavezan kod dodavanja nove trgovine.");
      return;
    }

    //Logo mora biti slika
    if (logoFile && !logoFile.type.startsWith("image/")) {
      alert("Logo mora biti slika (jpg, png, gif...)");
      return;
    }

    //Sve fotografije moraju biti slike
    for (const file of fotografije) {
      if (!file.type.startsWith("image/")) {
        alert(`Datoteka "${file.name}" nije slika.`);
        return;
      }
    }

    if(trgovina.telefonTrgovine){
      const ocisceniBroj = trgovina.telefonTrgovine.replace(/[\s.-]/g, '');
      const regexFiksni = /^(\+385|0)1\d{7}$/;
      const regexMobilni = /^(\+385|0)9[1-9]\d{7}$/;
      if(!(regexFiksni.test(ocisceniBroj) || regexMobilni.test(ocisceniBroj))) {
        alert("Neispravan broj telefona.")
        return
      }
    }

    if(trgovina.emailTrgovine){
      const emailRegex = /^(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])$/;
      if (!emailRegex.test(trgovina.emailTrgovine)) {
        alert("Neispravna email adresa.");
        return;
      }
    }

    // Validacija radnog vremena
    const pattern = /^([01]\d|2[0-3]):[0-5]\d-([01]\d|2[0-3]):[0-5]\d$/;
    if (!pattern.test(trgovina.radnoVrijeme || "")) {
      alert("Radno vrijeme mora biti u formatu HH:mm-HH:mm (npr. 09:00-20:00).");
      return;
    }
    const [pocetak, kraj] = trgovina.radnoVrijeme.split("-");
    const toMin = (t) => {
      const [h, m] = t.split(":").map(Number);
      return h * 60 + m;
    };
    if (toMin(pocetak) >= toMin(kraj)) {
      alert("Vrijeme početka mora biti prije završetka.");
      return;
    }
    
    const formData = new FormData();
    formData.append(
      "dto",
      new Blob([JSON.stringify(trgovina)], { type: "application/json" })
    );
    if (logoFile) formData.append("logo", logoFile);
    fotografije.forEach((file) => formData.append("fotografije", file));
    formData.append(
      "kategorije",
      new Blob([JSON.stringify(katTrgovine.map((el) => ({idKategorije: el.value, nazivKategorije: el.label})))], { type: "application/json" })
    );

    fetch(id && id !== "novo" ? `/api/trgovina/${id}` : "/api/trgovina/", {
      method: "POST",
      body: formData,
    })
      .then(async (res) => {
        if (!res.ok) throw new Error(await res.text());
        return res.json();
      })
      .then((data) => {
        setFotografije([]);
        setRefreshKey((prev) => prev + 1);
        setNoviId(data.id);
        setPoruka("Uspješno spremljeno!");
        navigate("/trgovina/"+data.id);
      })
      .catch((err) => alert(err.message));
  };


  const handleLogoChange = (e) => {
    const file = e.target.files[0];
    setLogoFile(file);
    if (file) {
      setLogoPreview(URL.createObjectURL(file));
    } else {
      setLogoPreview(null);
    }
  };

  const handleChangeKategorija = (e) => {
    setKatTrgovine(e);
  };

    const handleDelete = (e) => {
      e.preventDefault();
      if (window.confirm("Jeste li sigurni da želite izbrisati trgovinu?")) {
        fetch(`/api/trgovina/izbrisi/${id}`, {
          method: 'DELETE',
        })
          .then(async response => {
            if (response.ok) {
              alert(await response.text());
              navigate("/trgovine");
            } else {
              alert(await response.text());
            }
          })
          .catch(error => {
            alert("Greška pri komunikaciji sa serverom!");
          });
      }
    }

  function getIdUp () {
    const selectedUpravitelj = upravitelji?.find(up => up.value === trgovina.emailUpravitelj);
    return selectedUpravitelj;
  }

  if (!trgovina) return <p>Učitavanje podataka...</p>;
  if (loading) return <p>Učitavanje podataka...</p>;



  return (<>
    <form onSubmit={handleSubmit}>
      <h2 style={{ textAlign: 'left' }}>
        {id === "novo" ? "Stvori trgovinu" : "Uredi trgovinu"}
      </h2>
      <div className="form-row">
      <label>Naziv:</label>
        <input
          type="text"
          name="nazivTrgovine"
          value={trgovina.nazivTrgovine}
          onChange={handleChange}
          required
        />
      </div>
      <br />
      <div className="form-row">
        <label>Logo:</label><br />
        {logoPreview ? (
          <img
            src={logoPreview}
            alt="Novi logo"
            style={{ maxWidth: '150px', marginLeft: '54px' }}
          />
        ) : (
          trgovina.logoTrgovine && (
            <img
              src={`http://localhost:8080/slike/logo/${trgovina.logoTrgovine}`}
              alt="Logo"
              style={{ maxWidth: '150px', marginLeft: '54px' }}
            />
          )
        )}
        <input type="file" accept="image/*" required={id === "novo"}  onChange={handleLogoChange} />
      </div>
      <br />
      <div className="form-row">
      <label>Radno vrijeme: </label>
      <div className="radno-vrijeme-wrapper">
        <input
          type="time"
          value={trgovina.radnoVrijeme?.split('-')[0] || ''}
          onChange={(e) => {
            const kraj = trgovina.radnoVrijeme?.split('-')[1] || '';
            handleChange({
              target: {
                name: 'radnoVrijeme',
                value: `${e.target.value}-${kraj}`
              }
            });
          }}
          required
          style={{width:"auto"}}
        />
        <span> - </span>
        <input
          type="time"
          value={trgovina.radnoVrijeme?.split('-')[1] || ''}
          onChange={(e) => {
            const pocetak = trgovina.radnoVrijeme?.split('-')[0] || '';
            handleChange({
              target: {
                name: 'radnoVrijeme',
                value: `${pocetak}-${e.target.value}`
              }
            });
          }}
          required
          style={{width:"auto"}}
        />
      </div>
     </div>
      <br />

      <div className="form-row">
      <label>Email trgovine:</label>
        <input
          type="email"
          name="emailTrgovine"
          value={trgovina.emailTrgovine}
          onChange={handleChange}
        />
      </div>
      <br />

      <div className="form-row">
      <label>Telefon:</label>
        <input
          type="tel"
          name="telefonTrgovine"
          value={trgovina.telefonTrgovine}
          onChange={handleChange}
        />
      </div>
      <br />
      
      <div className="form-row">
      <label>Kategorije:</label>
      <Select
        name="kategorije"
        multi
        searchable
        handle
        keepSelectedInList
        dropdownPosition="bottom"
        values={katTrgovine}
        options={kategorije}
        onChange={handleChangeKategorija}
      /></div>

      <div className="form-row">
      <label>Upravitelj:</label>
        <Select
          name="emailUpravitelj"
          values={[{value: getIdUp, label: trgovina.emailUpravitelj}]}
          options={upravitelji}
          onChange={handleChangeUp}
          required
        />
      </div>
      <br />
      
      <div className="form-row">
        <label>Fotografije trgovine:</label>
        <input
          type="file"
          accept="image/*"
          multiple
          onChange={(e) => setFotografije(Array.from(e.target.files))}
        />
      </div>
      <br />
      
      {id!=="novo" && (
        <label>
          <span className="readonly-text">
            Posljednje ažuriranje: {new Date(trgovina.posljednjeAžuriranje).toLocaleString()}
          </span>
        </label>
      )}

      <button className="spremi" type="submit" style={{marginLeft:"530px",backgroundColor:"blue"}}>Spremi</button>
      {id!=="novo" && (<button className="spremi" onClick={handleDelete}>Izbriši</button>)}
      {poruka && (
        <div className="uspjesna-poruka">
          {poruka}
        </div>
      )}
      {id!=="novo" && (<Galerija idTrgovine={id} refreshKey={refreshKey} noviId={noviId}/>)}
    </form>
    {id!=="novo" && (<Promocije idTrgovine={id}/>)}</>
  );
}

export default Trgovina;
