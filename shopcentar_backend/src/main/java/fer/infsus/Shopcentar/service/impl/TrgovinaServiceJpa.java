package fer.infsus.Shopcentar.service.impl;


import fer.infsus.Shopcentar.dao.TrgovinaRepository;
import fer.infsus.Shopcentar.domain.Osoba;
import fer.infsus.Shopcentar.domain.Trgovina;
import fer.infsus.Shopcentar.dto.TrgovinaDTO;
import fer.infsus.Shopcentar.service.OsobaService;
import fer.infsus.Shopcentar.service.TrgovinaService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TrgovinaServiceJpa implements TrgovinaService {
    @Autowired
    private TrgovinaRepository trgovinaRepo;
    @Autowired
    private OsobaService osobaService;
    @Transactional
    public Trgovina kreirajTrgovinu(TrgovinaDTO dto) {
        Osoba upravitelj = osobaService.dohvatiUpraviteljaPoEmailu(dto.getEmailUpravitelj());

        Trgovina trgovina = new Trgovina();
        trgovina.setNazivTrgovine(dto.getNazivTrgovine());
        trgovina.setRadnoVrijeme(dto.getRadnoVrijeme());
        trgovina.setLogoTrgovine(dto.getLogoTrgovine());
        trgovina.setTelefonTrgovine(dto.getTelefonTrgovine());
        trgovina.setEmailTrgovine(dto.getEmailTrgovine());
        trgovina.setUpravitelj(upravitelj);

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
}
