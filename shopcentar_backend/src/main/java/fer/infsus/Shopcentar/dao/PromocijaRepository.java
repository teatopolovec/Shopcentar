package fer.infsus.Shopcentar.dao;

import fer.infsus.Shopcentar.domain.PromocijaTrgovine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PromocijaRepository extends JpaRepository<PromocijaTrgovine, Integer> {

    @Query("SELECT p FROM PromocijaTrgovine p WHERE p.trgovina.idTrgovine = :id")
    List<PromocijaTrgovine> findAllByIdtrgovine(@Param("id") Integer id);
}
