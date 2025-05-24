package fer.infsus.Shopcentar.integration;

import fer.infsus.Shopcentar.dao.OsobaRepository;
import fer.infsus.Shopcentar.dao.SlikaTrgovineRepository;
import fer.infsus.Shopcentar.dao.TrgovinaRepository;
import fer.infsus.Shopcentar.domain.Osoba;
import fer.infsus.Shopcentar.domain.SlikaTrgovine;
import fer.infsus.Shopcentar.domain.Trgovina;
import fer.infsus.Shopcentar.service.SlikaTrgovineService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SlikaTrgovineIntegrationTest {

    @Autowired
    private SlikaTrgovineService slikaTrgovineService;

    @Autowired
    private SlikaTrgovineRepository slikaRepo;

    @Autowired
    private TrgovinaRepository trgovinaRepo;

    @Autowired
    private OsobaRepository osobaRepo;

    @Autowired
    private MockMvc mockMvc;

    private Trgovina testTrgovina;

    @BeforeEach
    void setUp() {
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

        testTrgovina = new Trgovina();
        testTrgovina.setSlike(new HashSet<>());
        testTrgovina.setNazivTrgovine("xy");
        testTrgovina.setRadnoVrijeme("09:00-21:00");
        testTrgovina.setUpravitelj(upravitelj);
        testTrgovina.setLogoTrgovine("logo.png");
        trgovinaRepo.save(testTrgovina);
    }

    @Test
    void testSpremiDohvatiObrisiSlikuPutemKontrolera() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile(
                "fotografije",
                "testslika.jpg",
                "image/jpeg",
                "dummy image content".getBytes()
        );

        slikaTrgovineService.spremiSlike(List.of(multipartFile), testTrgovina);

        String responseJson = mockMvc.perform(get("/sliketrgovine/{id}", testTrgovina.getIdTrgovine())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        List<String> nazivi = mapper.readValue(responseJson, List.class);
        assertThat(nazivi).isNotEmpty();

        String nazivSlike = nazivi.get(0);

        Optional<SlikaTrgovine> slikaIzBaze = slikaRepo.findByNazivSlikeTrgovineAndIdTrgovine(nazivSlike, testTrgovina.getIdTrgovine());
        assertThat(slikaIzBaze).isPresent();

        Path putanja = Path.of("slike", String.valueOf(testTrgovina.getIdTrgovine()), nazivSlike);
        assertThat(Files.exists(putanja)).isTrue();

        mockMvc.perform(delete("/sliketrgovine/{id}/{nazivSlike}", testTrgovina.getIdTrgovine(), nazivSlike))
                .andExpect(status().isOk())
                .andExpect(content().string("Slika obrisana."));

        Optional<SlikaTrgovine> slikaNakonBrisanja = slikaRepo.findByNazivSlikeTrgovineAndIdTrgovine(nazivSlike, testTrgovina.getIdTrgovine());
        assertThat(slikaNakonBrisanja).isEmpty();

        assertThat(Files.exists(putanja)).isFalse();
    }

}
