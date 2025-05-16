package fer.infsus.Shopcentar.service;



import fer.infsus.Shopcentar.domain.Trgovina;
import fer.infsus.Shopcentar.dto.TrgovinaDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface TrgovinaService {

    Trgovina kreirajTrgovinu(TrgovinaDTO dto);

    List<Map<String, Object>> dohvatiTrgovine();

    Optional<TrgovinaDTO> findById(Integer id);
}
