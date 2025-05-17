package fer.infsus.Shopcentar.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PromocijaDTO {

    private Integer idPromocije;
    private String naslovPromocije;
    private String tekstPromocije;
    private LocalDateTime trenutakObjaveProm;
    private String slikaPromocije;
    private LocalDate datumPočetkaProm;
    private LocalDate datumKrajaProm;
    private Integer idTrgovine;
    private String objavitelj;

    public PromocijaDTO() {
    }

    // Getteri i setteri:

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

    public Integer getIdTrgovine() {
        return idTrgovine;
    }

    public void setIdTrgovine(Integer idTrgovine) {
        this.idTrgovine = idTrgovine;
    }

    public String getObjavitelj() {
        return objavitelj;
    }

    public void setObjavitelj(String objavitelj) {
        this.objavitelj = objavitelj;
    }

}

