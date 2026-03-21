package spotfit.modelo.service;

import java.util.List;
import spotfit.modelo.dto.ReservaDto;
import spotfit.modelo.entities.Reserva;

public interface ReservaService extends IntCrudGenerico<Reserva, Integer> {
	// Limpiamos el flujo de datos (DTO)
    List<ReservaDto> findAllDtos();
    ReservaDto findDtoById(Integer id);
}