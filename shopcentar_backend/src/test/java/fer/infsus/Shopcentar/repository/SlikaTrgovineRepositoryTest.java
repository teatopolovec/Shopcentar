package fer.infsus.Shopcentar.repository;

import fer.infsus.Shopcentar.dao.SlikaTrgovineRepository;
import fer.infsus.Shopcentar.domain.Osoba;
import fer.infsus.Shopcentar.domain.SlikaTrgovine;
import fer.infsus.Shopcentar.domain.Trgovina;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class SlikaTrgovineRepositoryTest {

    @Autowired
    private SlikaTrgovineRepository slikaRepo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testFindByTrgovinaIdTrgovine_ReturnsCorrectSlike() {
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

        SlikaTrgovine slika1 = new SlikaTrgovine();
        slika1.setNazivSlikeTrgovine("slika1.jpg");
        slika1.setTrgovina(trgovina);

        SlikaTrgovine slika2 = new SlikaTrgovine();
        slika2.setNazivSlikeTrgovine("slika2.jpg");
        slika2.setTrgovina(trgovina);

        entityManager.persist(slika1);
        entityManager.persist(slika2);
        entityManager.flush();

        List<SlikaTrgovine> rezultat = slikaRepo.findByTrgovinaIdTrgovine(trgovina.getIdTrgovine());

        assertEquals(2, rezultat.size());
        assertTrue(rezultat.stream().anyMatch(s -> s.getNazivSlikeTrgovine().equals("slika1.jpg")));
        assertTrue(rezultat.stream().anyMatch(s -> s.getNazivSlikeTrgovine().equals("slika2.jpg")));
    }

    @Test
    void testFindByNazivSlikeTrgovineAndIdTrgovine_ReturnsCorrectOptional() {
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

        SlikaTrgovine slika = new SlikaTrgovine();
        slika.setNazivSlikeTrgovine("slika.jpg");
        slika.setTrgovina(trgovina);
        entityManager.persist(slika);
        entityManager.flush();

        Optional<SlikaTrgovine> rezultat = slikaRepo.findByNazivSlikeTrgovineAndIdTrgovine(
                "slika.jpg", trgovina.getIdTrgovine());

        assertTrue(rezultat.isPresent());
        assertEquals("slika.jpg", rezultat.get().getNazivSlikeTrgovine());
        assertEquals(trgovina.getIdTrgovine(), rezultat.get().getTrgovina().getIdTrgovine());
    }

    @Test
    void testFindByNazivSlikeTrgovineAndIdTrgovine_ReturnsEmptyIfNotFound() {
        Optional<SlikaTrgovine> rezultat = slikaRepo.findByNazivSlikeTrgovineAndIdTrgovine("nepostojece.jpg", 999);
        assertTrue(rezultat.isEmpty());
    }
}
