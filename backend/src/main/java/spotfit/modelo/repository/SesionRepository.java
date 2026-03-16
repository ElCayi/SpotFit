package spotfit.modelo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import spotfit.modelo.entities.Sesion;
import java.util.List;

public interface SesionRepository extends JpaRepository<Sesion, Integer> {

    @Query("SELECT s FROM Sesion s WHERE DATE(s.fechaInicio) = CURRENT_DATE")
    List<Sesion> findSesionesHoy();
    
    // metodo para contar las reservas confirmadas
    
    @Query("SELECT COUNT(r) FROM Reserva r WHERE r.sesion.idSesion = :idSesion AND r.estado = 'CONFIRMADA'")
    int countReservasConfirmadas(@Param("idSesion") int idSesion);
}