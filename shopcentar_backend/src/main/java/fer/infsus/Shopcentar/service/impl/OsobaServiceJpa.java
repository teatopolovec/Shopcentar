package fer.infsus.Shopcentar.service.impl;



import fer.infsus.Shopcentar.dao.OsobaRepository;
import fer.infsus.Shopcentar.domain.Osoba;
import fer.infsus.Shopcentar.service.OsobaService;
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
