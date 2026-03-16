package spotfit.modelo.service;

import java.util.List;

import spotfit.modelo.dto.SesionDto;
import spotfit.modelo.entities.Sesion;

public interface SesionService extends IntCrudGenerico<Sesion, Integer> {
	// Limpiamos el flujo de datos (DTO)
    List<SesionDto> findAllDtos();
    SesionDto findDtoById(Integer id);
    List<SesionDto> findSesionesHoy();
}
