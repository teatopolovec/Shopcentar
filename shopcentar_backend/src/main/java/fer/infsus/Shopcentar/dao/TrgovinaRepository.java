package fer.infsus.Odbojka.dao;

import fer.infsus.Odbojka.domain.Trgovina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrgovinaRepository extends JpaRepository<Trgovina, Integer> {
    @Query("SELECT t.idTrgovine, t.nazivTrgovine FROM Trgovina t")
    List<Object[]> findIdAndNaziv();
}
