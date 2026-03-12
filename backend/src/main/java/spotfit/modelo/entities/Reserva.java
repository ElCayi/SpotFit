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
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name="reservas")
public class Reserva {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int idReserva;
	
	@ManyToOne
		// Relacion N:1. Un usuario puede tener muchas reservas.
	@JoinColumn(name="usuario_id")
	private Usuario usuario;
	
	@ManyToOne
		// Relacion N:1. Muchas reservas pueden apuntar a la misma sesion/clase.
	@JoinColumn(name="sesion_id")
	private Sesion sesion;
	
	@Column(name="fecha_reserva")
	private LocalDateTime fechaReserva;
	
	private String estado;
		// Almacena si la reserva esta 'CONFIRMADA' o 'CANCELADA'.
}