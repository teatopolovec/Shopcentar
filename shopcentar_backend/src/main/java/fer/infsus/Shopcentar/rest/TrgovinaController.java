package fer.infsus.Shopcentar.rest;


import fer.infsus.Shopcentar.dto.TrgovinaDTO;
import fer.infsus.Shopcentar.service.TrgovinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping({"/trgovina"})
public class TrgovinaController {
    @Autowired
    private TrgovinaService trgovinaService;
    public TrgovinaController() {
    }

    @PostMapping({"/"})
    public ResponseEntity<?> kreirajTrgovinu(@RequestBody TrgovinaDTO dto) {
        trgovinaService.kreirajTrgovinu(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Trgovina je uspješno kreirana.");
    }

    @GetMapping("/popis")
    public ResponseEntity<List<Map<String, Object>>> dohvatiTrgovine() {
        return ResponseEntity.ok(trgovinaService.dohvatiTrgovine());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrgovinaDTO> dohvatiTrgovinu(@PathVariable Integer id) {
        Optional<TrgovinaDTO> trgovina = trgovinaService.findById(id);
        return trgovina.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
