package spotfit.modelo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import spotfit.modelo.entities.Usuario;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UsuarioDto {
    private int idUsuario;
    private String nombre;
    private String apellidos;
    private String email;
    private String rol;  // Nombre del perfil (ej: "ROLE_ADMIN")
    private boolean activo;

    /**
     * Convierte un Usuario a DTO.
     * 
     * CAMBIO IMPORTANTE:
     * Antes: usuario.getRol() devolvía un String directamente
     * Ahora: usuario.getPerfil() devuelve un objeto Perfil,
     *        y de ese objeto extraemos el nombre con .getNombre()
     */
    public static UsuarioDto convertirADto(Usuario usuario) {
        UsuarioDto dto = new UsuarioDto();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombre(usuario.getNombre());
        dto.setApellidos(usuario.getApellidos());
        dto.setEmail(usuario.getEmail());
        dto.setRol(usuario.getPerfil().getNombre());  
        dto.setActivo(usuario.isActivo());
        return dto;
    }
}