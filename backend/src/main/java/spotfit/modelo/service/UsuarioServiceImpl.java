package spotfit.modelo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import spotfit.modelo.dto.UsuarioDto;
import spotfit.modelo.entities.Usuario;
import spotfit.modelo.repository.UsuarioRepository;


@Service
public class UsuarioServiceImpl implements UsuarioService {

    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    // ============================================================
    // MÉTODOS CRUD HEREDADOS DE IntCrudGenerico
    // ============================================================
    
    /**
     * Busca un usuario por su ID.
     * 
     */
    @Override
    public Usuario findById(Integer atributoId) {
        return usuarioRepository.findById(atributoId).orElse(null);
        // .findById() devuelve un Optional<Usuario>
        // .orElse(null) devuelve el usuario si existe, o null si no existe
    }

    /*
     * Obtiene todos los usuarios de la base de datos.
     */
    @Override
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    /*
     * Inserta un nuevo usuario en la base de datos.
     */
    @Override
    public Usuario insertOne(Usuario entidad) {
        return usuarioRepository.save(entidad);
        // .save() inserta si no tiene ID, o actualiza si ya tiene ID
    }

    /*
     * Actualiza un usuario existente.
     * 
     */
    @Override
    public Usuario updateOne(Usuario entidad) {
        if (usuarioRepository.existsById(entidad.getIdUsuario()))
            return usuarioRepository.save(entidad);
        else
            return null;
    }

    /*
     * Elimina un usuario de la base de datos.
     */
    @Override
    public int deleteOne(Integer atributoId) {
        if (usuarioRepository.existsById(atributoId)) {
            try {
                usuarioRepository.deleteById(atributoId);
                return 1;  // Eliminación exitosa
            } catch (Exception e) {
                return -1;  // Error al eliminar (por ejemplo, FK constraints)
            }
        } else {
            return 0;  // Usuario no existe
        }
    }
    
    // ============================================================
    // MÉTODOS CON DTOs (Data Transfer Objects)
    // ============================================================
    
    /*
     * Busca un usuario por ID y lo devuelve como DTO.
     * 
     */
    @Override
    public UsuarioDto findDtoById(Integer id) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        return usuario != null ? UsuarioDto.convertirADto(usuario) : null;
    }
    
    /*
     * Obtiene todos los usuarios y los devuelve como DTOs.
     *
     */
    @Override
    public List<UsuarioDto> findAllDtos() {
        return usuarioRepository.findAll()
                .stream()  // Convierte la lista en un Stream (flujo de datos)
                .map(usuario -> UsuarioDto.convertirADto(usuario))  // Convierte cada Usuario a DTO
                .toList();  // Convierte el Stream de vuelta a List
    }
    
    // ============================================================
    // MÉTODO DE SPRING SECURITY (AUTENTICACIÓN)
    // ============================================================
    
    /*
     * Método que Spring Security llama automáticamente durante el login.
     */
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(email);
        // Busca el usuario por email
        // Si no lo encuentra, devuelve null (Spring lo convierte en excepción)
    }
}

//FLUJO DE LOGIN
//
//1. Usuario hace petición → GET /usuarios
//2. Spring intercepta → "Requiere autenticación"
//3. Extrae credenciales → email: pedro@mail.com, password: 1234
//4. Llama a loadUserByUsername("pedro@mail.com")
//5. Este método busca en BD → SELECT * FROM usuarios WHERE email = 'pedro@mail.com'
//6. Devuelve Usuario → {email: pedro@mail.com, contrasena: {noop}1234, perfil: ROLE_CLIENTE}
//7. Spring compara contraseñas → "1234" == "{noop}1234" ✅
//8. Spring obtiene roles → getAuthorities() → [ROLE_CLIENTE]
//9. Spring verifica permisos → /usuarios requiere ADMIN, Pedro tiene CLIENTE ❌
//10. Respuesta → 403 Forbidden