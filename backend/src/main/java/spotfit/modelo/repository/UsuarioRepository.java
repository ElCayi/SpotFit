package spotfit.modelo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spotfit.modelo.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    
    /**
     * Busca un usuario por su email.
     * 
     * Spring Security llamará a este método durante el login
     * a través de loadUserByUsername().
     * 
     * Spring Data JPA genera automáticamente la query:
     * SELECT * FROM usuarios WHERE email = ?
     */
    Usuario findByEmail(String email);
    
    /**
     * Busca un usuario por email Y contraseña.
     * 
     * Se usa en el endpoint /login para verificar credenciales manualmente.
     * Spring Data JPA genera automáticamente:
     * SELECT * FROM usuarios WHERE email = ? AND contrasena = ?
     */
    Usuario findByEmailAndContrasena(String email, String contrasena);
}