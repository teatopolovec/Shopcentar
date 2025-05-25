package fer.infsus.Shopcentar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fer.infsus.Shopcentar.rest.KategorijaController;
import fer.infsus.Shopcentar.service.KategorijaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = KategorijaController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class KategorijaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KategorijaService kategorijaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testDohvatiKategorije() throws Exception {
        Map<Integer, String> kategorije = Map.of(1, "Kategorija 1", 2, "Kategorija 2", 3, "Kategorija 3");

        Mockito.when(kategorijaService.dohvatiKategorije()).thenReturn(kategorije);

        mockMvc.perform(get("/kategorija/popis"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.1").value("Kategorija 1"))
                .andExpect(jsonPath("$.2").value("Kategorija 2"))
                .andExpect(jsonPath("$.3").value("Kategorija 3"));
    }
}
