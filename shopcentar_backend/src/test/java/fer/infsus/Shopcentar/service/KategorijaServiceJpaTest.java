package fer.infsus.Shopcentar.service;
 import fer.infsus.Shopcentar.service.impl.KategorijaServiceJpa;
        import fer.infsus.Shopcentar.dao.KategorijaRepository;
        import fer.infsus.Shopcentar.domain.Kategorija;
        import org.junit.jupiter.api.BeforeEach;
        import org.junit.jupiter.api.Test;
        import org.junit.jupiter.api.extension.ExtendWith;
        import org.mockito.*;
        import org.mockito.junit.jupiter.MockitoExtension;

        import java.util.*;

        import static org.assertj.core.api.Assertions.*;
        import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KategorijaServiceJpaTest {

    @Mock
    private KategorijaRepository kategorijaRepository;

    @InjectMocks
    private KategorijaServiceJpa kategorijaService;

    private Kategorija kategorija1;
    private Kategorija kategorija2;

    @BeforeEach
    void setup() {
        kategorija1 = new Kategorija();
        kategorija1.setIdKategorije(1);
        kategorija1.setNazivKategorije("Hrana");

        kategorija2 = new Kategorija();
        kategorija2.setIdKategorije(2);
        kategorija2.setNazivKategorije("Tehnika");
    }

    @Test
    void dohvatiKategorije_shouldReturnMapOfIdToName() {
        when(kategorijaRepository.findAll()).thenReturn(List.of(kategorija1, kategorija2));

        Map<Integer, String> result = kategorijaService.dohvatiKategorije();

        assertThat(result).hasSize(2);
        assertThat(result).containsEntry(1, "Hrana");
        assertThat(result).containsEntry(2, "Tehnika");
    }

    @Test
    void findById_shouldReturnOptionalKategorijaIfFound() {
        when(kategorijaRepository.findById(1)).thenReturn(Optional.of(kategorija1));

        Optional<Kategorija> result = kategorijaService.findById(1);

        assertThat(result).isPresent();
        assertThat(result.get().getIdKategorije()).isEqualTo(1);
        assertThat(result.get().getNazivKategorije()).isEqualTo("Hrana");
    }

    @Test
    void findById_shouldReturnEmptyIfNotFound() {
        when(kategorijaRepository.findById(99)).thenReturn(Optional.empty());

        Optional<Kategorija> result = kategorijaService.findById(99);

        assertThat(result).isNotPresent();
    }
}
