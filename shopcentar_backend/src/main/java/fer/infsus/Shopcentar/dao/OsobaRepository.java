package fer.infsus.Shopcentar.dao;
import fer.infsus.Shopcentar.domain.Osoba;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OsobaRepository extends JpaRepository<Osoba, Integer>{
    Optional<Osoba> findByEmailOsobe(String emailOsobe);

    @Query("SELECT o.idOsobe, o.emailOsobe FROM Osoba o WHERE o.uloga = :uloga")
    List<Object[]> findByUloga(@Param("uloga") String uloga);
}
