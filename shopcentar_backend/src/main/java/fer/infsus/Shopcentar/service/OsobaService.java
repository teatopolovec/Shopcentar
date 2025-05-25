package fer.infsus.Shopcentar.service;

import fer.infsus.Shopcentar.domain.Osoba;

import java.util.List;
import java.util.Map;

public interface OsobaService {
    Osoba dohvatiUpraviteljaPoEmailu(String email);

    List<Map<String, Object>> dohvatiUpravitelje();
}
