package fer.infsus.Shopcentar.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "Prostor")
public class Prostor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProstora")
    private Integer idProstora;

    @Column(name = "kvadratura", nullable = false)
    private Integer kvadratura;

    @ManyToOne
    @JoinColumn(name = "idEtaza", nullable = false)
    private Etaza etaza;

    public Integer getIdProstora() {
        return idProstora;
    }

    public void setIdProstora(Integer idProstora) {
        this.idProstora = idProstora;
    }

    public Integer getKvadratura() {
        return kvadratura;
    }

    public void setKvadratura(Integer kvadratura) {
        this.kvadratura = kvadratura;
    }

    public Etaza getEtaza() {
        return etaza;
    }

    public void setEtaza(Etaza etaza) {
        this.etaza = etaza;
    }
}
