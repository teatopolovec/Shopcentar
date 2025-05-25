package fer.infsus.Shopcentar.service.impl;

import fer.infsus.Shopcentar.dao.KategorijaRepository;
import fer.infsus.Shopcentar.domain.Kategorija;
import fer.infsus.Shopcentar.domain.Trgovina;
import fer.infsus.Shopcentar.dto.KategorijaDTO;
import fer.infsus.Shopcentar.dto.TrgovinaDTO;
import fer.infsus.Shopcentar.service.KategorijaService;
import fer.infsus.Shopcentar.service.TrgovinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

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

    @Override
    public Optional<Kategorija> findById(Integer id) {
        return kategorijaRepo.findById(id);
    }

}
