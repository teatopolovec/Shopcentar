package fer.infsus.Shopcentar.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Trgovina")
public class Trgovina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtrgovine")
    private Integer idTrgovine;

    @Column(name = "nazivtrgovine", unique = true, nullable = false)
    private String nazivTrgovine;

    @Column(name = "radnovrijeme", nullable = false)
    private String radnoVrijeme;
    @Column(name = "posljednjeažuriranje", nullable = false)
    @UpdateTimestamp
    private LocalDateTime posljednjeAžuriranje;
    @Column(name = "logotrgovine", nullable = false)
    private String logoTrgovine;

    @Column(name = "aktivna", nullable = false)
    private boolean aktivna = true;
    @Column(name = "telefontrgovine")
    private String telefonTrgovine;
    @Column(name = "emailtrgovine")
    private String emailTrgovine;

    @ManyToOne
    @JoinColumn(name = "idupravitelj", nullable = false)
    private Osoba upravitelj;

    @OneToMany(
            mappedBy = "trgovina", fetch = FetchType.LAZY
    )
    private Set<PromocijaTrgovine> promocije = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "kategorija_trgovine",
            joinColumns = @JoinColumn(name = "idtrgovine"),
            inverseJoinColumns = @JoinColumn(name = "idkategorije"))
    private Set<Kategorija> kategorije = new HashSet<>();

    @OneToMany(
            mappedBy = "trgovina", fetch = FetchType.LAZY
    )
    private Set<SlikaTrgovine> slike = new HashSet<>();

    public Integer getIdTrgovine() {
        return idTrgovine;
    }

    public void setIdTrgovine(Integer idTrgovine) {
        this.idTrgovine = idTrgovine;
    }

    public String getNazivTrgovine() {
        return nazivTrgovine;
    }

    public void setNazivTrgovine(String nazivTrgovine) {
        this.nazivTrgovine = nazivTrgovine;
    }

    public String getRadnoVrijeme() {
        return radnoVrijeme;
    }

    public void setRadnoVrijeme(String radnoVrijeme) {
        this.radnoVrijeme = radnoVrijeme;
    }

    public LocalDateTime getPosljednjeAžuriranje() {
        return posljednjeAžuriranje;
    }

    public void setPosljednjeAžuriranje(LocalDateTime posljednjeAžuriranje) {
        this.posljednjeAžuriranje = posljednjeAžuriranje;
    }

    public String getLogoTrgovine() {
        return logoTrgovine;
    }

    public void setLogoTrgovine(String logoTrgovine) {
        this.logoTrgovine = logoTrgovine;
    }

    public String getTelefonTrgovine() {
        return telefonTrgovine;
    }

    public void setTelefonTrgovine(String telefonTrgovine) {
        this.telefonTrgovine = telefonTrgovine;
    }

    public String getEmailTrgovine() {
        return emailTrgovine;
    }

    public void setEmailTrgovine(String emailTrgovine) {
        this.emailTrgovine = emailTrgovine;
    }

    public Osoba getUpravitelj() {
        return upravitelj;
    }

    public void setUpravitelj(Osoba upravitelj) {
        this.upravitelj = upravitelj;
    }

    public Set<Kategorija> getKategorije() {
        return kategorije;
    }

    public void setKategorije(Set<Kategorija> kategorije) {
        this.kategorije = kategorije;
    }

    public Set<SlikaTrgovine> getSlike() {
        return slike;
    }


    public Set<PromocijaTrgovine> getPromocije() {
        return promocije;
    }

    public void setPromocije(Set<PromocijaTrgovine> promocije) {
        this.promocije = promocije;
    }

    public void setSlike(Set<SlikaTrgovine> slike) {
        this.slike = slike;
    }

    public boolean getAktivna() {
        return aktivna;
    }

    public void setAktivna(boolean aktivna) {
        this.aktivna = aktivna;
    }
}