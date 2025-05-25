package fer.infsus.Shopcentar.service;



import fer.infsus.Shopcentar.domain.Trgovina;
import fer.infsus.Shopcentar.dto.KategorijaDTO;
import fer.infsus.Shopcentar.dto.TrgovinaDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface TrgovinaService {

    Trgovina kreirajTrgovinu(TrgovinaDTO dto, MultipartFile logoFile, List<KategorijaDTO> kategorije) throws IOException;

    List<Map<String, Object>> dohvatiTrgovine();

    List<Map<String, Object>> dohvatiKategorijeTrg(Integer id);

    Optional<TrgovinaDTO> findById(Integer id);
    Optional<Trgovina> findById2(Integer id);

    void azurirajKategorije(Integer id, List<KategorijaDTO> dto);

    Trgovina azurirajTrgovinu(Integer id, TrgovinaDTO dto, MultipartFile logoFile, List<KategorijaDTO> kategorije);

    void izbrisiTrgovinu(Integer id);
}
