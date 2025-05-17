package fer.infsus.Shopcentar.rest;

import fer.infsus.Shopcentar.dto.PromocijaDTO;
import fer.infsus.Shopcentar.service.PromocijaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping({"/promocija"})
public class PromocijaController {
    public PromocijaController(){}

    @Autowired
    private PromocijaService promocijaService;
    @GetMapping("/popis/{id}")
    public ResponseEntity<List<PromocijaDTO>> dohvatiPromocije(@PathVariable Integer id) {
        return ResponseEntity.ok(promocijaService.dohvatiPromocije(id));
    }

    @PostMapping(value = "/{id}",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> ažurirajPromociju(@PathVariable Integer id, @RequestPart("dto") PromocijaDTO dto, @RequestPart(value = "slika", required = false) MultipartFile slika) throws IOException {
        try {
            PromocijaDTO p = promocijaService.azurirajPromociju(id, dto, slika);
            return ResponseEntity.ok(p);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Greška: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Greška: " + e.getMessage());
        }
    }

    @PostMapping(value = "/",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> kreirajPromociju(@RequestPart("dto") PromocijaDTO dto, @RequestPart(value = "slika", required = false) MultipartFile slika) throws IOException {
        try {
            PromocijaDTO p = promocijaService.kreirajPromociju(dto, slika);
            return ResponseEntity.ok(p);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Greška: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Greška: " + e.getMessage());
        }
    }

    @DeleteMapping("/izbrisi/{id}")
    public ResponseEntity<?> obrisiPromociju(@PathVariable Integer id) {
        try {
            promocijaService.izbrisiPromociju(id);
            return ResponseEntity.status(HttpStatus.CREATED).body("Promocija je uspješno izbrisana.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Greška: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Greška: " + e.getMessage());
        }
    }
}
