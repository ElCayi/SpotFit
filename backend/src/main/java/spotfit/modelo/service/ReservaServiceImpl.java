package spotfit.modelo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import spotfit.modelo.dto.ReservaDto;
import spotfit.modelo.entities.Reserva;
import spotfit.modelo.entities.Sesion;
import spotfit.modelo.repository.ReservaRepository;
import spotfit.modelo.repository.SesionRepository;

@Service
public class ReservaServiceImpl implements ReservaService {

	@Autowired
	private ReservaRepository reservaRepository;

	// Necesitamos SesionRepository para consultar el aforo máximo de una sesión

	@Autowired
	private SesionRepository sesionRepository;

	// Metodos que nos devuelven la entidad reserva entera

	@Override
	public Reserva findById(Integer atributoId) {
		return reservaRepository.findById(atributoId).orElse(null);
	}

	@Override
	public List<Reserva> findAll() {
		return reservaRepository.findAll();
	}

	// MÉTODO PRIVADO: VERIFICAR DISPONIBILIDAD

	// 1. Cuenta cuántas reservas CONFIRMADAS tiene la sesión
	// 2. Obtiene el aforo máximo de la sesión
	// 3. Compara: si reservas < aforo, hay plazas

	private boolean hayPlazasDisponibles(int sesionId) {
		long reservasConfirmadas = reservaRepository.countBySesion_IdSesionAndEstado(sesionId, "CONFIRMADA");
		Sesion sesion = sesionRepository.findById(sesionId).orElse(null);
		if (sesion == null) {
			return false;
		}
		return reservasConfirmadas < sesion.getAforoMaximo();
	}

	// INSERTAR RESERVA CON VALIDACIÓN DE PLAZAS

	// 1. Cliente intenta reservar una sesión
	// 2. Verificamos si la sesión tiene plazas disponibles
	// 3. Si no hay plazas → rechazamos la reserva (devolvemos null)
	// 4. Si hay plazas → guardamos la reserva

	@Override
	public Reserva insertOne(Reserva entidad) {
		if ("CONFIRMADA".equals(entidad.getEstado())) {
			int sesionId = entidad.getSesion().getIdSesion();
			if (!hayPlazasDisponibles(sesionId)) {
				return null;
			}
		}
		return reservaRepository.save(entidad);
	}

	@Override
	public Reserva updateOne(Reserva entidad) {
		if (reservaRepository.existsById(entidad.getIdReserva()))
			return reservaRepository.save(entidad);
		else
			return null;
	}

	@Override
	public int deleteOne(Integer atributoId) {
		if (reservaRepository.existsById(atributoId)) {
			try {
				reservaRepository.deleteById(atributoId);
				return 1;
			} catch (Exception e) {
				return -1;
			}
		} else {
			return 0;
		}
	}

	// Metodos que nos devuelven DTOs

	@Override
	public List<ReservaDto> findAllDtos() {
		return reservaRepository.findAll().stream().map(reserva -> ReservaDto.convertirADto(reserva)).toList();
	}

	@Override
	public ReservaDto findDtoById(Integer id) {
		Reserva reserva = reservaRepository.findById(id).orElse(null);
		return reserva != null ? ReservaDto.convertirADto(reserva) : null;
	}

}