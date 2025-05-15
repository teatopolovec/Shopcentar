package fer.infsus.Odbojka.service;

import fer.infsus.Odbojka.domain.Trgovina;
import fer.infsus.Odbojka.dto.TrgovinaDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface TrgovinaService {

    Trgovina kreirajTrgovinu(TrgovinaDTO dto);

    List<Map<String, Object>> dohvatiTrgovine();

    Optional<TrgovinaDTO> findById(Integer id);
}
