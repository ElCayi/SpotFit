package spotfit.modelo.entities;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
	//Genera un constructor con todos los atributos de la clase.
@NoArgsConstructor
	//Genera un constructor vacio, obligatorio para que JPA pueda crear la entidad.
@Data
	//Genera automaticamente los Getters, Setters, toString, equals y hashCode.
@Builder
	//Permite crear objetos de esta clase de forma mas legible
@Entity
	//Le indica a Spring Boot que esta clase es una tabla de la base de datos.
@Table(name="sesiones")
	//Especifica que el nombre real de la tabla en MySQL es "sesiones".
public class Sesion {
	@Id
		// Define este atributo como la clave primaria.
	@GeneratedValue(strategy = GenerationType.IDENTITY)
		// Configura el ID como autoincremental en MySQL.
	@Column(name="id")
		// En Java se llama "id", pero en la tabla se mapea como "id_sesion".
	private int idSesion;
	
	@ManyToOne
		// Relacion N:1. Muchas sesiones pueden pertenecer a un mismo servicio.
	@JoinColumn(name="servicio_id")
		// Define la columna en la tabla que actua como clave foranea (FK).
	private Servicio servicio;
	
	@ManyToOne
		// Relacion N:1. Muchas sesiones pueden ser impartidas por un mismo monitor.
	@JoinColumn(name="monitor_id")
	private Usuario monitor;
	
	@ManyToOne
		// Relacion N:1. Muchas sesiones pueden ocurrir en una misma sala.
	@JoinColumn(name="sala_id")
	private Sala sala;
	
	@Column(name="fecha_inicio")
	private LocalDateTime fechaInicio;
	
	@Column(name="fecha_fin")
	private LocalDateTime fechaFin;
	
	@Column(name="aforo_maximo")
	private int aforoMaximo;
}