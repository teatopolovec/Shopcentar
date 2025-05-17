package fer.infsus.Shopcentar.service;

import fer.infsus.Shopcentar.domain.Trgovina;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SlikaTrgovineService {
    void spremiSlike(List<MultipartFile> fotografije, Trgovina t);

    List<String> dohvatiSlike(Integer id);

    void obrisiSliku(Integer id, String naziv) throws IOException;
}
