package fer.infsus.Shopcentar.rest;


import fer.infsus.Shopcentar.domain.Trgovina;
import fer.infsus.Shopcentar.dto.KategorijaDTO;
import fer.infsus.Shopcentar.dto.TrgovinaDTO;
import fer.infsus.Shopcentar.service.SlikaTrgovineService;
import fer.infsus.Shopcentar.service.TrgovinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping({"/trgovina"})
public class TrgovinaController {
    @Autowired
    private TrgovinaService trgovinaService;
    @Autowired
    private SlikaTrgovineService slikaTrgovineService;
    public TrgovinaController() {
    }

    @PostMapping(value = "/",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> kreirajTrgovinu(@RequestPart("dto") TrgovinaDTO dto, @RequestPart(value = "logo") MultipartFile logoFile, @RequestPart(value = "fotografije", required = false) List<MultipartFile> fotografije, @RequestPart(value = "kategorije") List<KategorijaDTO> kategorije) throws IOException {
        try {
            Trgovina t = trgovinaService.kreirajTrgovinu(dto, logoFile, kategorije);
            slikaTrgovineService.spremiSlike(fotografije, t);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "Trgovina je uspješno kreirana.",
                    "id", t.getIdTrgovine()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Greška: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Greška: " + e.getMessage());
        }
    }
    @PostMapping(value = "/{id}",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> ažurirajTrgovinu(@PathVariable Integer id, @RequestPart("dto") TrgovinaDTO dto, @RequestPart(value = "logo", required = false) MultipartFile logoFile, @RequestPart(value = "fotografije", required = false) List<MultipartFile> fotografije, @RequestPart("kategorije") List<KategorijaDTO> kategorije) throws IOException {
        try {
            Trgovina t = trgovinaService.azurirajTrgovinu(id, dto, logoFile, kategorije);
            slikaTrgovineService.spremiSlike(fotografije, t);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "Trgovina je uspješno ažurirana.",
                    "id", t.getIdTrgovine()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Greška: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Greška: " + e.getMessage());
        }
    }

    @GetMapping("/kategorije/{id}")
    public ResponseEntity<List<Map<String, Object>>> dohvatiKategorijeTrg(@PathVariable Integer id) {
        return ResponseEntity.ok(trgovinaService.dohvatiKategorijeTrg(id));
    }

    @GetMapping("/kategorije")
    public ResponseEntity<List<Map<String, Object>>> dohvatiKategorijeTrgNova() {
        return ResponseEntity.ok(new ArrayList<>());
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

    @DeleteMapping("/izbrisi/{id}")
    public ResponseEntity<?> obrisiTrgovinu(@PathVariable Integer id) {
        trgovinaService.izbrisiTrgovinu(id);
        return ResponseEntity.status(HttpStatus.CREATED).body("Trgovina je uspješno arhivirana.");
    }
}
