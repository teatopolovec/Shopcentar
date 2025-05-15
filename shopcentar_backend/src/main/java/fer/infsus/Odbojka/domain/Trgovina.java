package fer.infsus.Odbojka.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

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
    @Column(name = "telefontrgovine")
    private String telefonTrgovine;
    @Column(name = "emailtrgovine")
    private String emailTrgovine;

    @ManyToOne
    @JoinColumn(name = "idupravitelj", nullable = false)
    private Osoba upravitelj;

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
}