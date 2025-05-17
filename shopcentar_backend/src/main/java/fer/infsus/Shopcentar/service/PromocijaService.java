package fer.infsus.Shopcentar.service;

import fer.infsus.Shopcentar.dto.PromocijaDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PromocijaService {
    List<PromocijaDTO> dohvatiPromocije(Integer id);

    PromocijaDTO azurirajPromociju(Integer id, PromocijaDTO dto, MultipartFile slika) throws IOException;
    PromocijaDTO kreirajPromociju(PromocijaDTO dto, MultipartFile slika);

    void izbrisiPromociju(Integer id) throws IOException;
}
