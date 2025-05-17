package fer.infsus.Shopcentar.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "promocijatrgovine")
public class PromocijaTrgovine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idpromocije")
    private Integer idPromocije;

    @Column(name = "naslovpromocije", unique = true, nullable = false)
    private String naslovPromocije;

    @Column(name = "tekstpromocije", nullable = false)
    private String tekstPromocije;

    @Column(name = "trenutakobjaveprom", nullable = false)
    @UpdateTimestamp
    private LocalDateTime trenutakObjaveProm;

    @Column(name = "slikapromocije", nullable = false)
    private String slikaPromocije;

    @Column(name = "datumpočetkaprom", nullable = false)
    private LocalDate datumPočetkaProm;

    @Column(name = "datumkrajaprom", nullable = false)
    private LocalDate datumKrajaProm;

    @ManyToOne
    @JoinColumn(name = "idobjavitelja", nullable = false)
    private Osoba objavitelj;

    @ManyToOne
    @JoinColumn(name = "idtrgovine", nullable = false)
    private Trgovina trgovina;

    public Integer getIdPromocije() {
        return idPromocije;
    }

    public void setIdPromocije(Integer idPromocije) {
        this.idPromocije = idPromocije;
    }

    public String getNaslovPromocije() {
        return naslovPromocije;
    }

    public void setNaslovPromocije(String naslovPromocije) {
        this.naslovPromocije = naslovPromocije;
    }

    public String getTekstPromocije() {
        return tekstPromocije;
    }

    public void setTekstPromocije(String tekstPromocije) {
        this.tekstPromocije = tekstPromocije;
    }

    public LocalDateTime getTrenutakObjaveProm() {
        return trenutakObjaveProm;
    }

    public void setTrenutakObjaveProm(LocalDateTime trenutakObjaveProm) {
        this.trenutakObjaveProm = trenutakObjaveProm;
    }

    public String getSlikaPromocije() {
        return slikaPromocije;
    }

    public void setSlikaPromocije(String slikaPromocije) {
        this.slikaPromocije = slikaPromocije;
    }

    public LocalDate getDatumPočetkaProm() {
        return datumPočetkaProm;
    }

    public void setDatumPočetkaProm(LocalDate datumPočetkaProm) {
        this.datumPočetkaProm = datumPočetkaProm;
    }

    public LocalDate getDatumKrajaProm() {
        return datumKrajaProm;
    }

    public void setDatumKrajaProm(LocalDate datumKrajaProm) {
        this.datumKrajaProm = datumKrajaProm;
    }

    public Osoba getObjavitelj() {
        return objavitelj;
    }

    public void setObjavitelj(Osoba objavitelj) {
        this.objavitelj = objavitelj;
    }

    public Trgovina getTrgovina() {
        return trgovina;
    }

    public void setTrgovina(Trgovina trgovina) {
        this.trgovina = trgovina;
    }
}
