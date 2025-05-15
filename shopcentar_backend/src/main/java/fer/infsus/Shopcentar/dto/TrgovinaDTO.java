package fer.infsus.Odbojka.dto;

import java.time.LocalDateTime;

public class TrgovinaDTO {
    private String nazivTrgovine;
    private String radnoVrijeme;
    private String logoTrgovine;
    private String telefonTrgovine;
    private String emailTrgovine;
    private String emailUpravitelj;
    private LocalDateTime posljednjeAžuriranje;

    public LocalDateTime getPosljednjeAžuriranje() {
        return posljednjeAžuriranje;
    }

    public void setPosljednjeAžuriranje(LocalDateTime posljednjeAžuriranje) {
        this.posljednjeAžuriranje = posljednjeAžuriranje;
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

    public String getEmailUpravitelj() {
        return emailUpravitelj;
    }

    public void setEmailUpravitelj(String emailUpravitelj) {
        this.emailUpravitelj = emailUpravitelj;
    }
}
