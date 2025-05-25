package fer.infsus.Shopcentar.repository;

import fer.infsus.Shopcentar.dao.EtazaRepository;
import fer.infsus.Shopcentar.domain.Etaza;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class EtazaRepositoryTest {

    @Autowired
    private EtazaRepository etazaRepository;

    @Test
    void existsByBrojEtaze_shouldReturnTrueIfExists() {
        // given
        Etaza etaza = new Etaza();
        etaza.setBrojEtaze(5);
        etaza.setOpis("gergerg");
        etazaRepository.save(etaza);

        // when
        boolean exists = etazaRepository.existsByBrojEtaze(5);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void existsByBrojEtaze_shouldReturnFalseIfNotExists() {
        // when
        boolean exists = etazaRepository.existsByBrojEtaze(99);

        // then
        assertThat(exists).isFalse();
    }
}

