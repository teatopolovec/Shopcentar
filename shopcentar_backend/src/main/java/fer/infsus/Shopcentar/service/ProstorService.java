package fer.infsus.Shopcentar.service;

import fer.infsus.Shopcentar.domain.Etaza;
import fer.infsus.Shopcentar.dto.EtazaDTO;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProstorService {
    List<Map<String, Object>> dohvatiProstore();

    List<Map<String, Object>> dohvatiEtaze();

    Etaza kreirajEtazu(EtazaDTO etaza) throws IOException;;

    Etaza azurirajEtazu(Integer id, EtazaDTO etaza);

    public void izbrisiEtazu(Integer id);

    Optional<EtazaDTO> findById(Integer id);
}
