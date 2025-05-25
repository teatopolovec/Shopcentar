package fer.infsus.Shopcentar.integration;

import fer.infsus.Shopcentar.domain.Osoba;
import fer.infsus.Shopcentar.dao.OsobaRepository;
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
public class OsobaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OsobaRepository osobaRepo;

    @BeforeEach
    public void setUp() {
        osobaRepo.deleteAll();

        Osoba ivan = new Osoba();
        ivan.setIme("Ivan");
        ivan.setPrezime("Ivić");
        ivan.setEmailOsobe("ivan@example.com");
        ivan.setLozinka("lozinka1");
        ivan.setMobitel("0911111111");
        ivan.setUloga("upravitelj");
        osobaRepo.save(ivan);

        Osoba ana = new Osoba();
        ana.setIme("Ana");
        ana.setPrezime("Anić");
        ana.setEmailOsobe("ana@example.com");
        ana.setLozinka("lozinka2");
        ana.setMobitel("0922222222");
        ana.setUloga("upravitelj");
        osobaRepo.save(ana);

        Osoba marko = new Osoba();
        marko.setIme("Marko");
        marko.setPrezime("Marić");
        marko.setEmailOsobe("marko@example.com");
        marko.setLozinka("lozinka3");
        marko.setMobitel("0933333333");
        marko.setUloga("admin");
        osobaRepo.save(marko);
    }

    @Test
    public void testDohvatiUpravitelje() throws Exception {
        mockMvc.perform(get("/osoba/upravitelji")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].emailOsobe", containsInAnyOrder("ivan@example.com", "ana@example.com")))
                .andExpect(jsonPath("$[*].uloga", everyItem(is("upravitelj"))));
    }

    @Test
    public void testDohvatiUpraviteljeEmpty() throws Exception {
        osobaRepo.deleteAll();

        mockMvc.perform(get("/osoba/upravitelji"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
