package spotfit.modelo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spotfit.modelo.entities.Video;

public interface VideoRepository extends JpaRepository<Video, Integer> {
}