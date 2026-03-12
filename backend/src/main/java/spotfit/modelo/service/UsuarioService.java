package spotfit.modelo.service;

import java.util.List;
import org.springframework.security.core.userdetails.UserDetailsService;
import spotfit.modelo.dto.UsuarioDto;
import spotfit.modelo.entities.Usuario;

/**
 * Servicio de usuarios.
 * 
 * Extiende UserDetailsService para que Spring Security pueda
 * usar este servicio para cargar usuarios durante el login.
 */
public interface UsuarioService extends IntCrudGenerico<Usuario, Integer>, UserDetailsService {
    
    // Métodos con DTOs
    UsuarioDto findDtoById(Integer id);
    List<UsuarioDto> findAllDtos();
}