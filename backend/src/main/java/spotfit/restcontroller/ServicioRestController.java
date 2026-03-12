package spotfit.restcontroller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spotfit.modelo.dto.ServicioDto;
import spotfit.modelo.entities.Servicio;
import spotfit.modelo.service.ServicioService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/servicios")
public class ServicioRestController {

    @Autowired
    private ServicioService servicioService;

    // Métodos GET usan DTOs
    
    @GetMapping("")
    public List<ServicioDto> getAllServicios() {
        return servicioService.findAllDtos();
    }

    @GetMapping("/{idServicio}")
    public ResponseEntity<ServicioDto> getOneServicio(@PathVariable int idServicio) {
        ServicioDto servicio = servicioService.findDtoById(idServicio);
        if (servicio != null)
            return ResponseEntity.status(200).body(servicio);
        else
            return ResponseEntity.status(404).body(null);
    }

    // Métodos POST, PUT, DELETE siguen usando entidades
    
    @PostMapping("/")
    public ResponseEntity<?> addServicio(@RequestBody Servicio servicio) {
        return ResponseEntity.status(201).body(servicioService.insertOne(servicio));
    }

    @PutMapping("/{idServicio}")
    public ResponseEntity<?> updateServicio(@PathVariable int idServicio, @RequestBody Servicio servicio) {
        servicio.setIdServicio(idServicio);
        if (servicioService.updateOne(servicio) != null)
            return ResponseEntity.status(200).body(servicio);
        else
            return ResponseEntity.status(404).body("Servicio no existe");
    }

    @DeleteMapping("/{idServicio}")
    public ResponseEntity<String> deleteServicio(@PathVariable int idServicio) {
        switch (servicioService.deleteOne(idServicio)) {
            case 1: return ResponseEntity.status(200).body("Servicio Eliminado");
            case 0: return ResponseEntity.status(404).body("Servicio no existe");
            case -1: return ResponseEntity.status(400).body("Servicio no se puede eliminar (vinculado a sesiones)");
            default: return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}