package fer.infsus.Shopcentar.dao;

import fer.infsus.Shopcentar.domain.Trgovina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrgovinaRepository extends JpaRepository<Trgovina, Integer> {
    @Query("SELECT t.idTrgovine, t.nazivTrgovine FROM Trgovina t WHERE t.aktivna = true")
    List<Object[]> findIdAndNaziv();

    boolean existsByNazivTrgovine(String nazivTrgovine);
}
