package fer.infsus.Shopcentar.service.impl;

import fer.infsus.Shopcentar.dao.SlikaTrgovineRepository;
import fer.infsus.Shopcentar.domain.SlikaTrgovine;
import fer.infsus.Shopcentar.domain.Trgovina;
import fer.infsus.Shopcentar.service.SlikaTrgovineService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SlikaTrgovineServiceJpa implements SlikaTrgovineService {
    @Autowired
    SlikaTrgovineRepository slikaRepo;
    @Override
    @Transactional
    public void spremiSlike(List<MultipartFile> fotografije, Trgovina t) {
        if (fotografije == null || fotografije.isEmpty()) return;

        Path direktorij = Paths.get("slike", String.valueOf(t.getIdTrgovine()));

        try {
            if (!Files.exists(direktorij)) {
                Files.createDirectories(direktorij);
            }

            for (MultipartFile fotografija : fotografije) {
                String contentType = fotografija.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    continue;
                }

                String originalName = fotografija.getOriginalFilename();
                if (originalName == null || originalName.isBlank()) continue;

                String ekstenzija = originalName.substring(originalName.lastIndexOf('.') + 1).toLowerCase();
                String ime = UUID.randomUUID() + "." + ekstenzija;

                Path destinacija = direktorij.resolve(ime);
                fotografija.transferTo(destinacija);
                SlikaTrgovine s = new SlikaTrgovine();
                s.setNazivSlikeTrgovine(ime);
                s.setTrgovina(t);
                t.getSlike().add(s);
                slikaRepo.save(s);
            }

        } catch (IOException e) {
            throw new RuntimeException("Greška prilikom spremanja fotografija: " + e.getMessage(), e);
        }
    }

    @Override
    public List<String> dohvatiSlike(Integer id) {
        List<SlikaTrgovine> slike = slikaRepo.findByTrgovinaIdTrgovine(id);
        List<String> nazivi = slike.stream()
                .map(SlikaTrgovine::getNazivSlikeTrgovine)
                .toList();
        return nazivi;
    }

    @Override
    @Transactional
    public void obrisiSliku(Integer id, String naziv) throws IOException {
        Optional<SlikaTrgovine> s = slikaRepo.findByNazivSlikeTrgovineAndIdTrgovine(naziv, id);

        if (s.isPresent()) {
            SlikaTrgovine slika = s.get();
            Trgovina trgovina = slika.getTrgovina();
            if (trgovina != null && trgovina.getSlike() != null) {
                trgovina.getSlike().remove(slika);
            }

            slikaRepo.delete(slika);
        } else {
            throw new IllegalArgumentException("Slika nije pronađena.");
        }

        Path putanja = Paths.get("slike", String.valueOf(id), naziv);
        if (!Files.exists(putanja)) {
            throw new IllegalArgumentException("Slika ne postoji.");
        }

        Files.delete(putanja);

    }
}
