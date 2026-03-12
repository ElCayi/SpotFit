package spotfit.modelo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spotfit.modelo.dto.VideoDto;
import spotfit.modelo.entities.Video;
import spotfit.modelo.repository.VideoRepository;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoRepository videoRepository;

    @Override
    public Video findById(Integer atributoId) {
        return videoRepository.findById(atributoId).orElse(null);
    }

    @Override
    public List<Video> findAll() {
        return videoRepository.findAll();
    }

    @Override
    public Video insertOne(Video entidad) {
        return videoRepository.save(entidad);
    }

    @Override
    public Video updateOne(Video entidad) {
        if (videoRepository.existsById(entidad.getIdVideo()))
            return videoRepository.save(entidad);
        else
            return null;
    }

    @Override
    public int deleteOne(Integer atributoId) {
        if (videoRepository.existsById(atributoId)) {
            try {
                videoRepository.deleteById(atributoId);
                return 1;
            } catch (Exception e) {
                return -1;
            }
        } else {
            return 0;
        }
    }
    
    // MÉTODOS CON DTOs

    
    @Override
    public VideoDto findDtoById(Integer id) {
        Video video = videoRepository.findById(id).orElse(null);
        return video != null ? VideoDto.convertirADto(video) : null;
    }
    
    @Override
    public List<VideoDto> findAllDtos() {
        return videoRepository.findAll()
                .stream()
                .map(video -> VideoDto.convertirADto(video))
                .toList();
    }
}