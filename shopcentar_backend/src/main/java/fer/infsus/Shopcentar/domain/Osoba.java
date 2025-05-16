package fer.infsus.Shopcentar.domain;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Osoba")
public class Osoba {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idosobe")
    private Integer idOsobe;

    @Column(nullable = false)
    private String ime;

    @Column(nullable = false)
    private String prezime;

    @Column(nullable = false)
    private String lozinka;

    @Column(name = "emailosobe", nullable = false, unique = true)
    private String emailOsobe;

    @Column(nullable = false)
    private String uloga;

    @Column(nullable = false)
    private String mobitel;

    @OneToMany(
            mappedBy = "upravitelj", fetch = FetchType.LAZY
    )
    private Set<Trgovina> trgovine = new HashSet<>();

    public Set<Trgovina> getTrgovine() {
        return trgovine;
    }

    public void setTrgovine(Set<Trgovina> trgovine) {
        this.trgovine = trgovine;
    }
    public Integer getIdOsobe() {
        return idOsobe;
    }

    public void setIdOsobe(Integer idOsobe) {
        this.idOsobe = idOsobe;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public String getEmailOsobe() {
        return emailOsobe;
    }

    public void setEmailOsobe(String emailOsobe) {
        this.emailOsobe = emailOsobe;
    }

    public String getUloga() {
        return uloga;
    }

    public void setUloga(String uloga) {
        this.uloga = uloga;
    }

    public String getMobitel() {
        return mobitel;
    }

    public void setMobitel(String mobitel) {
        this.mobitel = mobitel;
    }
}