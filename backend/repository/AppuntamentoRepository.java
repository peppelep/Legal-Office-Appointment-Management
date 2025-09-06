package piattaforme.demo.repository;

import piattaforme.demo.models.Appuntamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppuntamentoRepository extends JpaRepository<Appuntamento, Long> {
}
