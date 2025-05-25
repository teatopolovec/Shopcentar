package fer.infsus.Shopcentar.repository;

import fer.infsus.Shopcentar.dao.OsobaRepository;
import fer.infsus.Shopcentar.domain.Osoba;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class OsobaRepositoryTest {

    @Autowired
    private OsobaRepository osobaRepository;

    @Test
    void findByEmailOsobe_shouldReturnOsobaIfExists() {
        // given
        Osoba osoba = new Osoba();
        osoba.setEmailOsobe("test@example.com");
        osoba.setIme("Ime");
        osoba.setPrezime("Prezime");
        osoba.setLozinka("loz123");
        osoba.setMobitel("0955555555");
        osoba.setUloga("admin");
        osobaRepository.save(osoba);

        // when
        Optional<Osoba> found = osobaRepository.findByEmailOsobe("test@example.com");

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getEmailOsobe()).isEqualTo("test@example.com");
    }

    @Test
    void findByEmailOsobe_shouldReturnEmptyIfNotExists() {
        // when
        Optional<Osoba> found = osobaRepository.findByEmailOsobe("nonexistent@example.com");

        // then
        assertThat(found).isNotPresent();
    }

    @Test
    void findByUloga_shouldReturnListOfIdAndEmailForGivenUloga() {
        // given
        Osoba osoba1 = new Osoba();
        osoba1.setEmailOsobe("user1@example.com");
        osoba1.setIme("Ime");
        osoba1.setPrezime("Prezime");
        osoba1.setLozinka("loz123");
        osoba1.setMobitel("0955555555");
        osoba1.setUloga("admin");
        osobaRepository.save(osoba1);

        Osoba osoba2 = new Osoba();
        osoba2.setEmailOsobe("user2@example.com");
        osoba2.setIme("Ime2");
        osoba2.setPrezime("Prezime2");
        osoba2.setLozinka("loz1234");
        osoba2.setMobitel("0954555555");
        osoba2.setUloga("admin");
        osobaRepository.save(osoba2);

        Osoba osoba3 = new Osoba();
        osoba3.setEmailOsobe("user3@example.com");
        osoba3.setIme("Ime3");
        osoba3.setPrezime("Prezime3");
        osoba3.setLozinka("loz1235");
        osoba3.setMobitel("0955565555");
        osoba3.setUloga("upravitelj");
        osobaRepository.save(osoba3);

        // when
        List<Object[]> results = osobaRepository.findByUloga("admin");
        // then
        assertThat(results).hasSize(2);

        // Each result is Object[] with [idOsobe, emailOsobe]
        for (Object[] row : results) {
            Integer id = (Integer) row[0];
            String email = (String) row[1];
            assertThat(email).endsWith("@example.com");
            assertThat(id).isNotNull();
        }
    }
}

