package spotfit.modelo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spotfit.modelo.dto.NoticiaDto;
import spotfit.modelo.entities.Noticia;
import spotfit.modelo.repository.NoticiaRepository;

@Service
public class NoticiaServiceImpl implements NoticiaService {

    @Autowired
    private NoticiaRepository noticiaRepository;

    @Override
    public Noticia findById(Integer atributoId) {
        return noticiaRepository.findById(atributoId).orElse(null);
    }

    @Override
    public List<Noticia> findAll() {
        return noticiaRepository.findAll();
    }

    @Override
    public Noticia insertOne(Noticia entidad) {
        return noticiaRepository.save(entidad);
    }

    @Override
    public Noticia updateOne(Noticia entidad) {
        if (noticiaRepository.existsById(entidad.getIdNoticia()))
            return noticiaRepository.save(entidad);
        else
            return null;
    }

    @Override
    public int deleteOne(Integer atributoId) {
        if (noticiaRepository.existsById(atributoId)) {
            try {
                noticiaRepository.deleteById(atributoId);
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
    public NoticiaDto findDtoById(Integer id) {
        Noticia noticia = noticiaRepository.findById(id).orElse(null);
        return noticia != null ? NoticiaDto.convertirADto(noticia) : null;
    }
    
    @Override
    public List<NoticiaDto> findAllDtos() {
        return noticiaRepository.findAll()
                .stream()
                .map(noticia -> NoticiaDto.convertirADto(noticia))
                .toList();
    }
}