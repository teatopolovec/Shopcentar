package fer.infsus.Shopcentar.repository;

import fer.infsus.Shopcentar.dao.PromocijaRepository;
import fer.infsus.Shopcentar.domain.Osoba;
import fer.infsus.Shopcentar.domain.PromocijaTrgovine;
import fer.infsus.Shopcentar.domain.Trgovina;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class PromocijaRepositoryTest {

    @Autowired
    private PromocijaRepository promocijaRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testFindAllByIdtrgovine_ReturnsPromotions() {
        Osoba upravitelj = new Osoba();
        upravitelj.setEmailOsobe("nesta@gmail.com");
        upravitelj.setIme("Ime");
        upravitelj.setPrezime("Prezime");
        upravitelj.setLozinka("loz123");
        upravitelj.setMobitel("0955555555");
        upravitelj.setUloga("upravitelj");
        entityManager.persist(upravitelj);

        Trgovina trgovina = new Trgovina();
        trgovina.setNazivTrgovine("Test trgovina");
        trgovina.setLogoTrgovine("slika.jpg");
        trgovina.setRadnoVrijeme("09:00-21:00");
        trgovina.setUpravitelj(upravitelj);
        entityManager.persist(trgovina);

        PromocijaTrgovine p1 = new PromocijaTrgovine();
        p1.setNaslovPromocije("Popust 10%");
        p1.setTekstPromocije("Popust 10%");
        p1.setSlikaPromocije("slika.png");
        p1.setObjavitelj(upravitelj);
        p1.setDatumKrajaProm(LocalDate.of(2024, 5, 23));
        p1.setDatumPočetkaProm(LocalDate.of(2024, 4, 23));
        p1.setTrgovina(trgovina);
        entityManager.persist(p1);

        PromocijaTrgovine p2 = new PromocijaTrgovine();
        p2.setNaslovPromocije("2+1 gratis");
        p2.setTekstPromocije("Popust 10%");
        p2.setSlikaPromocije("slika.png");
        p2.setObjavitelj(upravitelj);
        p2.setDatumKrajaProm(LocalDate.of(2024, 5, 23));
        p2.setDatumPočetkaProm(LocalDate.of(2024, 4, 23));
        p2.setTrgovina(trgovina);
        entityManager.persist(p2);

        entityManager.flush();

        List<PromocijaTrgovine> rezultat = promocijaRepository.findAllByIdtrgovine(trgovina.getIdTrgovine());

        assertEquals(2, rezultat.size());
        assertTrue(rezultat.stream().anyMatch(p -> p.getNaslovPromocije().equals("Popust 10%")));
        assertTrue(rezultat.stream().anyMatch(p -> p.getNaslovPromocije().equals("2+1 gratis")));
    }

    @Test
    public void testFindAllByIdtrgovine_ReturnsEmptyList() {
        List<PromocijaTrgovine> rezultat = promocijaRepository.findAllByIdtrgovine(999);

        assertNotNull(rezultat);
        assertTrue(rezultat.isEmpty());
    }
}
