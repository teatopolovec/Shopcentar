package fer.infsus.Shopcentar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fer.infsus.Shopcentar.domain.Etaza;
import fer.infsus.Shopcentar.dto.EtazaDTO;
import fer.infsus.Shopcentar.rest.EtazaController;
import fer.infsus.Shopcentar.service.EtazaService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers =  EtazaController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class EtazaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EtazaService etazaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testDohvatiEtaze() throws Exception {
        List<Map<String, Object>> mockEtaze = List.of(
                Map.of("id", 1, "naziv", "Prizemlje"),
                Map.of("id", 2, "naziv", "Kat 1")
        );

        Mockito.when(etazaService.dohvatiEtaze()).thenReturn(mockEtaze);

        mockMvc.perform(get("/etaza/etaze"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].naziv").value("Kat 1"));
    }

    @Test
    void testDohvatiEtazu() throws Exception {
        int id = 1;
        EtazaDTO etazaDTO = new EtazaDTO();
        etazaDTO.setOpis("Prizemlje");

        Mockito.when(etazaService.findById(1)).thenReturn(Optional.of(etazaDTO));

        mockMvc.perform(get("/etaza/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.opis").value("Prizemlje"));
    }

    @Test
    void testDohvatiEtazu_NotFound() throws Exception {
        Mockito.when(etazaService.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/etaza/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testKreirajEtazu() throws Exception {
        EtazaDTO etazaDTO = new EtazaDTO();
        etazaDTO.setOpis("Kat 2");

        Etaza createdEtaza = new Etaza();
        createdEtaza.setIdEtaze(10);

        Mockito.when(etazaService.kreirajEtazu(any(EtazaDTO.class))).thenReturn(createdEtaza);

        MockMultipartFile etazaPart = new MockMultipartFile(
                "etaza", "", "application/json", objectMapper.writeValueAsBytes(etazaDTO)
        );

        mockMvc.perform(multipart("/etaza/")
                        .file(etazaPart)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    void testAzurirajEtazu() throws Exception {
        EtazaDTO etazaDTO = new EtazaDTO();
        etazaDTO.setOpis("Novi kat");

        Etaza updatedEtaza = new Etaza();
        updatedEtaza.setIdEtaze(5);

        Mockito.when(etazaService.azurirajEtazu(eq(5), any(EtazaDTO.class))).thenReturn(updatedEtaza);

        MockMultipartFile etazaPart = new MockMultipartFile(
                "etaza", "", "application/json", objectMapper.writeValueAsBytes(etazaDTO)
        );

        mockMvc.perform(multipart("/etaza/5")
                        .file(etazaPart)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(req -> { req.setMethod("POST"); return req; }))  // multipart defaults to POST
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5));
    }

    @Test
    void testObrisiEtazu() throws Exception {
        doNothing().when(etazaService).izbrisiEtazu(3);

        mockMvc.perform(delete("/etaza/izbrisi/3"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Etaža je uspješno izbrisana."));
    }
}
