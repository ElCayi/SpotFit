package spotfit.modelo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spotfit.modelo.entities.Servicio;

public interface ServicioRepository extends JpaRepository<Servicio, Integer> {
}