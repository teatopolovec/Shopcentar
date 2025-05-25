import { useParams } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import "./Etaza.css";

function Etaza() {
    const {id} = useParams();
    const [etaza, setEtaza] = useState(null);
    const [poruka, setPoruka] = useState("");
    const navigate = useNavigate();

    useEffect(() => {
        if (id === "novo") {
        setEtaza(prev => {
            if (prev) return prev;
            return {
            brojEtaze: "",
            opis: "",
            };
        });
        } else {
            fetch(`/api/etaza/${id}`)
                .then(res => {
                if (!res.ok) throw new Error('Neuspješan dohvat podataka');
                return res.json();
                })
                .then(data => {
                setEtaza(data);})
                .catch(err => alert(err.message));
        }
    }, [id]);

    const handleSubmit = (e) => {
        e.preventDefault();
        setPoruka("");

        if (etaza.brojEtaze === "") {
            alert("Broj etaže je obavezan.");
            return;
        } else{
            const regexBroj = /^[0-9]+$/;
            if(!(regexBroj.test(etaza.brojEtaze))){
                alert("Neispravan broj etaže.")
                return;
            }
        }

        const formData = new FormData();
        formData.append("etaza", new Blob([JSON.stringify(etaza)], { type: "application/json" })
        );

        fetch(id && id !== "novo" ? `/api/etaza/${id}` : "/api/etaza/", {
        method: "POST",
        body: formData,
        })
        .then(async (res) => {
            if (!res.ok) throw new Error(await res.text());
            return res.json();
        })
        .then((data) => {
            setPoruka("Uspješno spremljeno!");
            navigate("/etaza/"+data.id);
        })
        .catch((err) => alert(err.message));
    };

    const handleDelete = (e) => {
      e.preventDefault();
      if (window.confirm("Jeste li sigurni da želite izbrisati etažu?")) {
        fetch(`/api/etaza/izbrisi/${id}`, {
          method: 'DELETE',
        })
          .then(async response => {
            if (response.ok) {
              alert(await response.text());
              navigate("/etaze");
            } else {
              alert(await response.text());
            }
          })
          .catch(error => {
            alert("Greška pri komunikaciji sa serverom!");
          });
      }
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setEtaza(prev => ({
        ...prev,
        [name]: value
        }));
    };

if (!etaza) return <p>Učitavanje podataka...</p>;

return (<>
    <form onSubmit={handleSubmit}>
      <h2 style={{ textAlign: 'left' }}>
        {id === "novo" ? "Stvori etažu" : "Uredi etažu"}
      </h2>
      <div className="form-row">
      <label>Broj etaže:</label>
        <input
          type="text"
          name="brojEtaze"
          value={etaza.brojEtaze}
          onChange={handleChange}
          required
        />
      </div>
      <br />

      <div className="form-row">
      <label>Opis:</label>
        <input
          type="text"
          name="opis"
          value={etaza.opis}
          onChange={handleChange}
          required
        />
      </div>
      <br />

      <button className="spremi" type="submit" style={{marginLeft:"530px",backgroundColor:"blue"}}>Spremi</button>
      {id!=="novo" && (<button className="spremi" onClick={handleDelete}>Izbriši</button>)}
      {poruka && (
        <div className="uspjesna-poruka">
          {poruka}
        </div>
      )}
    </form>
    </>
  );
}

export default Etaza;