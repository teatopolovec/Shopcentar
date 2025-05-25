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
                Map.of("idOsobe", "1", "emailOsobe", "ivan@example.com"),
                Map.of("idOSobe", "2", "emailOsobe", "ana@example.com")
        );

        Mockito.when(osobaService.dohvatiUpravitelje()).thenReturn(mockUpravitelji);

        mockMvc.perform(get("/osoba/upravitelji"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idOsobe").value("1"))
                .andExpect(jsonPath("$[0].emailOsobe").value("ivan@example.com"))
                .andExpect(jsonPath("$[1].idOsobe").value("2"));
    }
}

