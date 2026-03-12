package spotfit.modelo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import spotfit.modelo.entities.Video;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VideoDto {
    private int idVideo;
    private String titulo;
    private String urlVideo;
    private String categoria;

//     El video es una entidad simple sin relaciones,
//     así que el DTO es una copia.
     
    public static VideoDto convertirADto(Video video) {
        VideoDto dto = new VideoDto();
        dto.setIdVideo(video.getIdVideo());
        dto.setTitulo(video.getTitulo());
        dto.setUrlVideo(video.getUrlVideo());
        dto.setCategoria(video.getCategoria());
        return dto;
    }
}