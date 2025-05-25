package fer.infsus.Shopcentar.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import fer.infsus.Shopcentar.dao.OsobaRepository;
import fer.infsus.Shopcentar.dao.TrgovinaRepository;
import fer.infsus.Shopcentar.domain.Osoba;
import fer.infsus.Shopcentar.domain.Trgovina;
import fer.infsus.Shopcentar.dto.PromocijaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PromocijaIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private PromocijaDTO testPromocija;

    @Autowired
    private OsobaRepository osobaRepo;

    @Autowired
    private TrgovinaRepository trgovinaRepo;

    @BeforeEach
    void setup() {
        Osoba upravitelj = new Osoba();
        upravitelj.setIdOsobe(1);
        upravitelj.setEmailOsobe("ana.admin@hedera.com");
        upravitelj.setPromocije(new HashSet<>());
        upravitelj.setIme("Ime");
        upravitelj.setPrezime("Prezime");
        upravitelj.setMobitel("0955555555");
        upravitelj.setUloga("upravitelj");
        upravitelj.setLozinka("loz");
        osobaRepo.save(upravitelj);

        Trgovina testTrgovina = new Trgovina();
        testTrgovina.setSlike(new HashSet<>());
        testTrgovina.setNazivTrgovine("xy");
        testTrgovina.setRadnoVrijeme("09:00-21:00");
        testTrgovina.setUpravitelj(upravitelj);
        testTrgovina.setLogoTrgovine("logo.png");
        trgovinaRepo.save(testTrgovina);

        testPromocija = new PromocijaDTO();
        testPromocija.setNaslovPromocije("Test naslov");
        testPromocija.setTekstPromocije("Testni tekst promocije");
        testPromocija.setDatumPočetkaProm(LocalDate.now());
        testPromocija.setDatumKrajaProm(LocalDate.now().plusDays(5));
        testPromocija.setIdTrgovine(testTrgovina.getIdTrgovine());
    }

    @Test
    void testKreirajDohvatiIzbrisiPromociju() throws Exception {
        MockMultipartFile slika = new MockMultipartFile(
                "slika",
                "testslika.jpg",
                "image/jpeg",
                "dummy image content".getBytes()
        );

        MockMultipartFile dto = new MockMultipartFile(
                "dto",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(testPromocija)
        );

        MvcResult createResult = mockMvc.perform(multipart("/promocija/")
                        .file(dto)
                        .file(slika)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = createResult.getResponse().getContentAsString();
        PromocijaDTO kreiranaPromocija = objectMapper.readValue(jsonResponse, PromocijaDTO.class);
        assertThat(kreiranaPromocija.getIdPromocije()).isNotNull();
        Integer idPromocije = kreiranaPromocija.getIdPromocije();

        mockMvc.perform(get("/promocija/popis/{id}", testPromocija.getIdTrgovine()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].naslovPromocije").value("Test naslov"));

        testPromocija.setNaslovPromocije("Ažurirani naslov");

        MockMultipartFile dtoUpdate = new MockMultipartFile(
                "dto",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(testPromocija)
        );

        mockMvc.perform(multipart("/promocija/{id}", idPromocije)
                        .file(dtoUpdate)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> { request.setMethod("POST"); return request; }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.naslovPromocije").value("Ažurirani naslov"));

        mockMvc.perform(delete("/promocija/izbrisi/{id}", idPromocije))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/promocija/popis/{id}", testPromocija.getIdTrgovine()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}
