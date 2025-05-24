package fer.infsus.Shopcentar.repository;

import fer.infsus.Shopcentar.dao.TrgovinaRepository;
import fer.infsus.Shopcentar.domain.Osoba;
import fer.infsus.Shopcentar.domain.Trgovina;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class TrgovinaRepositoryTest {

    @Autowired
    private TrgovinaRepository trgovinaRepo;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        Osoba upravitelj = new Osoba();
        upravitelj.setEmailOsobe("nesta@gmail.com");
        upravitelj.setIme("Ime");
        upravitelj.setPrezime("Prezime");
        upravitelj.setLozinka("loz123");
        upravitelj.setMobitel("0955555555");
        upravitelj.setUloga("upravitelj");
        entityManager.persist(upravitelj);

        Trgovina t1 = new Trgovina();
        t1.setNazivTrgovine("Tech Shop");
        t1.setLogoTrgovine("slika.jpg");
        t1.setRadnoVrijeme("09:00-21:00");
        t1.setUpravitelj(upravitelj);
        t1.setAktivna(true);
        entityManager.persist(t1);

        Trgovina t2 = new Trgovina();
        t2.setNazivTrgovine("Book World");
        t2.setLogoTrgovine("slika.jpg");
        t2.setRadnoVrijeme("09:00-21:00");
        t2.setUpravitelj(upravitelj);
        t2.setAktivna(false);
        entityManager.persist(t2);

        Trgovina t3 = new Trgovina();
        t3.setNazivTrgovine("Fashion Zone");
        t3.setLogoTrgovine("slika.jpg");
        t3.setRadnoVrijeme("09:00-21:00");
        t3.setUpravitelj(upravitelj);
        t3.setAktivna(true);
        entityManager.persist(t3);
        entityManager.flush();
    }

    @Test
    void findIdAndNaziv_VracaSamoAktivneTrgovine() {
        List<Object[]> result = trgovinaRepo.findIdAndNaziv();

        assertEquals(2, result.size());
        for (Object[] row : result) {
            assertNotNull(row[0]);
            assertTrue(row[1] instanceof String);
        }
        List<String> nazivi = result.stream().map(r -> (String) r[1]).toList();
        assertTrue(nazivi.contains("Tech Shop"));
        assertTrue(nazivi.contains("Fashion Zone"));
        assertFalse(nazivi.contains("Book World"));
    }

    @Test
    void existsByNazivTrgovine_VracaTrueAkoPostoji() {
        assertTrue(trgovinaRepo.existsByNazivTrgovine("Tech Shop"));
        assertFalse(trgovinaRepo.existsByNazivTrgovine("NonExistent"));
    }
}
