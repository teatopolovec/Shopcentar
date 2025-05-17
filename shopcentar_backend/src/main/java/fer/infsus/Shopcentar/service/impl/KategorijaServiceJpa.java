package fer.infsus.Shopcentar.service.impl;

import fer.infsus.Shopcentar.dao.KategorijaRepository;
import fer.infsus.Shopcentar.domain.Kategorija;
import fer.infsus.Shopcentar.service.KategorijaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KategorijaServiceJpa implements KategorijaService {
    @Autowired
    private KategorijaRepository kategorijaRepo;
    @Override
    public Map<Integer, String> dohvatiKategorije() {
        List<Kategorija> rezultati = kategorijaRepo.findAll();
        Map<Integer, String> mapa = new HashMap<>();
        for (Kategorija k : rezultati) {
            mapa.put(k.getIdKategorije(), k.getNazivKategorije());
        }
        return mapa;
    }
}
