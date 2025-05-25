package fer.infsus.Shopcentar.domain;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Etaza")
public class Etaza {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idetaza")
    private Integer idEtaze;

    @Column(name = "brojetaze", nullable = false)
    private Integer brojEtaze;

    @Column(name = "opis",nullable = false)
    private String opis;

    @OneToMany(
            mappedBy = "etaza", fetch = FetchType.LAZY
    )
    private Set<Prostor> prostori = new HashSet<>();

    public Set<Prostor> getProstori() {
        return prostori;
    }

    public void setProstori(Set<Prostor> prostori) {
        this.prostori = prostori;
    }

    public Integer getIdEtaze() {
        return idEtaze;
    }

    public void setIdEtaze(Integer idEtaze) {
        this.idEtaze = idEtaze;
    }

    public Integer getBrojEtaze() {
        return brojEtaze;
    }

    public void setBrojEtaze(Integer brojEtaze) {
        this.brojEtaze = brojEtaze;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }
}
