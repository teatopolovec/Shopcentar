package fer.infsus.Shopcentar.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fer.infsus.Shopcentar.dao.OsobaRepository;
import fer.infsus.Shopcentar.dao.TrgovinaRepository;
import fer.infsus.Shopcentar.domain.Osoba;
import fer.infsus.Shopcentar.domain.Trgovina;
import fer.infsus.Shopcentar.dto.PromocijaDTO;
import fer.infsus.Shopcentar.dto.TrgovinaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TrgovinaIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OsobaRepository osobaRepo;

    @Autowired
    private TrgovinaRepository trgovinaRepo;

    private Osoba upravitelj;

    private TrgovinaDTO dto;

    private MockMultipartFile logoFile;

    private MockMultipartFile dtoPart;
    private MockMultipartFile katPart;

    @Autowired
    private ObjectMapper mapper;


    @BeforeEach
    void setup() throws JsonProcessingException {
        upravitelj = new Osoba();
        upravitelj.setIdOsobe(1);
        upravitelj.setEmailOsobe("ana.admin@hedera.com");
        upravitelj.setPromocije(new HashSet<>());
        upravitelj.setIme("Ime");
        upravitelj.setPrezime("Prezime");
        upravitelj.setMobitel("0955555555");
        upravitelj.setUloga("upravitelj");
        upravitelj.setLozinka("loz");
        osobaRepo.save(upravitelj);

        dto = new TrgovinaDTO();
        dto.setNazivTrgovine("Test Trgovina");
        dto.setRadnoVrijeme("08:00-16:00");
        dto.setEmailTrgovine("test@example.com");
        dto.setTelefonTrgovine("0991234567");
        dto.setEmailUpravitelj("ana.admin@hedera.com");

        String dtoJson = objectMapper.writeValueAsString(dto);

        logoFile = new MockMultipartFile(
                "logo",
                "logo.png",
                "image/png",
                "dummy image content".getBytes(StandardCharsets.UTF_8)
        );

        dtoPart = new MockMultipartFile(
                "dto",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                dtoJson.getBytes(StandardCharsets.UTF_8)
        );

        katPart = new MockMultipartFile(
                "kategorije",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsString(new ArrayList<>()).getBytes(StandardCharsets.UTF_8)
        );

    }


    @Test
    void testKreirajDohvatiTrgovinu() throws Exception {

        var mvcResult = mockMvc.perform(multipart("/trgovina/")
                        .file(dtoPart)
                        .file(logoFile)
                        .file(katPart)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Trgovina je uspješno kreirana."))
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        var jsonNode = objectMapper.readTree(responseBody);
        Integer idTrgovine = jsonNode.get("id").asInt();

        mockMvc.perform(get("/trgovina/" + idTrgovine))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nazivTrgovine").value("Test Trgovina"))
                .andExpect(jsonPath("$.radnoVrijeme").value("08:00-16:00"))
                .andExpect(jsonPath("$.emailTrgovine").value("test@example.com"))
                .andExpect(jsonPath("$.telefonTrgovine").value("0991234567"))
                .andExpect(jsonPath("$.emailUpravitelj").value("ana.admin@hedera.com"));

        Path path = Path.of("slike","logo", "test trgovina_logo.png");
        if (Files.exists(path)) {
            Files.walk(path)
                    .filter(Files::isRegularFile)
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
            Files.deleteIfExists(path);
        }
    }

    @Test
    void testDodajAzurirajObrisiTrgovinu() throws Exception {
        var createRes = mockMvc.perform(multipart("/trgovina/")
                        .file(dtoPart)
                        .file(logoFile)
                        .file(katPart)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Trgovina je uspješno kreirana."))
                .andReturn();

        int id = mapper.readTree(createRes.getResponse().getContentAsString())
                .get("id").asInt();
        Optional<Trgovina> kreirana = trgovinaRepo.findById(id);
        assertThat(kreirana).isPresent();
        assertThat(kreirana.get().getNazivTrgovine()).isEqualTo("Test Trgovina");

        dto.setNazivTrgovine("IT-Dućan+");
        dto.setRadnoVrijeme("09:00-17:00");

        mockMvc.perform(multipart("/trgovina/{id}", id)
                            .file( new MockMultipartFile(
                                    "dto",
                                    "",
                                    MediaType.APPLICATION_JSON_VALUE,
                                    objectMapper.writeValueAsString(dto).getBytes(StandardCharsets.UTF_8)))
                            .file(katPart)
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.message").value("Trgovina je uspješno ažurirana."));

        Trgovina azurirana = trgovinaRepo.findById(id).orElseThrow();
        assertThat(azurirana.getNazivTrgovine()).isEqualTo("IT-Dućan+");
        assertThat(azurirana.getRadnoVrijeme()).isEqualTo("09:00-17:00");

        mockMvc.perform(delete("/trgovina/izbrisi/{id}", id))
                    .andExpect(status().isCreated())
                    .andExpect(content().string("Trgovina je uspješno arhivirana."));

        Trgovina arhivirana = trgovinaRepo.findById(id).orElseThrow();
        assertThat(arhivirana.getAktivna()).isFalse();
        Path path = Path.of("slike","logo", "it-dućan+_logo.png");
        if (Files.exists(path)) {
            Files.walk(path)
                    .filter(Files::isRegularFile)
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
            Files.deleteIfExists(path);
        }
    }
}
