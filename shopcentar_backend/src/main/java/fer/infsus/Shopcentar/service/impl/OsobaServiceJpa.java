package fer.infsus.Odbojka.service.impl;


import fer.infsus.Odbojka.dao.OsobaRepository;
import fer.infsus.Odbojka.domain.Osoba;
import fer.infsus.Odbojka.service.OsobaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OsobaServiceJpa implements OsobaService {
    @Autowired
    private OsobaRepository osobaRepo;

    public Osoba dohvatiUpraviteljaPoEmailu(String email) {
        return osobaRepo.findByEmailOsobe(email)
                .orElseThrow(() -> new RuntimeException("Upravitelj nije pronađen"));
    }
}
