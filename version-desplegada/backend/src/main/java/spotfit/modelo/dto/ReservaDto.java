package spotfit.modelo.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import spotfit.modelo.entities.Reserva;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReservaDto {
    private int idReserva;
    private int idUsuario;
    private String nombreUsuario;
    private int idSesion;
    private String nombreServicio;
    private LocalDateTime fechaReserva;
    private String estado;

    public static ReservaDto convertirADto(Reserva reserva) {
        ReservaDto dto = new ReservaDto();
        dto.setIdReserva(reserva.getIdReserva());
        dto.setIdUsuario(reserva.getUsuario().getIdUsuario());
        dto.setNombreUsuario(reserva.getUsuario().getNombre());
        dto.setIdSesion(reserva.getSesion().getIdSesion());
        dto.setNombreServicio(reserva.getSesion().getServicio().getNombre());
        dto.setFechaReserva(reserva.getFechaReserva());
        dto.setEstado(reserva.getEstado());
        return dto;
    }
}