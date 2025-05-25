package fer.infsus.Shopcentar.service;

import fer.infsus.Shopcentar.dao.PromocijaRepository;
import fer.infsus.Shopcentar.domain.Osoba;
import fer.infsus.Shopcentar.domain.PromocijaTrgovine;
import fer.infsus.Shopcentar.domain.Trgovina;
import fer.infsus.Shopcentar.dto.PromocijaDTO;
import fer.infsus.Shopcentar.service.impl.PromocijaServiceJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class PromocijaServiceJpaTest {

    @InjectMocks
    private PromocijaServiceJpa service;

    @Mock
    private PromocijaRepository promocijaRepo;

    @Mock
    private OsobaService osobaService;

    @Mock
    private TrgovinaService trgovinaService;

    private Osoba upravitelj;
    private Trgovina trgovina;

    @BeforeEach
    void setUp(){
        upravitelj = new Osoba();
        upravitelj.setIdOsobe(1);
        upravitelj.setEmailOsobe("ana.admin@hedera.com");
        upravitelj.setPromocije(new HashSet<>());

        trgovina = new Trgovina();
        trgovina.setIdTrgovine(10);
        trgovina.setPromocije(new HashSet<>());

        lenient().when(osobaService.dohvatiUpraviteljaPoEmailu(anyString())).thenReturn(upravitelj);
        lenient().when(trgovinaService.findById2(10)).thenReturn(Optional.of(trgovina));

    }

    @Test
    void dohvatiPromocije_VraćaIspravanDTO() {
        PromocijaTrgovine prom = new PromocijaTrgovine();
        prom.setIdPromocije(5);
        prom.setNaslovPromocije("Popust");
        prom.setTekstPromocije("-10 %");
        prom.setSlikaPromocije("5.jpg");
        prom.setDatumPočetkaProm(LocalDate.of(2025, 6, 1));
        prom.setDatumKrajaProm(LocalDate.of(2025, 6, 30));
        prom.setObjavitelj(upravitelj);
        prom.setTrgovina(trgovina);

        when(promocijaRepo.findAllByIdtrgovine(10)).thenReturn(List.of(prom));

        List<PromocijaDTO> rezultat = service.dohvatiPromocije(10);

        assertEquals(1, rezultat.size());
        assertEquals("Popust", rezultat.get(0).getNaslovPromocije());
    }

    @Test
    void kreirajPromociju_Success() throws IOException {
        PromocijaDTO dto = new PromocijaDTO();
        dto.setNaslovPromocije("Akcija");
        dto.setTekstPromocije("2+1");
        dto.setIdTrgovine(10);
        dto.setDatumPočetkaProm(LocalDate.of(2025, 6, 1));
        dto.setDatumKrajaProm(LocalDate.of(2025, 6, 20));

        MockMultipartFile mockFile = new MockMultipartFile("file", "promo.jpg", "image/jpeg", "content".getBytes());

        when(promocijaRepo.save(any())).thenAnswer(inv -> {
            PromocijaTrgovine p = inv.getArgument(0);
            p.setIdPromocije(42);
            return p;
        });

        PromocijaDTO rezultat = service.kreirajPromociju(dto, mockFile);

        Path path = Path.of("slike","promocije","10");
        assertEquals(42, rezultat.getIdPromocije());
        verify(promocijaRepo, times(2)).save(any());
        assertTrue(Files.exists(path));
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
    void kreirajPromociju_BezSlike_BacaGresku() {
        PromocijaDTO dto = new PromocijaDTO();
        dto.setNaslovPromocije("Akcija");
        dto.setTekstPromocije("X");
        dto.setIdTrgovine(10);
        dto.setDatumPočetkaProm(LocalDate.of(2025, 6, 1));
        dto.setDatumKrajaProm(LocalDate.of(2025, 6, 20));

        MockMultipartFile prazan = new MockMultipartFile("file", new byte[0]);

        assertThrows(IllegalArgumentException.class,
                () -> service.kreirajPromociju(dto, prazan));
    }

    @Test
    void azurirajPromociju_DatumPočetkaNakonKraja_BacaGresku() {
        PromocijaTrgovine prom = new PromocijaTrgovine();
        prom.setIdPromocije(7);
        prom.setNaslovPromocije("Old");
        prom.setTekstPromocije("Old");
        prom.setTrgovina(trgovina);
        when(promocijaRepo.findById(7)).thenReturn(Optional.of(prom));

        PromocijaDTO dto = new PromocijaDTO();
        dto.setNaslovPromocije("Novi");
        dto.setTekstPromocije("TXT");
        dto.setDatumPočetkaProm(LocalDate.of(2025, 7, 10));
        dto.setDatumKrajaProm(LocalDate.of(2025, 7, 1));

        assertThrows(IllegalArgumentException.class,
                () -> service.azurirajPromociju(7, dto, null));
    }

    @Test
    void izbrisiPromociju_SlikaNePostoji_BacaGresku() {
        PromocijaTrgovine prom = new PromocijaTrgovine();
        prom.setIdPromocije(8);
        prom.setSlikaPromocije("8.jpg");
        prom.setTrgovina(trgovina);
        prom.setObjavitelj(upravitelj);

        when(promocijaRepo.findById(8)).thenReturn(Optional.of(prom));

        assertThrows(IllegalArgumentException.class, () -> service.izbrisiPromociju(8));
        verify(promocijaRepo, never()).delete(any());
    }
}
