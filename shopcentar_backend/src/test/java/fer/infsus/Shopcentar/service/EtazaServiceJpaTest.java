package fer.infsus.Shopcentar.service;

import fer.infsus.Shopcentar.service.impl.EtazaServiceJpa;
        import fer.infsus.Shopcentar.dao.EtazaRepository;
        import fer.infsus.Shopcentar.domain.Etaza;
        import fer.infsus.Shopcentar.dto.EtazaDTO;
        import org.junit.jupiter.api.BeforeEach;
        import org.junit.jupiter.api.Test;
        import org.junit.jupiter.api.extension.ExtendWith;
        import org.mockito.*;
        import org.mockito.junit.jupiter.MockitoExtension;

        import java.io.IOException;
        import java.util.*;

        import static org.assertj.core.api.Assertions.*;
        import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EtazaServiceJpaTest {

    @Mock
    private EtazaRepository etazaRepository;

    @InjectMocks
    private EtazaServiceJpa etazaService;

    private Etaza etazaEntity;

    @BeforeEach
    void setup() {
        etazaEntity = new Etaza();
        etazaEntity.setIdEtaze(1);
        etazaEntity.setBrojEtaze(2);
        etazaEntity.setOpis("Test opis");
    }

    @Test
    void dohvatiEtaze_shouldReturnListOfMaps() {
        when(etazaRepository.findAll()).thenReturn(List.of(etazaEntity));

        List<Map<String, Object>> result = etazaService.dohvatiEtaze();

        assertThat(result).hasSize(1);
        Map<String, Object> map = result.get(0);
        assertThat(map.get("idEtaze")).isEqualTo(etazaEntity.getIdEtaze());
        assertThat(map.get("brojEtaze")).isEqualTo(etazaEntity.getBrojEtaze());
        assertThat(map.get("opis")).isEqualTo(etazaEntity.getOpis());
    }

    @Test
    void kreirajEtazu_shouldSaveNewEtaza() throws IOException {
        EtazaDTO dto = new EtazaDTO();
        dto.setBrojEtaze(3);
        dto.setOpis("Novi opis");

        when(etazaRepository.existsByBrojEtaze(dto.getBrojEtaze())).thenReturn(false);
        when(etazaRepository.save(any(Etaza.class))).thenAnswer(invocation -> {
            Etaza saved = invocation.getArgument(0);
            saved.setIdEtaze(10);
            return saved;
        });

        Etaza saved = etazaService.kreirajEtazu(dto);

        assertThat(saved.getIdEtaze()).isNotNull();
        assertThat(saved.getBrojEtaze()).isEqualTo(dto.getBrojEtaze());
        assertThat(saved.getOpis()).isEqualTo(dto.getOpis());
    }

    @Test
    void kreirajEtazu_shouldThrowIfBrojEtazeExists() {
        EtazaDTO dto = new EtazaDTO();
        dto.setBrojEtaze(2);
        dto.setOpis("Opis");

        when(etazaRepository.existsByBrojEtaze(dto.getBrojEtaze())).thenReturn(true);

        assertThatThrownBy(() -> etazaService.kreirajEtazu(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Etaža s tim brojem već postoji.");
    }

    @Test
    void azurirajEtazu_shouldUpdateExistingEtaza() {
        EtazaDTO dto = new EtazaDTO();
        dto.setBrojEtaze(5);
        dto.setOpis("Novi opis");

        when(etazaRepository.findById(1)).thenReturn(Optional.of(etazaEntity));
        when(etazaRepository.existsByBrojEtaze(dto.getBrojEtaze())).thenReturn(false);
        when(etazaRepository.save(any(Etaza.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Etaza updated = etazaService.azurirajEtazu(1, dto);

        assertThat(updated.getBrojEtaze()).isEqualTo(dto.getBrojEtaze());
        assertThat(updated.getOpis()).isEqualTo(dto.getOpis());
    }

    @Test
    void azurirajEtazu_shouldThrowIfIdNotFound() {
        EtazaDTO dto = new EtazaDTO();
        when(etazaRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> etazaService.azurirajEtazu(1, dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Etaža s ID 1 nije pronađena.");
    }

    @Test
    void izbrisiEtazu_shouldDeleteIfExists() {
        when(etazaRepository.findById(1)).thenReturn(Optional.of(etazaEntity));

        etazaService.izbrisiEtazu(1);

        verify(etazaRepository).delete(etazaEntity);
    }

    @Test
    void izbrisiEtazu_shouldThrowIfIdNotFound() {
        when(etazaRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> etazaService.izbrisiEtazu(1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Etaža s ID 1 nije pronađena.");
    }

    @Test
    void findById_shouldReturnEtazaDTOIfFound() {
        when(etazaRepository.findById(1)).thenReturn(Optional.of(etazaEntity));

        Optional<EtazaDTO> dto = etazaService.findById(1);

        assertThat(dto).isPresent();
        assertThat(dto.get().getBrojEtaze()).isEqualTo(etazaEntity.getBrojEtaze());
        assertThat(dto.get().getOpis()).isEqualTo(etazaEntity.getOpis());
    }

    @Test
    void findById_shouldReturnEmptyIfNotFound() {
        when(etazaRepository.findById(1)).thenReturn(Optional.empty());

        Optional<EtazaDTO> dto = etazaService.findById(1);

        assertThat(dto).isNotPresent();
    }
}

