package spotfit.modelo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spotfit.modelo.entities.Sesion;

public interface SesionRepository extends JpaRepository<Sesion, Integer> {
}