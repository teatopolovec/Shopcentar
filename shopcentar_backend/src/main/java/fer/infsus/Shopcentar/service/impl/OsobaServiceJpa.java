package fer.infsus.Shopcentar.service.impl;



import fer.infsus.Shopcentar.dao.OsobaRepository;
import fer.infsus.Shopcentar.domain.Osoba;
import fer.infsus.Shopcentar.service.OsobaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OsobaServiceJpa implements OsobaService {
    @Autowired
    private OsobaRepository osobaRepo;

    public Osoba dohvatiUpraviteljaPoEmailu(String email) {
        return osobaRepo.findByEmailOsobe(email)
                .orElseThrow(() -> new IllegalArgumentException("Upravitelj nije pronađen."));
    }

    @Override
    public List<Map<String, Object>> dohvatiUpravitelje() {
        List<Object[]> rezultati = osobaRepo.findByUloga("upravitelj");
        List<Map<String, Object>> upravitelji = new ArrayList<>();
        for (Object[] red : rezultati) {
            Map<String, Object> mapa = new HashMap<>();
            mapa.put("idOsobe", red[0]);
            mapa.put("emailOsobe", red[1]);
            upravitelji.add(mapa);
        }
        return upravitelji;
    }
}
