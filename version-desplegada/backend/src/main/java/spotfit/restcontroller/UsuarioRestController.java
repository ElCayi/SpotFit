package spotfit.restcontroller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import spotfit.modelo.dto.UsuarioDto;
import spotfit.modelo.entities.Usuario;
import spotfit.modelo.service.UsuarioService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/usuarios")
public class UsuarioRestController {

    @Autowired
    private UsuarioService usuarioService;
    
    @GetMapping("")
    public List<UsuarioDto> getAllUsuarios() {
        return usuarioService.findAllDtos();
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<UsuarioDto> getOneUsuario(@PathVariable int idUsuario) {
        UsuarioDto usuario = usuarioService.findDtoById(idUsuario);
        if (usuario != null)
            return ResponseEntity.status(200).body(usuario);
        else
            return ResponseEntity.status(404).body(null);
    }
    
    @PostMapping("")
    public ResponseEntity<?> addUsuario(@RequestBody Usuario usuario) {
        Usuario guardado = usuarioService.insertOne(usuario);
        if (guardado != null) {
            return ResponseEntity.status(201).body(UsuarioDto.convertirADto(guardado));
        }
        return ResponseEntity.status(400).body("Error al crear usuario");
    }

    @PutMapping("/{idUsuario}")
    public ResponseEntity<?> updateUsuario(@PathVariable int idUsuario, @RequestBody Usuario usuario) {
        usuario.setIdUsuario(idUsuario);
        Usuario actualizado = usuarioService.updateOne(usuario);
        if (actualizado != null) {
            return ResponseEntity.status(200).body(UsuarioDto.convertirADto(actualizado));
        }
        return ResponseEntity.status(404).body("Usuario no existe");
    }

    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<String> deleteUsuario(@PathVariable int idUsuario) {
        switch (usuarioService.deleteOne(idUsuario)) {
            case 1: return ResponseEntity.status(200).body("Usuario Eliminado");
            case 0: return ResponseEntity.status(404).body("Usuario no existe");
            case -1: return ResponseEntity.status(400).body("Usuario no se puede eliminar (tiene registros asociados)");
            default: return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}