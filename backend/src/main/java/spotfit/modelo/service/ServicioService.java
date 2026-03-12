package spotfit.modelo.service;

import java.util.List;
import spotfit.modelo.dto.ServicioDto;
import spotfit.modelo.entities.Servicio;

public interface ServicioService extends IntCrudGenerico<Servicio, Integer> {
    // Métodos con DTOs
    ServicioDto findDtoById(Integer id);
    List<ServicioDto> findAllDtos();
}