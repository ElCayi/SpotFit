package spotfit.restcontroller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import spotfit.modelo.dto.ReservaDto;
import spotfit.modelo.entities.Reserva;
import spotfit.modelo.service.ReservaService;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/reservas")
public class ReservaRestController {

    @Autowired
    private ReservaService reservaService;
    
  //Metodos findAll y findById filtrados con el DTO

    @GetMapping("")
    public List<ReservaDto> getAllReservas() {
        return reservaService.findAllDtos();
    }

    @GetMapping("/{idReserva}")
    public ResponseEntity<ReservaDto> getOneReserva(@PathVariable int idReserva) {
        ReservaDto reserva = reservaService.findDtoById(idReserva);
        if (reserva != null)
            return ResponseEntity.status(200).body(reserva);
        else
            return ResponseEntity.status(404).body(null);
    }
    
  //Demas metodos

    @PostMapping("")
    public ResponseEntity<?> addReserva(@RequestBody Reserva reserva) {
        Reserva guardada = reservaService.insertOne(reserva);
        if (guardada != null) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("mensaje", "Reserva creada correctamente");
            resp.put("idReserva", guardada.getIdReserva());
            return ResponseEntity.status(201).body(resp);
        }
        return ResponseEntity.status(400).body("No hay plazas disponibles");
    }

    @PutMapping("/{idReserva}")
    public ResponseEntity<?> updateReserva(@PathVariable int idReserva, @RequestBody Reserva reserva) {
        reserva.setIdReserva(idReserva);
        if (reservaService.updateOne(reserva) != null)
            return ResponseEntity.status(200).body(reserva);
        else
            return ResponseEntity.status(404).body("Reserva no existe");
    }

    @DeleteMapping("/{idReserva}")
    public ResponseEntity<String> deleteReserva(@PathVariable int idReserva) {
        switch (reservaService.deleteOne(idReserva)) {
            case 1: return ResponseEntity.status(200).body("Reserva Eliminada");
            case 0: return ResponseEntity.status(404).body("Reserva no existe");
            case -1: return ResponseEntity.status(400).body("No se puede eliminar la reserva");
            default: return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}