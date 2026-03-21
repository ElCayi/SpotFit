package spotfit.modelo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spotfit.modelo.entities.Noticia;

public interface NoticiaRepository extends JpaRepository<Noticia, Integer> {
}