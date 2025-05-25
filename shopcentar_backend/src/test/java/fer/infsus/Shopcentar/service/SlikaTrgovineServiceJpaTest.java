package fer.infsus.Shopcentar.service;

import fer.infsus.Shopcentar.dao.SlikaTrgovineRepository;
import fer.infsus.Shopcentar.domain.SlikaTrgovine;
import fer.infsus.Shopcentar.domain.Trgovina;
import fer.infsus.Shopcentar.service.impl.SlikaTrgovineServiceJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SlikaTrgovineServiceJpaTest {

    @InjectMocks
    private SlikaTrgovineServiceJpa service;

    @Mock
    private SlikaTrgovineRepository slikaRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSpremiSlike_NullOrEmptyList_DoesNothing() {
        Trgovina t = new Trgovina();
        service.spremiSlike(null, t);
        service.spremiSlike(Collections.emptyList(), t);
        verifyNoInteractions(slikaRepo);
    }

    @Test
    void testSpremiSlike_SavesImage() throws IOException {
        Trgovina t = new Trgovina();
        t.setIdTrgovine(1);
        t.setSlike(new HashSet<>());

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "blabla".getBytes()
        );

        service.spremiSlike(List.of(file), t);

        verify(slikaRepo, atLeastOnce()).save(any(SlikaTrgovine.class));

        assertFalse(t.getSlike().isEmpty());

        Path path = Path.of("slike", "1");
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
    void testSpremiSlike_InvalidFileType_DoesNotSave() throws IOException {
        Trgovina t = new Trgovina();
        t.setIdTrgovine(1);
        t.setSlike(new HashSet<>());

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "ovo nije slika".getBytes()
        );

        service.spremiSlike(List.of(file), t);

        verify(slikaRepo, never()).save(any());

        assertTrue(t.getSlike().isEmpty());

        Path path = Path.of("slike", "1");
        assertFalse(Files.exists(path));

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
    void testSpremiSlike_TooLargeImage_DoesNotSave() throws IOException {
        Trgovina t = new Trgovina();
        t.setIdTrgovine(1);
        t.setSlike(new HashSet<>());

        byte[] x = new byte[6 * 1024 * 1024]; // 6 MB
        new Random().nextBytes(x);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "large.jpg",
                "image/jpeg",
                x
        );

        Path path = Paths.get("slike", "1");

        service.spremiSlike(List.of(file), t);

        verify(slikaRepo, never()).save(any());
        assertTrue(t.getSlike().isEmpty());
        assertFalse(Files.exists(path));

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
    void testSpremiSlike_MixedFiles_SavesOnlyValid() throws IOException {
        Trgovina t = new Trgovina();
        t.setIdTrgovine(1);
        t.setSlike(new HashSet<>());

        MockMultipartFile valid = new MockMultipartFile(
                "file", "valid.jpg", "image/jpeg", "img".getBytes()
        );
        MockMultipartFile invalidType = new MockMultipartFile(
                "file", "notimage.txt", "text/plain", "text".getBytes()
        );
        MockMultipartFile tooLarge = new MockMultipartFile(
                "file", "large.jpg", "image/jpeg", new byte[6 * 1024 * 1024]
        );

        service.spremiSlike(List.of(valid, invalidType, tooLarge), t);

        verify(slikaRepo, times(1)).save(any());
        assertEquals(1, t.getSlike().size());

        Path path = Path.of("slike", "1");
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
    void testDohvatiSlike_NullFromRepo_ReturnsEmptyList() {
        when(slikaRepo.findByTrgovinaIdTrgovine(anyInt())).thenReturn(null);
        List<String> result = service.dohvatiSlike(1);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testObrisiSliku_DeletesImageSuccessfully() throws IOException {
        int trgovinaId = 1;
        String naziv = "slika.jpg";

        Trgovina trgovina = new Trgovina();
        Set<SlikaTrgovine> slike = new HashSet<>();
        trgovina.setSlike(slike);

        SlikaTrgovine slika = new SlikaTrgovine();
        slika.setNazivSlikeTrgovine(naziv);
        slika.setTrgovina(trgovina);
        slike.add(slika);

        when(slikaRepo.findByNazivSlikeTrgovineAndIdTrgovine(naziv, trgovinaId)).thenReturn(Optional.of(slika));

        Path putanja = Paths.get("slike", String.valueOf(trgovinaId), naziv);
        Files.createDirectories(putanja.getParent());
        Files.createFile(putanja);

        service.obrisiSliku(trgovinaId, naziv);

        verify(slikaRepo).delete(slika);
        assertFalse(Files.exists(putanja));

        Path path = Path.of("slike", "1");
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
    void testObrisiSliku_ImageNotFoundInDatabase_ThrowsException() {
        when(slikaRepo.findByNazivSlikeTrgovineAndIdTrgovine(anyString(), anyInt())).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.obrisiSliku(1, "nepostojeca.jpg");
        });

        assertEquals("Slika nije pronađena.", exception.getMessage());
        verify(slikaRepo, never()).delete(any());
    }

    @Test
    void testObrisiSliku_FileDoesNotExist_ThrowsException() {
        int trgovinaId = 1;
        String naziv = "nepostojeci.jpg";

        Trgovina trgovina = new Trgovina();
        trgovina.setSlike(new HashSet<>());

        SlikaTrgovine slika = new SlikaTrgovine();
        slika.setNazivSlikeTrgovine(naziv);
        slika.setTrgovina(trgovina);

        when(slikaRepo.findByNazivSlikeTrgovineAndIdTrgovine(naziv, trgovinaId)).thenReturn(Optional.of(slika));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            service.obrisiSliku(trgovinaId, naziv);
        });

        assertEquals("Slika ne postoji.", exception.getMessage());
        verify(slikaRepo).delete(slika);
    }

}
