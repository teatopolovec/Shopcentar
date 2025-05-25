package fer.infsus.Shopcentar.integration;

import fer.infsus.Shopcentar.domain.Kategorija;
import fer.infsus.Shopcentar.dao.KategorijaRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class KategorijaIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private KategorijaRepository kategorijaRepo;

    @BeforeEach
    public void setUp() {
        kategorijaRepo.deleteAll();

        Kategorija k1 = new Kategorija();
        k1.setNazivKategorije("Odjeća");
        kategorijaRepo.save(k1);

        Kategorija k2 = new Kategorija();
        k2.setNazivKategorije("Elektronika");
        kategorijaRepo.save(k2);
    }

    @Test
    public void testDohvatiKategorije() throws Exception {
        mockMvc.perform(get("/kategorija/popis")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", aMapWithSize(2)))
                .andExpect(jsonPath("$.*", hasItems("Odjeća", "Elektronika")));
    }

    @Test
    public void testDohvatiKategorijeEmpty() throws Exception {
        kategorijaRepo.deleteAll();

        mockMvc.perform(get("/kategorija/popis"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", anEmptyMap()));
    }
}
