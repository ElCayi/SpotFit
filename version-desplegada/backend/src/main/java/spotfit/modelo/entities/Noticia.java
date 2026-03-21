package spotfit.modelo.entities;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name="noticias")
public class Noticia {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int idNoticia;
	private String titulo;
	@Column(columnDefinition = "TEXT")
	private String contenido;
		// Texto completo de la noticia,la columna puede almacenar textos largos.
	@Column(name="url_imagen")
	private String urlImagen;
	
	@Column(name="fecha_publicacion", insertable = false, updatable = false)
	private LocalDateTime fechaPublicacion;
}