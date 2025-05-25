package fer.infsus.Shopcentar.service;

import fer.infsus.Shopcentar.domain.Kategorija;
import fer.infsus.Shopcentar.domain.Trgovina;
import fer.infsus.Shopcentar.dto.KategorijaDTO;
import fer.infsus.Shopcentar.dto.TrgovinaDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface KategorijaService {
    Map<Integer, String> dohvatiKategorije();


    Optional<Kategorija> findById(Integer id);

}
