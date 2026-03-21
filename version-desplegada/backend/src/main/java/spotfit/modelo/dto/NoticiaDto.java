package spotfit.modelo.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import spotfit.modelo.entities.Noticia;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class NoticiaDto {
    private int idNoticia;
    private String titulo;
    private String contenido;
    private String urlImagen;
    private LocalDateTime fechaPublicacion;


//     La noticia es una entidad simple sin relaciones,
//     así que el DTO es una copia.
     
    public static NoticiaDto convertirADto(Noticia noticia) {
        NoticiaDto dto = new NoticiaDto();
        dto.setIdNoticia(noticia.getIdNoticia());
        dto.setTitulo(noticia.getTitulo());
        dto.setContenido(noticia.getContenido());
        dto.setUrlImagen(noticia.getUrlImagen());
        dto.setFechaPublicacion(noticia.getFechaPublicacion());
        return dto;
    }
}