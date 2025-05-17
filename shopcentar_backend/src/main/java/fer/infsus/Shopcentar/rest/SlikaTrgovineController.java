package fer.infsus.Shopcentar.rest;

import fer.infsus.Shopcentar.service.SlikaTrgovineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping({"/sliketrgovine"})
public class SlikaTrgovineController {
    @Autowired
    private SlikaTrgovineService slikaTrgovineService;
    public SlikaTrgovineController(){}

    @GetMapping("/{id}")
    public ResponseEntity<List<String>> dohvatiSlike(@PathVariable Integer id) {
        return ResponseEntity.ok(slikaTrgovineService.dohvatiSlike(id));
    }

    @DeleteMapping("/{id}/{nazivSlike}")
    public ResponseEntity<?> obrisiSliku(@PathVariable Integer id, @PathVariable String nazivSlike) {
        try {
            slikaTrgovineService.obrisiSliku(id, nazivSlike);
            return ResponseEntity.ok("Slika obrisana.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Greška pri brisanju slike: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
