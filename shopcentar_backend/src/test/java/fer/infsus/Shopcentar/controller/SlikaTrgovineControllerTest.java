package fer.infsus.Shopcentar.controller;

import fer.infsus.Shopcentar.rest.SlikaTrgovineController;
import fer.infsus.Shopcentar.service.SlikaTrgovineService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.*;

@WebMvcTest(controllers = SlikaTrgovineController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class SlikaTrgovineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SlikaTrgovineService slikaTrgovineService;

    @Test
    void testDohvatiSlike_ReturnsListOfSlike() throws Exception {
        List<String> slike = Arrays.asList("slika1.jpg", "slika2.jpg");
        Mockito.when(slikaTrgovineService.dohvatiSlike(1)).thenReturn(slike);

        mockMvc.perform(get("/sliketrgovine/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]", is("slika1.jpg")))
                .andExpect(jsonPath("$[1]", is("slika2.jpg")));
    }

    @Test
    void testObrisiSliku_Success() throws Exception {
        Mockito.doNothing().when(slikaTrgovineService).obrisiSliku(1, "slika1.jpg");
        mockMvc.perform(delete("/sliketrgovine/1/slika1.jpg"))
                .andExpect(status().isOk())
                .andExpect(content().string("Slika obrisana."));
    }

    @Test
    void testObrisiSliku_IOException() throws Exception {
        Mockito.doThrow(new IllegalStateException("Datoteka nije pronađena"))
                .when(slikaTrgovineService).obrisiSliku(1, "nepostojeca.jpg");

        mockMvc.perform(delete("/sliketrgovine/1/nepostojeca.jpg"))
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("Nedosljednost u podacima: Datoteka nije pronađena")));
    }

    @Test
    void testObrisiSliku_BadRequest() throws Exception {
        Mockito.doThrow(new IllegalArgumentException("Slika nije pronađena."))
                .when(slikaTrgovineService).obrisiSliku(0, "slika.jpg");

        mockMvc.perform(delete("/sliketrgovine/0/slika.jpg"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Slika nije pronađena."));
    }
}
