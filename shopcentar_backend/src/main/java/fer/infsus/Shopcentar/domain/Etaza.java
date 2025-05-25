package fer.infsus.Shopcentar.domain;

import jakarta.persistence.*;

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
