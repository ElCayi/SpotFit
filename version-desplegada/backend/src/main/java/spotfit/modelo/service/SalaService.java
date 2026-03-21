package spotfit.modelo.service;

import java.util.List;
import spotfit.modelo.dto.SalaDto;
import spotfit.modelo.entities.Sala;

public interface SalaService extends IntCrudGenerico<Sala, Integer> {
    // Métodos con DTOs
    SalaDto findDtoById(Integer id);
    List<SalaDto> findAllDtos();
}