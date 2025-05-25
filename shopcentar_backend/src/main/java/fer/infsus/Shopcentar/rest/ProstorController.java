package fer.infsus.Shopcentar.rest;

import fer.infsus.Shopcentar.domain.Etaza;
import fer.infsus.Shopcentar.domain.Trgovina;
import fer.infsus.Shopcentar.dto.EtazaDTO;
import fer.infsus.Shopcentar.dto.KategorijaDTO;
import fer.infsus.Shopcentar.dto.TrgovinaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import fer.infsus.Shopcentar.service.ProstorService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping({"/prostor"})
public class ProstorController {
    @Autowired
    private ProstorService prostorService;

    public ProstorController(){
    }

    @GetMapping("/popis")
    public ResponseEntity<List<Map<String, Object>>> dohvatiTrgovine() {
        return ResponseEntity.ok(prostorService.dohvatiProstore());
    }

    @GetMapping("/etaze")
    public ResponseEntity<List<Map<String, Object>>> dohvatiEtaze() {
        return ResponseEntity.ok(prostorService.dohvatiEtaze());
    }

    @GetMapping("/etaza/{id}")
    public ResponseEntity<EtazaDTO> dohvatiEtazu(@PathVariable Integer id) {
        Optional<EtazaDTO> etaza = prostorService.findById(id);
        return etaza.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/etaza/",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> kreirajEtazu(@RequestPart("etaza") EtazaDTO etaza) throws IOException {
        try {
            Etaza e = prostorService.kreirajEtazu(etaza);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "Etaza je uspješno kreirana.",
                    "id", e.getIdEtaze()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Greška: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Greška: " + e.getMessage());
        }
    }
    @PostMapping(value = "/etaza/{id}",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> ažurirajEtazu(@PathVariable Integer id, @RequestPart("etaza") EtazaDTO etaza) throws IOException {
        try {
            Etaza e = prostorService.azurirajEtazu(id, etaza);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "Etaža je uspješno ažurirana.",
                    "id", e.getIdEtaze()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Greška: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Greška: " + e.getMessage());
        }
    }

    @DeleteMapping("/etaza/izbrisi/{id}")
    public ResponseEntity<?> obrisiEtazu(@PathVariable Integer id) {
        prostorService.izbrisiEtazu(id);
        return ResponseEntity.status(HttpStatus.CREATED).body("Etaža je uspješno izbrisana.");
    }

}
