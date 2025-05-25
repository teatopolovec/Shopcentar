package fer.infsus.Shopcentar.dao;

import fer.infsus.Shopcentar.domain.Kategorija;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KategorijaRepository extends JpaRepository<Kategorija, Integer> {

}
