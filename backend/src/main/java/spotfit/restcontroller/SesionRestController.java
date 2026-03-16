package spotfit.restcontroller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import spotfit.modelo.dto.SesionDto;
import spotfit.modelo.entities.Sesion;
import spotfit.modelo.service.SesionService;

@RestController
@RequestMapping("/sesiones")
public class SesionRestController {

    @Autowired
    private SesionService sesionService;
    
  //Metodos findAll y findById filtrados con el DTO

    @GetMapping("")
    public List<SesionDto> getAllSesiones() {
        return sesionService.findAllDtos();
    }
    
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
    
  //Demas metodos

    @PostMapping("")
    public ResponseEntity<?> addSesion(@RequestBody Sesion sesion) {
        return ResponseEntity.status(201).body(sesionService.insertOne(sesion));
    }

    @PutMapping("/{idSesion}")
    public ResponseEntity<?> updateSesion(@PathVariable int idSesion, @RequestBody Sesion sesion) {
        sesion.setIdSesion(idSesion);
        if (sesionService.updateOne(sesion) != null)
            return ResponseEntity.status(200).body(sesion);
        else
            return ResponseEntity.status(404).body("Sesion no existe");
    }

    @DeleteMapping("/{idSesion}")
    public ResponseEntity<String> deleteSesion(@PathVariable int idSesion) {
        switch (sesionService.deleteOne(idSesion)) {
            case 1: return ResponseEntity.status(200).body("Sesion Eliminada");
            case 0: return ResponseEntity.status(404).body("Sesion no existe");
            case -1: return ResponseEntity.status(400).body("Sesion tiene reservas activas");
            default: return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}