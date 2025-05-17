package fer.infsus.Shopcentar.rest;

import fer.infsus.Shopcentar.service.KategorijaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
