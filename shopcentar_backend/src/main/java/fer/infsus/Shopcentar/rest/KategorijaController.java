package fer.infsus.Shopcentar.rest;

import fer.infsus.Shopcentar.domain.Trgovina;
import fer.infsus.Shopcentar.dto.KategorijaDTO;
import fer.infsus.Shopcentar.dto.TrgovinaDTO;
import fer.infsus.Shopcentar.service.KategorijaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/kategorija"})
public class KategorijaController {
    @Autowired
    private KategorijaService kategorijaService;
    public KategorijaController() {
    }

    @GetMapping("/popis")
    public ResponseEntity<Map<Integer, String>> dohvatiKategorije() {
        return ResponseEntity.ok(kategorijaService.dohvatiKategorije());
    }

}
