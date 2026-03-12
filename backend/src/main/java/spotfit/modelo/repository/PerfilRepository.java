package spotfit.modelo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spotfit.modelo.entities.Perfil;

public interface PerfilRepository extends JpaRepository<Perfil, Integer> {
    public Perfil findByNombre(String nombre);
}