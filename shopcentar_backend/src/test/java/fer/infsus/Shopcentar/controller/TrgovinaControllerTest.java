package fer.infsus.Shopcentar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fer.infsus.Shopcentar.domain.Trgovina;
import fer.infsus.Shopcentar.dto.TrgovinaDTO;
import fer.infsus.Shopcentar.rest.TrgovinaController;
import fer.infsus.Shopcentar.service.SlikaTrgovineService;
import fer.infsus.Shopcentar.service.TrgovinaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TrgovinaController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class TrgovinaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrgovinaService trgovinaService;

    @MockBean
    private SlikaTrgovineService slikaTrgovineService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testKreirajTrgovinu() throws Exception {
        TrgovinaDTO dto = new TrgovinaDTO();
        dto.setNazivTrgovine("Nova trgovina");

        MockMultipartFile dtoPart = new MockMultipartFile(
                "dto", "",
                "application/json", objectMapper.writeValueAsBytes(dto));
        MockMultipartFile logoFile = new MockMultipartFile(
                "logo", "logo.png",
                MediaType.IMAGE_PNG_VALUE, "fake-image-content".getBytes());

        Trgovina trgovina = new Trgovina();
        trgovina.setIdTrgovine(1);

        Mockito.when(trgovinaService.kreirajTrgovinu(any(), any())).thenReturn(trgovina);

        mockMvc.perform(multipart("/trgovina/")
                        .file(dtoPart)
                        .file(logoFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Trgovina je uspješno kreirana."))
                .andExpect(jsonPath("$.id").value(1));

        Mockito.verify(slikaTrgovineService).spremiSlike(null, trgovina);
    }

    @Test
    void testAzurirajTrgovinu() throws Exception {
        int id = 1;
        TrgovinaDTO dto = new TrgovinaDTO();
        dto.setNazivTrgovine("Izmijenjena trgovina");

        MockMultipartFile dtoPart = new MockMultipartFile(
                "dto", "",
                "application/json", objectMapper.writeValueAsBytes(dto));
        MockMultipartFile logoFile = new MockMultipartFile(
                "logo", "logo2.png",
                MediaType.IMAGE_PNG_VALUE, "fake-logo-content".getBytes());

        Trgovina trgovina = new Trgovina();
        trgovina.setIdTrgovine(id);

        Mockito.when(trgovinaService.azurirajTrgovinu(eq(id), any(), any())).thenReturn(trgovina);

        mockMvc.perform(multipart("/trgovina/{id}", id)
                        .file(dtoPart)
                        .file(logoFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Trgovina je uspješno ažurirana."))
                .andExpect(jsonPath("$.id").value(id));

        Mockito.verify(slikaTrgovineService).spremiSlike(null, trgovina);
    }

    @Test
    void testDohvatiTrgovine() throws Exception {
        List<Map<String, Object>> trgovine = List.of(
                Map.of("id", 1, "naziv", "Trgovina 1"),
                Map.of("id", 2, "naziv", "Trgovina 2"));

        Mockito.when(trgovinaService.dohvatiTrgovine()).thenReturn(trgovine);

        mockMvc.perform(get("/trgovina/popis"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].naziv").value("Trgovina 1"))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void testDohvatiTrgovinuPoId() throws Exception {
        int id = 1;
        TrgovinaDTO dto = new TrgovinaDTO();
        dto.setNazivTrgovine("Test trgovina");

        Mockito.when(trgovinaService.findById(id)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/trgovina/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nazivTrgovine").value("Test trgovina"));
    }

    @Test
    void testDohvatiTrgovinuPoId_NijePronadeno() throws Exception {
        int id = 999;
        Mockito.when(trgovinaService.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/trgovina/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void testObrisiTrgovinu() throws Exception {
        int id = 1;

        mockMvc.perform(delete("/trgovina/izbrisi/{id}", id))
                .andExpect(status().isCreated())
                .andExpect(content().string("Trgovina je uspješno arhivirana."));

        Mockito.verify(trgovinaService).izbrisiTrgovinu(id);
    }
}
