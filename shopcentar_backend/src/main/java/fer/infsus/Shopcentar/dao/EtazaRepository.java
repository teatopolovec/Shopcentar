package fer.infsus.Shopcentar.dao;

import fer.infsus.Shopcentar.domain.Etaza;
import fer.infsus.Shopcentar.dto.EtazaDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EtazaRepository extends JpaRepository<Etaza, Integer> {
    boolean existsByBrojEtaze(Integer brojEtaze);
}
