package spotfit.modelo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import spotfit.modelo.entities.Servicio;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ServicioDto {
    private int idServicio;
    private String nombre;
    private String descripcion;
    private String categoria;

//     El servicio es una entidad simple sin relaciones,
//     así que el DTO es básicamente una copia.
     
    public static ServicioDto convertirADto(Servicio servicio) {
        ServicioDto dto = new ServicioDto();
        dto.setIdServicio(servicio.getIdServicio());
        dto.setNombre(servicio.getNombre());
        dto.setDescripcion(servicio.getDescripcion());
        dto.setCategoria(servicio.getCategoria());
        return dto;
    }
}