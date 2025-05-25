package fer.infsus.Shopcentar.service;

import fer.infsus.Shopcentar.dao.TrgovinaRepository;
import fer.infsus.Shopcentar.domain.Osoba;
import fer.infsus.Shopcentar.domain.Trgovina;
import fer.infsus.Shopcentar.dto.TrgovinaDTO;
import fer.infsus.Shopcentar.service.impl.TrgovinaServiceJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrgovinaServiceJpaTest {

    @InjectMocks
    private TrgovinaServiceJpa service;

    @Mock
    private TrgovinaRepository trgovinaRepo;

    @Mock
    private OsobaService osobaService;

    private Osoba upravitelj;

    private Path path = Path.of("slike","logo", "techshop_logo.png");


    @BeforeEach
    void init(){
        upravitelj = new Osoba();
        upravitelj.setEmailOsobe("admin@test.hr");
        upravitelj.setTrgovine(new HashSet<>());

        lenient().when(osobaService.dohvatiUpraviteljaPoEmailu("admin@test.hr"))
                .thenReturn(upravitelj);
    }

    @Test
    void kreirajTrgovinu_Success() throws IOException {
        TrgovinaDTO dto = new TrgovinaDTO();
        dto.setNazivTrgovine("TechShop");
        dto.setRadnoVrijeme("08:00-16:00");
        dto.setTelefonTrgovine("0911234567");
        dto.setEmailTrgovine("shop@mail.hr");
        dto.setEmailUpravitelj("admin@test.hr");

        MockMultipartFile logo = new MockMultipartFile(
                "file", "logo.png", "image/png", "img".getBytes());

        when(trgovinaRepo.existsByNazivTrgovine("TechShop")).thenReturn(false);
        when(trgovinaRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Trgovina rezultat = service.kreirajTrgovinu(dto, logo, new ArrayList<>());

        assertEquals("techshop_logo.png", rezultat.getLogoTrgovine());
        assertTrue(Files.exists(path));

        verify(trgovinaRepo).save(any());

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
    void kreirajTrgovinu_DuplikatNaziva_BacaGresku() {
        TrgovinaDTO dto = new TrgovinaDTO();
        dto.setNazivTrgovine("Postoji");
        dto.setRadnoVrijeme("08:00-15:00");
        dto.setEmailUpravitelj("admin@test.hr");

        MockMultipartFile logo = new MockMultipartFile(
                "file", "x.jpg", "image/jpeg", "a".getBytes());

        when(trgovinaRepo.existsByNazivTrgovine("Postoji")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> service.kreirajTrgovinu(dto, logo, new ArrayList<>()));
        verify(trgovinaRepo, never()).save(any());
    }

    @Test
    void kreirajTrgovinu_BadWorkingHours() {
        TrgovinaDTO dto = new TrgovinaDTO();
        dto.setNazivTrgovine("X");
        dto.setRadnoVrijeme("25:00-02:00");
        dto.setEmailUpravitelj("admin@test.hr");

        MockMultipartFile logo = new MockMultipartFile(
                "file", "x.jpg", "image/jpeg", "a".getBytes());

        when(trgovinaRepo.existsByNazivTrgovine("X")).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> service.kreirajTrgovinu(dto, logo, new ArrayList<>()));
    }

    @Test
    void kreirajTrgovinu_LogoNijeSlika() {
        TrgovinaDTO dto = new TrgovinaDTO();
        dto.setNazivTrgovine("NoImg");
        dto.setRadnoVrijeme("07:00-13:00");
        dto.setEmailUpravitelj("admin@test.hr");

        MockMultipartFile txtFile =
                new MockMultipartFile("file", "doc.txt", "text/plain", "123".getBytes());

        when(trgovinaRepo.existsByNazivTrgovine("NoImg")).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> service.kreirajTrgovinu(dto, txtFile, new ArrayList<>()));
    }

    @Test
    void dohvatiTrgovine_VraćaListuMapa() {
        Object[] r1 = {1, "A"};
        Object[] r2 = {2, "B"};
        when(trgovinaRepo.findIdAndNaziv()).thenReturn(List.of(r1, r2));

        List<Map<String, Object>> rezultat = service.dohvatiTrgovine();

        assertEquals(2, rezultat.size());
        assertEquals(1, rezultat.get(0).get("idTrgovine"));
        assertEquals("B", rezultat.get(1).get("nazivTrgovine"));
    }

    @Test
    void azurirajTrgovinu_nazivVecPostoji_bacaException() {
        Integer id = 1;
        Trgovina stara = new Trgovina();
        stara.setNazivTrgovine("StariNaziv");
        when(trgovinaRepo.findById(id)).thenReturn(Optional.of(stara));
        when(trgovinaRepo.existsByNazivTrgovine("NoviNaziv")).thenReturn(true);

        TrgovinaDTO dto = new TrgovinaDTO();
        dto.setNazivTrgovine("NoviNaziv");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.azurirajTrgovinu(id, dto, null, new ArrayList<>()));
        assertEquals("Trgovina s tim nazivom već postoji.", ex.getMessage());
    }

    @Test
    void azurirajTrgovinu_nazivNullBacaException() {
        Integer id = 1;
        Trgovina stara = new Trgovina();
        stara.setNazivTrgovine("StariNaziv");
        when(trgovinaRepo.findById(id)).thenReturn(Optional.of(stara));
        when(trgovinaRepo.existsByNazivTrgovine(null)).thenReturn(false);

        TrgovinaDTO dto = new TrgovinaDTO();
        dto.setNazivTrgovine(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.azurirajTrgovinu(id, dto, null, new ArrayList<>()));
        assertEquals("Trgovina mora imati naziv.", ex.getMessage());
    }

    @Test
    void azurirajTrgovinu_mijenjaNazivIImeZaLogo() throws Exception {
        Integer id = 1;

        Trgovina stara = new Trgovina();
        stara.setNazivTrgovine("stariNaziv");
        stara.setLogoTrgovine("stari_logo.png");
        stara.setRadnoVrijeme("09:00-21:00");
        Osoba upraviteljStari = new Osoba();
        upraviteljStari.setEmailOsobe("stari@upravljatelj.com");
        stara.setUpravitelj(upraviteljStari);
        upraviteljStari.getTrgovine().add(stara);
        Path logoDir = Paths.get("slike/logo");
        if (!Files.exists(logoDir)) {
            Files.createDirectories(logoDir);
        }
        Path stariLogo = logoDir.resolve("stari_logo.png");
        if (!Files.exists(stariLogo)) {
            Files.createFile(stariLogo);
        }

        when(trgovinaRepo.findById(id)).thenReturn(Optional.of(stara));
        when(trgovinaRepo.existsByNazivTrgovine("noviNaziv")).thenReturn(false);

        TrgovinaDTO dto = new TrgovinaDTO();
        dto.setNazivTrgovine("noviNaziv");
        dto.setRadnoVrijeme("09:00-20:00");
        dto.setEmailTrgovine("email@trgovina.com");
        dto.setTelefonTrgovine("0912345678");
        dto.setEmailUpravitelj("novi@upravljatelj.com");

        MockMultipartFile logoFile = new MockMultipartFile("logoFile", "logo.jpg", "image/jpeg", "some-image-data".getBytes());

        when(trgovinaRepo.save(any())).thenAnswer(i -> i.getArguments()[0]);
        Osoba upravitelj2 = new Osoba();
        upravitelj2.setEmailOsobe("novi@upravljatelj.com");
        upravitelj2.setTrgovine(new HashSet<>());
        when(osobaService.dohvatiUpraviteljaPoEmailu("novi@upravljatelj.com")).thenReturn(upravitelj2);

        Trgovina rezultat = service.azurirajTrgovinu(id, dto, logoFile, new ArrayList<>());

        assertEquals("noviNaziv", rezultat.getNazivTrgovine());
        assertEquals("09:00-20:00", rezultat.getRadnoVrijeme());
        assertEquals("email@trgovina.com", rezultat.getEmailTrgovine());
        assertEquals("0912345678", rezultat.getTelefonTrgovine());
        assertTrue(rezultat.getLogoTrgovine().startsWith("novinaziv_logo"));
        assertEquals("novi@upravljatelj.com", rezultat.getUpravitelj().getEmailOsobe());
        Path novilogo = Paths.get("slike/logo/novinaziv_logo.jpg");
        Files.deleteIfExists(novilogo);
    }

    @Test
    void izbrisiTrgovinu_postojiPostavljaAktivnaFalse() {
        Integer id = 1;
        Trgovina trgovina = new Trgovina();
        trgovina.setAktivna(true);
        when(trgovinaRepo.findById(id)).thenReturn(Optional.of(trgovina));
        when(trgovinaRepo.save(any())).thenAnswer(i -> i.getArguments()[0]);

        service.izbrisiTrgovinu(id);

        assertFalse(trgovina.getAktivna());
        verify(trgovinaRepo).save(trgovina);
    }

    @Test
    void izbrisiTrgovinu_nePostojiBacaException() {
        Integer id = 999;
        when(trgovinaRepo.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.izbrisiTrgovinu(id));
        assertEquals("Trgovina s ID 999 nije pronađena.", ex.getMessage());
    }
}
