package fer.infsus.Shopcentar.rest;

import fer.infsus.Shopcentar.service.OsobaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/osoba"})
public class OsobaController {
    @Autowired
    private OsobaService osobaService;
    public OsobaController() {
    }

    @GetMapping("/upravitelji")
    public ResponseEntity<List<Map<String, Object>>> dohvatiUpravitelje() {
        return ResponseEntity.ok(osobaService.dohvatiUpravitelje());
    }
}
