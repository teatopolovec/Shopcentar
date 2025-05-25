package fer.infsus.Shopcentar.service;

import fer.infsus.Shopcentar.dao.OsobaRepository;
import fer.infsus.Shopcentar.domain.Osoba;
import fer.infsus.Shopcentar.service.impl.OsobaServiceJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OsobaServiceJpaTest {

    @Mock
    private OsobaRepository osobaRepository;

    @InjectMocks
    private OsobaServiceJpa osobaService;

    private Osoba osobaEntity;

    @BeforeEach
    void setup() {
        osobaEntity = new Osoba();
        osobaEntity.setIdOsobe(1);
        osobaEntity.setEmailOsobe("test@domain.com");
        osobaEntity.setUloga("upravitelj");
    }

    @Test
    void dohvatiUpraviteljaPoEmailu_shouldReturnOsobaIfFound() {
        when(osobaRepository.findByEmailOsobe("test@domain.com"))
                .thenReturn(Optional.of(osobaEntity));

        Osoba result = osobaService.dohvatiUpraviteljaPoEmailu("test@domain.com");

        assertThat(result).isNotNull();
        assertThat(result.getEmailOsobe()).isEqualTo("test@domain.com");
    }

    @Test
    void dohvatiUpraviteljaPoEmailu_shouldThrowIfNotFound() {
        when(osobaRepository.findByEmailOsobe("missing@domain.com"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> osobaService.dohvatiUpraviteljaPoEmailu("missing@domain.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Upravitelj nije pronađen.");
    }

    @Test
    void dohvatiUpravitelje_shouldReturnListOfMaps() {
        // Prepare dummy data as Object[] list: [idOsobe, emailOsobe]
        List<Object[]> repoResult = List.of(
                new Object[]{1, "upravitelj1@domain.com"},
                new Object[]{2, "upravitelj2@domain.com"}
        );

        when(osobaRepository.findByUloga("upravitelj")).thenReturn(repoResult);

        List<Map<String, Object>> result = osobaService.dohvatiUpravitelje();

        assertThat(result).hasSize(2);
        assertThat(result.get(0)).containsEntry("idOsobe", 1).containsEntry("emailOsobe", "upravitelj1@domain.com");
        assertThat(result.get(1)).containsEntry("idOsobe", 2).containsEntry("emailOsobe", "upravitelj2@domain.com");
    }
}
