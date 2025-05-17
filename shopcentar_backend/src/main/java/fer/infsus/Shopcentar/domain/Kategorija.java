package fer.infsus.Shopcentar.domain;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Kategorija")
public class Kategorija {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idkategorije")
    private Integer idKategorije;

    @Column(name = "nazivkategorije", unique = true, nullable = false)
    private String nazivKategorije;

    @ManyToMany(mappedBy = "kategorije")
    private Set<Trgovina> trgovine = new HashSet<>();

    public Integer getIdKategorije() {
        return idKategorije;
    }

    public void setIdKategorije(Integer idKategorije) {
        this.idKategorije = idKategorije;
    }

    public String getNazivKategorije() {
        return nazivKategorije;
    }

    public void setNazivKategorije(String nazivKategorije) {
        this.nazivKategorije = nazivKategorije;
    }

    public Set<Trgovina> getTrgovine() {
        return trgovine;
    }

    public void setTrgovine(Set<Trgovina> trgovine) {
        this.trgovine = trgovine;
    }
}
