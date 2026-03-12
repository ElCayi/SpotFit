package spotfit.restcontroller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spotfit.modelo.dto.SalaDto;
import spotfit.modelo.entities.Sala;
import spotfit.modelo.service.SalaService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/salas")
public class SalaRestController {

    @Autowired
    private SalaService salaService;

    // Métodos GET usan DTOs
    
    @GetMapping("")
    public List<SalaDto> getAllSalas() {
        return salaService.findAllDtos();
    }

    @GetMapping("/{idSala}")
    public ResponseEntity<SalaDto> getOneSala(@PathVariable int idSala) {
        SalaDto sala = salaService.findDtoById(idSala);
        if (sala != null)
            return ResponseEntity.status(200).body(sala);
        else
            return ResponseEntity.status(404).body(null);
    }

    // Métodos POST, PUT, DELETE siguen usando entidades
    
    @PostMapping("/")
    public ResponseEntity<?> addSala(@RequestBody Sala sala) {
        return ResponseEntity.status(201).body(salaService.insertOne(sala));
    }

    @PutMapping("/{idSala}")
    public ResponseEntity<?> updateSala(@PathVariable int idSala, @RequestBody Sala sala) {
        sala.setIdSala(idSala);
        if (salaService.updateOne(sala) != null)
            return ResponseEntity.status(200).body(sala);
        else
            return ResponseEntity.status(404).body("Sala no existe");
    }

    @DeleteMapping("/{idSala}")
    public ResponseEntity<String> deleteSala(@PathVariable int idSala) {
        switch (salaService.deleteOne(idSala)) {
            case 1: return ResponseEntity.status(200).body("Sala Eliminada");
            case 0: return ResponseEntity.status(404).body("Sala no existe");
            case -1: return ResponseEntity.status(400).body("Sala en uso");
            default: return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}