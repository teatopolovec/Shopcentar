package fer.infsus.Shopcentar.rest;

import fer.infsus.Shopcentar.domain.Etaza;
import fer.infsus.Shopcentar.dto.EtazaDTO;
import fer.infsus.Shopcentar.service.EtazaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping({"/etaza"})
public class EtazaController {

    @Autowired
    private EtazaService etazaService;
    public EtazaController(){
    }

    @GetMapping("/etaze")
    public ResponseEntity<List<Map<String, Object>>> dohvatiEtaze() {
        return ResponseEntity.ok(etazaService.dohvatiEtaze());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EtazaDTO> dohvatiEtazu(@PathVariable Integer id) {
        Optional<EtazaDTO> etaza = etazaService.findById(id);
        return etaza.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> kreirajEtazu(@RequestPart("etaza") EtazaDTO etaza) throws IOException {
        try {
            Etaza e = etazaService.kreirajEtazu(etaza);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "Etaza je uspješno kreirana.",
                    "id", e.getIdEtaze()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Greška: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Greška: " + e.getMessage());
        }
    }
    @PostMapping(value = "/{id}",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> ažurirajEtazu(@PathVariable Integer id, @RequestPart("etaza") EtazaDTO etaza) throws IOException {
        try {
            Etaza e = etazaService.azurirajEtazu(id, etaza);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "Etaža je uspješno ažurirana.",
                    "id", e.getIdEtaze()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Greška: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Greška: " + e.getMessage());
        }
    }

    @DeleteMapping("/izbrisi/{id}")
    public ResponseEntity<?> obrisiEtazu(@PathVariable Integer id) {
        etazaService.izbrisiEtazu(id);
        return ResponseEntity.status(HttpStatus.CREATED).body("Etaža je uspješno izbrisana.");
    }

}
