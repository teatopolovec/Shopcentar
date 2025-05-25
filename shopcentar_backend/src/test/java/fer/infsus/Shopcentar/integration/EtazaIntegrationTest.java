package fer.infsus.Shopcentar.integration;

        import fer.infsus.Shopcentar.domain.Etaza;
        import fer.infsus.Shopcentar.dao.EtazaRepository;
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
public class EtazaIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EtazaRepository etazaRepository;

    @BeforeEach
    public void setUp() {
        etazaRepository.deleteAll();

        Etaza e1 = new Etaza();
        e1.setBrojEtaze(0);
        e1.setOpis("Prizemlje");
        etazaRepository.save(e1);

        Etaza e2 = new Etaza();
        e2.setBrojEtaze(1);
        e2.setOpis("Prvi kat");
        etazaRepository.save(e2);
    }

    @Test
    public void testDohvatiEtaze() throws Exception {
        mockMvc.perform(get("/etaza/etaze")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].brojEtaze", containsInAnyOrder(0, 1)))
                .andExpect(jsonPath("$[*].opis", containsInAnyOrder("Prizemlje", "Prvi kat")));
    }

    @Test
    public void testDohvatiEtazeEmpty() throws Exception {
        etazaRepository.deleteAll();

        mockMvc.perform(get("/etaza/etaze"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
