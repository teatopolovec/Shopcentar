package fer.infsus.Shopcentar.service;



import fer.infsus.Shopcentar.domain.Trgovina;
import fer.infsus.Shopcentar.dto.TrgovinaDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface TrgovinaService {

    Trgovina kreirajTrgovinu(TrgovinaDTO dto, MultipartFile logoFile) throws IOException;

    List<Map<String, Object>> dohvatiTrgovine();

    Optional<TrgovinaDTO> findById(Integer id);
    Optional<Trgovina> findById2(Integer id);

    Trgovina azurirajTrgovinu(Integer id, TrgovinaDTO dto, MultipartFile logoFile);

    void izbrisiTrgovinu(Integer id);
}
