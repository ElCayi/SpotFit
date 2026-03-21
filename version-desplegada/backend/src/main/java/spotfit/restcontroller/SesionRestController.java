package spotfit.restcontroller;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import spotfit.modelo.dto.SesionDto;
import spotfit.modelo.entities.Sesion;
import spotfit.modelo.service.SesionService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/sesiones")
public class SesionRestController {

    @Autowired
    private SesionService sesionService;

    @GetMapping("")
    public List<SesionDto> getAllSesiones() {
        return sesionService.findAllDtos();
    }

    // ✅ NUEVO: Endpoint para obtener sesiones de hoy
    @GetMapping("/hoy")
    public List<SesionDto> getSesionesHoy() {
        return sesionService.findSesionesHoy();
    }

    @GetMapping("/{idSesion}")
    public ResponseEntity<SesionDto> getOneSesion(@PathVariable int idSesion) {
        SesionDto sesion = sesionService.findDtoById(idSesion);
        if (sesion != null)
            return ResponseEntity.status(200).body(sesion);
        else
            return ResponseEntity.status(404).body(null);
    }

    @PostMapping("")
    public ResponseEntity<?> addSesion(@RequestBody Sesion sesion) {
        Sesion guardada = sesionService.insertOne(sesion);
        if (guardada != null) {
            return ResponseEntity.status(201).body(SesionDto.convertirADto(guardada));
        }
        return ResponseEntity.status(400).body("Error al crear sesión");
    }

    @PutMapping("/{idSesion}")
    public ResponseEntity<?> updateSesion(@PathVariable int idSesion, @RequestBody Sesion sesion) {
        sesion.setIdSesion(idSesion);
        Sesion actualizada = sesionService.updateOne(sesion);
        if (actualizada != null) {
            return ResponseEntity.status(200).body(SesionDto.convertirADto(actualizada));
        }
        return ResponseEntity.status(404).body("Sesión no existe");
    }

    @DeleteMapping("/{idSesion}")
    public ResponseEntity<String> deleteSesion(@PathVariable int idSesion) {
        switch (sesionService.deleteOne(idSesion)) {
            case 1: return ResponseEntity.status(200).body("Sesión Eliminada");
            case 0: return ResponseEntity.status(404).body("Sesión no existe");
            case -1: return ResponseEntity.status(400).body("Sesión tiene reservas activas");
            default: return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}