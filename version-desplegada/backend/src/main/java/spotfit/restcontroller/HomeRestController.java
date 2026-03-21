package spotfit.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import spotfit.modelo.dto.UsuarioLoginDto;
import spotfit.modelo.entities.Perfil;
import spotfit.modelo.entities.Usuario;
import spotfit.modelo.repository.PerfilRepository;
import spotfit.modelo.repository.UsuarioRepository;
import spotfit.security.JwtUtil;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeRestController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private JwtUtil jwtUtil;

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

        if (usuario == null) {
            return ResponseEntity.status(400).body("Usuario o contraseña incorrecta");
        }

        String role = usuario.getPerfil().getNombre();
        String token = jwtUtil.generateToken(usuario.getEmail(), role);

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("id", usuario.getIdUsuario());
        response.put("nombre", usuario.getNombre());
        response.put("email", usuario.getEmail());
        response.put("rol", role);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UsuarioLoginDto registerDto) {
        if (usuarioRepository.findByEmail(registerDto.getEmail()) != null) {
            return ResponseEntity.status(400).body("El email ya está registrado");
        }

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