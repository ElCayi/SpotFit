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

    public static SesionDto convertirADto(Sesion sesion) {
        SesionDto dto = new SesionDto();
        dto.setIdSesion(sesion.getIdSesion());
        // Extraemos datos del objeto Servicio
        dto.setIdServicio(sesion.getServicio().getIdServicio());
        dto.setNombreServicio(sesion.getServicio().getNombre());
        // Extraemos datos del objeto Monitor (Usuario)
        dto.setIdMonitor(sesion.getMonitor().getIdUsuario());
        dto.setNombreMonitor(sesion.getMonitor().getNombre());
        // Extraemos datos del objeto Sala
        dto.setIdSala(sesion.getSala().getIdSala());
        dto.setNombreSala(sesion.getSala().getNombre());
        
        dto.setFechaInicio(sesion.getFechaInicio());
        dto.setFechaFin(sesion.getFechaFin());
        dto.setAforoMaximo(sesion.getAforoMaximo());
        return dto;
    }
}