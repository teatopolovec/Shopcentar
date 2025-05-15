package fer.infsus.Odbojka.dao;
import fer.infsus.Odbojka.domain.Osoba;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OsobaRepository extends JpaRepository<Osoba, Integer>{
    Optional<Osoba> findByEmailOsobe(String emailOsobe);
}
