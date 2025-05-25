package fer.infsus.Shopcentar.dto;

import fer.infsus.Shopcentar.domain.Prostor;

import java.util.HashSet;
import java.util.Set;

public class EtazaDTO {
    private Integer brojEtaze;

    private String opis;

    private Set<Prostor> prostori = new HashSet<>();

    public Set<Prostor> getProstori() {
        return prostori;
    }

    public void setProstori(Set<Prostor> prostori) {
        this.prostori = prostori;
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
