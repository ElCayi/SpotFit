package spotfit.modelo.service;

import java.util.List;
import spotfit.modelo.dto.NoticiaDto;
import spotfit.modelo.entities.Noticia;

public interface NoticiaService extends IntCrudGenerico<Noticia, Integer> {
    // Métodos con DTOs
    NoticiaDto findDtoById(Integer id);
    List<NoticiaDto> findAllDtos();
}