package fer.infsus.Shopcentar.controller;

import fer.infsus.Shopcentar.rest.OsobaController;
import fer.infsus.Shopcentar.service.OsobaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OsobaController.class)
public class OsobaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OsobaService osobaService;

    @Test
    void testDohvatiUpravitelje() throws Exception {
        List<Map<String, Object>> mockUpravitelji = List.of(
                Map.of("id", 1, "ime", "Ivan", "prezime", "Ivić"),
                Map.of("id", 2, "ime", "Ana", "prezime", "Anić")
        );

        Mockito.when(osobaService.dohvatiUpravitelje()).thenReturn(mockUpravitelji);

        mockMvc.perform(get("/osoba/upravitelji"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].ime").value("Ivan"))
                .andExpect(jsonPath("$[1].prezime").value("Anić"));
    }
}

