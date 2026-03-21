package spotfit.modelo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spotfit.modelo.entities.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
	

    // MÉTODO PARA CONTAR RESERVAS POR SESIÓN Y ESTADO
    // Cuenta cuántas reservas tiene una sesión específica con un estado determinado.
	
	long countBySesion_IdSesionAndEstado(int idSesion, String estado);
}