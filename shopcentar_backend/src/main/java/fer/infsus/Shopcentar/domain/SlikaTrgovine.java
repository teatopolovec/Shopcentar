package fer.infsus.Shopcentar.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "slikatrgovine")
public class SlikaTrgovine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idsliketrgovine")
    private Integer idSlikeTrgovine;

    @Column(name = "nazivsliketrgovine", unique = true, nullable = false)
    private String nazivSlikeTrgovine;

    @ManyToOne
    @JoinColumn(name="idtrgovine", nullable=false)
    private Trgovina trgovina;
    public Integer getIdSlikeTrgovine() {
        return idSlikeTrgovine;
    }

    public void setIdSlikeTrgovine(Integer idSlikeTrgovine) {
        this.idSlikeTrgovine = idSlikeTrgovine;
    }

    public String getNazivSlikeTrgovine() {
        return nazivSlikeTrgovine;
    }

    public void setNazivSlikeTrgovine(String putanjaSlikeTrgovine) {
        this.nazivSlikeTrgovine = putanjaSlikeTrgovine;
    }

    public Trgovina getTrgovina() {
        return trgovina;
    }

    public void setTrgovina(Trgovina trgovina) {
        this.trgovina = trgovina;
    }
}
