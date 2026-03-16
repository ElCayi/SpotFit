package spotfit.modelo.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import spotfit.modelo.entities.Sesion;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SesionDto {
    private int idSesion;
    private int idServicio;
    private String nombreServicio;
    private int idMonitor;
    private String nombreMonitor;
    private int idSala;
    private String nombreSala;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private int aforoMaximo;
    private int reservasActuales;

    public static SesionDto convertirADto(Sesion sesion) {
        SesionDto dto = new SesionDto();
        dto.setIdSesion(sesion.getIdSesion());
        dto.setIdServicio(sesion.getServicio().getIdServicio());
        dto.setNombreServicio(sesion.getServicio().getNombre());
        dto.setIdMonitor(sesion.getMonitor().getIdUsuario());
        dto.setNombreMonitor(sesion.getMonitor().getNombre());
        dto.setIdSala(sesion.getSala().getIdSala());
        dto.setNombreSala(sesion.getSala().getNombre());
        dto.setFechaInicio(sesion.getFechaInicio());
        dto.setFechaFin(sesion.getFechaFin());
        dto.setAforoMaximo(sesion.getAforoMaximo());
        dto.setReservasActuales(0);
        return dto;
    }
}