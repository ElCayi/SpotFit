package spotfit.modelo.service;

import java.util.List;
import spotfit.modelo.dto.VideoDto;
import spotfit.modelo.entities.Video;

public interface VideoService extends IntCrudGenerico<Video, Integer> {
    // Métodos con DTOs
    VideoDto findDtoById(Integer id);
    List<VideoDto> findAllDtos();
}