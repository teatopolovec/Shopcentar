package fer.infsus.Shopcentar.dao;

import fer.infsus.Shopcentar.domain.SlikaTrgovine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SlikaTrgovineRepository extends JpaRepository<SlikaTrgovine, Integer> {
    List<SlikaTrgovine> findByTrgovinaIdTrgovine(Integer idTrgovine);

    @Query("SELECT s FROM SlikaTrgovine s WHERE s.nazivSlikeTrgovine = :naziv AND s.trgovina.idTrgovine = :id")
    Optional<SlikaTrgovine> findByNazivSlikeTrgovineAndIdTrgovine(@Param("naziv") String naziv, @Param("id") Integer id);
}
