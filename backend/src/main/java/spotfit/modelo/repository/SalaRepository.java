package spotfit.modelo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spotfit.modelo.entities.Sala;

public interface SalaRepository extends JpaRepository<Sala, Integer> {
}