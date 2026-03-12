package spotfit.modelo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import spotfit.modelo.entities.Sala;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SalaDto {
    private int idSala;
    private String nombre;
    private int capacidad;

 
//     La sala es una entidad simple sin relaciones,
//     así que el DTO es básicamente una copia.
    
    public static SalaDto convertirADto(Sala sala) {
        SalaDto dto = new SalaDto();
        dto.setIdSala(sala.getIdSala());
        dto.setNombre(sala.getNombre());
        dto.setCapacidad(sala.getCapacidad());
        return dto;
    }
}