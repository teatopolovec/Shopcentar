package fer.infsus.Shopcentar.service.impl;

import fer.infsus.Shopcentar.dao.PromocijaRepository;
import fer.infsus.Shopcentar.domain.Osoba;
import fer.infsus.Shopcentar.domain.PromocijaTrgovine;
import fer.infsus.Shopcentar.domain.Trgovina;
import fer.infsus.Shopcentar.dto.PromocijaDTO;
import fer.infsus.Shopcentar.service.OsobaService;
import fer.infsus.Shopcentar.service.PromocijaService;
import fer.infsus.Shopcentar.service.TrgovinaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PromocijaServiceJpa implements PromocijaService {
    @Autowired
    private PromocijaRepository promocijaRepo;
    @Autowired
    private OsobaService osobaService;
    @Autowired
    private TrgovinaService trgovinaService;
    @Override
    public List<PromocijaDTO> dohvatiPromocije(Integer id) {
        List<PromocijaTrgovine> promocije = promocijaRepo.findAllByIdtrgovine(id);

        return promocije.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PromocijaDTO azurirajPromociju(Integer id, PromocijaDTO dto, MultipartFile slika) throws IOException {
        PromocijaTrgovine promocija = promocijaRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Promocija nije pronađena"));

        if (!Objects.equals(promocija.getNaslovPromocije(), dto.getNaslovPromocije())) {
            if (dto.getNaslovPromocije() == null || dto.getNaslovPromocije().isEmpty()) {
                throw new IllegalArgumentException("Promocija mora imati naslov.");
            }
            promocija.setNaslovPromocije(dto.getNaslovPromocije());
        }

        if (!Objects.equals(promocija.getTekstPromocije(), dto.getTekstPromocije())) {
            if (dto.getTekstPromocije() == null || dto.getTekstPromocije().isEmpty()) {
                throw new IllegalArgumentException("Promocija mora imati tekst.");
            }
            promocija.setTekstPromocije(dto.getTekstPromocije());
        }

        if (dto.getDatumPočetkaProm() != null && dto.getDatumKrajaProm() != null) {
            if (dto.getDatumPočetkaProm().isAfter(dto.getDatumKrajaProm())) {
                throw new IllegalArgumentException("Datum početka promocije ne može biti nakon datuma kraja.");
            }
        } else {
            throw new IllegalArgumentException("Oba datuma moraju biti popunjena.");
        }
        promocija.setDatumPočetkaProm(dto.getDatumPočetkaProm());
        promocija.setDatumKrajaProm(dto.getDatumKrajaProm());

        if (slika != null && !slika.isEmpty()) {
            String novaEkstenzija = slika.getOriginalFilename()
                    .substring(slika.getOriginalFilename().lastIndexOf('.') + 1)
                    .toLowerCase();

            try {
                if (slika.getSize() > 1 * 1024 * 1024) {
                    throw new RuntimeException("Slika je prevelika, najviše 1MB.");
                }
                String contentType = slika.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    throw new RuntimeException("Slika mora biti slika..");
                }

                Path dir = Paths.get("slike", "promocije", String.valueOf(promocija.getTrgovina().getIdTrgovine()));
                if (!Files.exists(dir)) {
                    Files.createDirectories(dir);
                }

                if (promocija.getSlikaPromocije() != null) {
                    Path staraPutanja = dir.resolve(promocija.getSlikaPromocije());
                    if (Files.exists(staraPutanja)) {
                        Files.delete(staraPutanja);
                    }
                }
                String novoIme = promocija.getIdPromocije() + "." + novaEkstenzija;
                Path novaPutanja = dir.resolve(novoIme);
                Files.copy(slika.getInputStream(), novaPutanja, StandardCopyOption.REPLACE_EXISTING);
                promocija.setSlikaPromocije(novoIme);
                dto.setSlikaPromocije(novoIme);
            } catch (IOException e) {
                throw new RuntimeException("Greška prilikom spremanja promocije: " + e.getMessage(), e);
            }
        }

        promocijaRepo.save(promocija);
        return dto;
    }

    @Override
    @Transactional
    public PromocijaDTO kreirajPromociju(PromocijaDTO dto, MultipartFile slika) {
        PromocijaTrgovine promocija = new PromocijaTrgovine();

        if (dto.getNaslovPromocije() == null || dto.getNaslovPromocije().isEmpty()) {
            throw new IllegalArgumentException("Promocija mora imati naslov.");
        }
        promocija.setNaslovPromocije(dto.getNaslovPromocije());

        if (dto.getTekstPromocije() == null || dto.getTekstPromocije().isEmpty()) {
            throw new IllegalArgumentException("Promocija mora imati tekst.");
        }
        promocija.setTekstPromocije(dto.getTekstPromocije());


        if (dto.getDatumPočetkaProm() != null && dto.getDatumKrajaProm() != null) {
            if (dto.getDatumPočetkaProm().isAfter(dto.getDatumKrajaProm())) {
                throw new IllegalArgumentException("Datum početka promocije ne može biti nakon datuma kraja.");
            }
        } else {
            throw new IllegalArgumentException("Oba datuma moraju biti popunjena.");
        }
        promocija.setDatumPočetkaProm(dto.getDatumPočetkaProm());
        promocija.setDatumKrajaProm(dto.getDatumKrajaProm());

        if (slika.isEmpty()) throw new IllegalArgumentException("Promocija mora imati sliku.");

        Osoba o = osobaService.dohvatiUpraviteljaPoEmailu("ana.admin@hedera.com");
        promocija.setObjavitelj(o);
        o.getPromocije().add(promocija);

        Trgovina t = trgovinaService.findById2(dto.getIdTrgovine()).orElseThrow(() -> new IllegalArgumentException("Trgovina nije pronađena"));
        t.getPromocije().add(promocija);
        promocija.setTrgovina(t);

        promocija.setSlikaPromocije("");
        promocijaRepo.save(promocija);
        dto.setIdPromocije(promocija.getIdPromocije());

        if (slika != null && !slika.isEmpty()) {
            if (slika.getSize() > 1 * 1024 * 1024) {
                throw new RuntimeException("Slika je prevelika, najviše 1MB.");
            }
            String contentType = slika.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new RuntimeException("Slika mora biti slika..");
            }
            String vrsta = slika.getOriginalFilename().substring(slika.getOriginalFilename().lastIndexOf('.') + 1).toLowerCase();
            try {
                Path putanja = Paths.get("slike", "promocije", String.valueOf(dto.getIdTrgovine()));
                if (!Files.exists(putanja)) {
                    Files.createDirectories(putanja);
                }
                Path punaPutanja = putanja.resolve(promocija.getIdPromocije() +"."+vrsta);
                Files.copy(slika.getInputStream(), punaPutanja, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException("Greška prilikom spremanja promocije: " + e.getMessage(), e);
            }
            promocija.setSlikaPromocije((promocija.getIdPromocije())+"."+vrsta);
            dto.setSlikaPromocije((promocija.getIdPromocije())+"."+vrsta);
        }
        promocijaRepo.save(promocija);
        return dto;
    }

    @Override
    @Transactional
    public void izbrisiPromociju(Integer id) throws IOException {
        PromocijaTrgovine promocija = promocijaRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Promocija nije pronađena"));
        promocija.getTrgovina().getPromocije().remove(promocija);
        promocija.getObjavitelj().getPromocije().remove(promocija);
        Path putanja = Paths.get("slike", "promocije", String.valueOf(promocija.getTrgovina().getIdTrgovine()));
        Path punaPutanja = putanja.resolve(promocija.getSlikaPromocije());
        if (!Files.exists(punaPutanja)) {
            throw new IllegalArgumentException("Slika ne postoji.");
        }
        Files.delete(punaPutanja);

        Path folder = putanja;
        try (var dirStream = Files.newDirectoryStream(folder)) {
            if (!dirStream.iterator().hasNext()) {
                Files.delete(folder);
            }
        }

        promocijaRepo.delete(promocija);
    }

    private PromocijaDTO convertToDto(PromocijaTrgovine promocija) {
        PromocijaDTO dto = new PromocijaDTO();
        dto.setIdPromocije(promocija.getIdPromocije());
        dto.setNaslovPromocije(promocija.getNaslovPromocije());
        dto.setTekstPromocije(promocija.getTekstPromocije());
        dto.setSlikaPromocije(promocija.getSlikaPromocije());
        dto.setDatumPočetkaProm(promocija.getDatumPočetkaProm());
        dto.setDatumKrajaProm(promocija.getDatumKrajaProm());
        dto.setObjavitelj(promocija.getObjavitelj().getEmailOsobe());
        dto.setTrenutakObjaveProm(promocija.getTrenutakObjaveProm());
        return dto;
    }
}
