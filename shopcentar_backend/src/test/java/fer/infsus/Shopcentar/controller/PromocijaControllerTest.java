package fer.infsus.Shopcentar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fer.infsus.Shopcentar.dto.PromocijaDTO;
import fer.infsus.Shopcentar.rest.PromocijaController;
import fer.infsus.Shopcentar.service.PromocijaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PromocijaController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class PromocijaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PromocijaService promocijaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void dohvatiPromocije_ReturnsList() throws Exception {
        PromocijaDTO dto = new PromocijaDTO();
        dto.setIdPromocije(1);
        dto.setNaslovPromocije("Akcija");

        Mockito.when(promocijaService.dohvatiPromocije(5)).thenReturn(List.of(dto));

        mockMvc.perform(get("/promocija/popis/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idPromocije").value(1));
    }

    @Test
    void kreirajPromociju_Success() throws Exception {
        PromocijaDTO dto = new PromocijaDTO();
        dto.setNaslovPromocije("Akcija");
        dto.setTekstPromocije("Popust");
        dto.setIdTrgovine(10);
        dto.setDatumPočetkaProm(LocalDate.now());
        dto.setDatumKrajaProm(LocalDate.now().plusDays(10));

        MockMultipartFile file = new MockMultipartFile("slika", "promo.jpg", "image/jpeg", "slika".getBytes());
        MockMultipartFile dtoPart = new MockMultipartFile("dto", "", "application/json", objectMapper.writeValueAsBytes(dto));

        dto.setIdPromocije(123);
        Mockito.when(promocijaService.kreirajPromociju(any(), any())).thenReturn(dto);

        mockMvc.perform(multipart("/promocija/")
                        .file(file)
                        .file(dtoPart)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPromocije").value(123));
    }

    @Test
    void azurirajPromociju_Success() throws Exception {
        PromocijaDTO dto = new PromocijaDTO();
        dto.setNaslovPromocije("Novo");
        dto.setTekstPromocije("Update");
        dto.setDatumPočetkaProm(LocalDate.now());
        dto.setDatumKrajaProm(LocalDate.now().plusDays(5));

        MockMultipartFile file = new MockMultipartFile("slika", "new.jpg", "image/jpeg", "update".getBytes());
        MockMultipartFile dtoPart = new MockMultipartFile("dto", "", "application/json", objectMapper.writeValueAsBytes(dto));

        dto.setIdPromocije(42);
        Mockito.when(promocijaService.azurirajPromociju(any(), any(), any())).thenReturn(dto);

        mockMvc.perform(multipart("/promocija/42")
                        .file(file)
                        .file(dtoPart)
                        .with(req -> {
                            req.setMethod("POST");
                            return req;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPromocije").value(42));
    }

    @Test
    void obrisiPromociju_Success() throws Exception {
        mockMvc.perform(delete("/promocija/izbrisi/55"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Promocija je uspješno izbrisana."));
    }

    @Test
    void obrisiPromociju_NotFound() throws Exception {
        Mockito.doThrow(new IllegalArgumentException("Ne postoji.")).when(promocijaService).izbrisiPromociju(77);

        mockMvc.perform(delete("/promocija/izbrisi/77"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Greška: Ne postoji."));
    }
}
