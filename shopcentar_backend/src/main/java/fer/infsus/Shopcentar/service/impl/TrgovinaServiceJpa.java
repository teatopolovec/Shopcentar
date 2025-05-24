package fer.infsus.Shopcentar.service.impl;


import fer.infsus.Shopcentar.dao.TrgovinaRepository;
import fer.infsus.Shopcentar.domain.Osoba;
import fer.infsus.Shopcentar.domain.Trgovina;
import fer.infsus.Shopcentar.dto.TrgovinaDTO;
import fer.infsus.Shopcentar.service.OsobaService;
import fer.infsus.Shopcentar.service.TrgovinaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@Service
public class TrgovinaServiceJpa implements TrgovinaService {
    @Autowired
    private TrgovinaRepository trgovinaRepo;
    @Autowired
    private OsobaService osobaService;
    @Transactional
    public Trgovina kreirajTrgovinu(TrgovinaDTO dto, MultipartFile logoFile) throws IOException {
        Trgovina trgovina = new Trgovina();

        if (trgovinaRepo.existsByNazivTrgovine(dto.getNazivTrgovine())) {
            throw new IllegalArgumentException("Trgovina s tim nazivom već postoji.");
        }
        if (dto.getNazivTrgovine() == null || dto.getNazivTrgovine().isEmpty()) {
            throw new IllegalArgumentException("Trgovina mora imati naziv.");
        }
        trgovina.setNazivTrgovine(dto.getNazivTrgovine());

        validirajRadnoVrijeme(dto.getRadnoVrijeme());
        trgovina.setRadnoVrijeme(dto.getRadnoVrijeme());

        if (!(dto.getTelefonTrgovine() == null || dto.getTelefonTrgovine().isEmpty())) {
            if(!validirajTelefon(dto.getTelefonTrgovine())) throw new IllegalArgumentException("Neispravan broj telefona.");
        }
        trgovina.setTelefonTrgovine(dto.getTelefonTrgovine());

        if (!(dto.getEmailTrgovine() == null || dto.getEmailTrgovine().isEmpty())) {
            String regexEmail = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+))";
            if (!dto.getEmailTrgovine().toLowerCase().matches(regexEmail)) throw new IllegalArgumentException("Neispravna email adresa.");
        }
        trgovina.setEmailTrgovine(dto.getEmailTrgovine());

        if (logoFile.isEmpty()) throw new IllegalArgumentException("Trgovina mora imati logo.");
        String vrsta = logoFile.getOriginalFilename().substring(logoFile.getOriginalFilename().lastIndexOf('.') + 1).toLowerCase();
        String ime = trgovina.getNazivTrgovine().toLowerCase() + "_logo." + vrsta;
        if (!logoFile.isEmpty()) {
            String contentType = logoFile.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IllegalArgumentException("Prenesena datoteka nije slika.");
            }
            if (logoFile.getSize() > 1 * 1024 * 1024) {
                throw new RuntimeException("Slika je prevelika, najviše 1MB.");
            }
            try {
                Path putanja = Paths.get("slike/logo").resolve(ime);
                if (!Files.exists(Paths.get("slike/logo"))) {
                    Files.createDirectories(Paths.get("slike/logo"));
                }
                Files.copy(logoFile.getInputStream(), putanja, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException("Greška prilikom spremanja slike: " + e.getMessage(), e);
            }
        }
        trgovina.setLogoTrgovine(ime);

        Osoba o = osobaService.dohvatiUpraviteljaPoEmailu(dto.getEmailUpravitelj());
        trgovina.setUpravitelj(o);
        o.getTrgovine().add(trgovina);

        return trgovinaRepo.save(trgovina);
    }

    @Override
    public List<Map<String, Object>> dohvatiTrgovine() {
        List<Object[]> rezultati = trgovinaRepo.findIdAndNaziv();
        List<Map<String, Object>> trgovine = new ArrayList<>();
        for (Object[] red : rezultati) {
            Map<String, Object> mapa = new HashMap<>();
            mapa.put("idTrgovine", red[0]);
            mapa.put("nazivTrgovine", red[1]);
            trgovine.add(mapa);
        }
        return trgovine;
    }

    @Override
    public Optional<TrgovinaDTO> findById(Integer id) {
        return trgovinaRepo.findById(id)
                .map(trgovina -> {
                    TrgovinaDTO dto = new TrgovinaDTO();
                    dto.setNazivTrgovine(trgovina.getNazivTrgovine());
                    dto.setRadnoVrijeme(trgovina.getRadnoVrijeme());
                    dto.setLogoTrgovine(trgovina.getLogoTrgovine());
                    dto.setTelefonTrgovine(trgovina.getTelefonTrgovine());
                    dto.setEmailTrgovine(trgovina.getEmailTrgovine());
                    dto.setPosljednjeAžuriranje(trgovina.getPosljednjeAžuriranje());
                    if (trgovina.getUpravitelj() != null) {
                        dto.setEmailUpravitelj(trgovina.getUpravitelj().getEmailOsobe());
                    } else {
                        dto.setEmailUpravitelj(null);
                    }

                    return dto;
                });
    }

    public Optional<Trgovina> findById2(Integer id) {
        return trgovinaRepo.findById(id);
    }

    @Override
    @Transactional
    public Trgovina azurirajTrgovinu(Integer id, TrgovinaDTO dto, MultipartFile logoFile) {
        Trgovina trgovina = trgovinaRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Trgovina s ID " + id + " nije pronađena."));

        boolean novo = false;
        if (!Objects.equals(trgovina.getNazivTrgovine(), dto.getNazivTrgovine())) {
            if (trgovinaRepo.existsByNazivTrgovine(dto.getNazivTrgovine())) {
                throw new IllegalArgumentException("Trgovina s tim nazivom već postoji.");
            }
            if (dto.getNazivTrgovine() == null || dto.getNazivTrgovine().isEmpty()) {
                throw new IllegalArgumentException("Trgovina mora imati naziv.");
            }
            trgovina.setNazivTrgovine(dto.getNazivTrgovine());
            novo = true;
        }

        if (!Objects.equals(trgovina.getRadnoVrijeme(), dto.getRadnoVrijeme())) {
            validirajRadnoVrijeme(dto.getRadnoVrijeme());
            trgovina.setRadnoVrijeme(dto.getRadnoVrijeme());
        }

        if (!Objects.equals(trgovina.getEmailTrgovine(), dto.getEmailTrgovine())) {
            if (!(dto.getEmailTrgovine() == null || dto.getEmailTrgovine().isEmpty())) {
                String regexEmail = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+))";
                if (!dto.getEmailTrgovine().toLowerCase().matches(regexEmail)) throw new IllegalArgumentException("Neispravna email adresa.");
            }
            trgovina.setEmailTrgovine(dto.getEmailTrgovine());
        }

        if (!Objects.equals(trgovina.getTelefonTrgovine(), dto.getTelefonTrgovine())) {
            if (!(dto.getTelefonTrgovine() == null || dto.getTelefonTrgovine().isEmpty())) {
                if(!validirajTelefon(dto.getTelefonTrgovine())) throw new IllegalArgumentException("Neispravan broj telefona.");
            }
            trgovina.setTelefonTrgovine(dto.getTelefonTrgovine());
        }

        Osoba o = osobaService.dohvatiUpraviteljaPoEmailu(dto.getEmailUpravitelj());
        if (!Objects.equals(o.getEmailOsobe(), trgovina.getUpravitelj().getEmailOsobe())) {
            trgovina.getUpravitelj().getTrgovine().remove(trgovina);
            trgovina.setUpravitelj(o);
            o.getTrgovine().add(trgovina);
        }


        if (novo) {
            String stariNazivLoga = trgovina.getLogoTrgovine();
            String vrsta;
            if(logoFile != null && !logoFile.isEmpty()) vrsta = logoFile.getOriginalFilename().substring(logoFile.getOriginalFilename().lastIndexOf('.') + 1).toLowerCase();
            else vrsta = stariNazivLoga.substring(stariNazivLoga.lastIndexOf('.') + 1);
            String noviNazivLoga = dto.getNazivTrgovine().toLowerCase() + "_logo." + vrsta;

            Path staraPutanja = Paths.get("slike/logo").resolve(stariNazivLoga);
            Path novaPutanja = Paths.get("slike/logo").resolve(noviNazivLoga);
            try {
                Files.move(staraPutanja, novaPutanja, StandardCopyOption.REPLACE_EXISTING);
                trgovina.setLogoTrgovine(noviNazivLoga);
            } catch (IOException e) {
                throw new RuntimeException("Greška prilikom preimenovanja slike: " + e.getMessage(), e);
            }
        }

        if (logoFile != null && !logoFile.isEmpty()) {
            String vrsta = logoFile.getOriginalFilename().substring(logoFile.getOriginalFilename().lastIndexOf('.') + 1).toLowerCase();
            String ime = trgovina.getNazivTrgovine().toLowerCase() + "_logo." + vrsta;
            String contentType = logoFile.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IllegalArgumentException("Prenesena datoteka nije slika.");
            }
            if (logoFile.getSize() > 1 * 1024 * 1024) {
                throw new RuntimeException("Slika je prevelika, najviše 1MB.");
            }
            try {
                Path direktorij = Paths.get("slike/logo");
                String prefiks = trgovina.getNazivTrgovine().toLowerCase() + "_logo";
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(direktorij, prefiks + ".*")) {
                    for (Path stara : stream) {
                        Files.deleteIfExists(stara);
                    }
                }
                Path putanja = direktorij.resolve(ime);
                Files.copy(logoFile.getInputStream(), putanja, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException("Greška prilikom spremanja slike: " + e.getMessage(), e);
            }
            trgovina.setLogoTrgovine(ime);
        }
        return trgovinaRepo.save(trgovina);
    }

    @Override
    @Transactional
    public void izbrisiTrgovinu(Integer id) {
        Trgovina trgovina = trgovinaRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Trgovina s ID " + id + " nije pronađena."));
        trgovina.setAktivna(false);
        trgovinaRepo.save(trgovina);
    }

    public void validirajRadnoVrijeme(String radnoVrijeme) {
        if (radnoVrijeme == null || radnoVrijeme.isEmpty()) {
            throw new IllegalArgumentException("Radno vrijeme ne smije biti prazno.");
        }

        String pattern = "^([01]\\d|2[0-3]):[0-5]\\d-([01]\\d|2[0-3]):[0-5]\\d$";
        if (!radnoVrijeme.matches(pattern)) {
            throw new IllegalArgumentException("Radno vrijeme mora biti u formatu HH:mm-HH:mm.");
        }

        String[] dijelovi = radnoVrijeme.split("-");
        String pocetak = dijelovi[0];
        String kraj = dijelovi[1];

        int pocetakMin = pretvoriUMinute(pocetak);
        int krajMin = pretvoriUMinute(kraj);

        if (pocetakMin >= krajMin) {
            throw new IllegalArgumentException("Vrijeme početka mora biti prije završetka.");
        }
    }

    private int pretvoriUMinute(String vrijeme) {
        String[] parts = vrijeme.split(":");
        int sati = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);
        return sati * 60 + minute;
    }

    public boolean validirajTelefon(String telefon) {
        if (telefon == null) return false;
        String cistiTelefon = telefon.replaceAll("[\\s\\-\\.\\(\\)]", "");
        String regexFiksni = "(\\+385|0)1\\d{7}";
        String regexMobilni = "(\\+385|0)9[1-9]\\d{7}";
        return cistiTelefon.matches(regexFiksni) || cistiTelefon.matches(regexMobilni);
    }


}
