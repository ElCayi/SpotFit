package spotfit.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import spotfit.modelo.dto.UsuarioLoginDto;
import spotfit.modelo.entities.Perfil;
import spotfit.modelo.entities.Usuario;
import spotfit.modelo.repository.PerfilRepository;
import spotfit.modelo.repository.UsuarioRepository;

@CrossOrigin(origins = "*")
@RestController
public class HomeRestController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @GetMapping("/")
    public ResponseEntity<?> home() {
        return ResponseEntity.ok("Bienvenido a SPOTFIT");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioLoginDto loginDto) {
        Usuario usuario = usuarioRepository.findByEmailAndContrasena(
            loginDto.getEmail(),
            "{noop}" + loginDto.getPassword()
        );

        if (usuario != null) {
            usuario.setContrasena(null);
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.status(400).body("Usuario o contraseña incorrecta");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UsuarioLoginDto registerDto) {
        // Verificar si el email ya existe
        if (usuarioRepository.findByEmail(registerDto.getEmail()) != null) {
            return ResponseEntity.status(400).body("El email ya está registrado");
        }

        // Asignar rol CLIENTE por defecto a todo registro público
        Perfil perfilCliente = perfilRepository.findByNombre("ROLE_CLIENTE");
        if (perfilCliente == null) {
            return ResponseEntity.status(500).body("Error interno: perfil CLIENTE no encontrado");
        }

        Usuario nuevo = new Usuario();
        nuevo.setEmail(registerDto.getEmail());
        nuevo.setContrasena("{noop}" + registerDto.getPassword());
        nuevo.setNombre("");
        nuevo.setApellidos("");
        nuevo.setActivo(true);
        nuevo.setPerfil(perfilCliente);

        usuarioRepository.save(nuevo);
        return ResponseEntity.status(201).body("Usuario registrado correctamente");
    }
}