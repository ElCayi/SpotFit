package spotfit.restcontroller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spotfit.modelo.dto.NoticiaDto;
import spotfit.modelo.entities.Noticia;
import spotfit.modelo.service.NoticiaService;

@RestController
@RequestMapping("/noticias")
public class NoticiaRestController {

    @Autowired
    private NoticiaService noticiaService;

    // Métodos GET usan DTOs
    
    @GetMapping("")
    public List<NoticiaDto> getAllNoticias() {
        return noticiaService.findAllDtos();
    }

    @GetMapping("/{idNoticia}")
    public ResponseEntity<NoticiaDto> getOneNoticia(@PathVariable int idNoticia) {
        NoticiaDto noticia = noticiaService.findDtoById(idNoticia);
        if (noticia != null)
            return ResponseEntity.status(200).body(noticia);
        else
            return ResponseEntity.status(404).body(null);
    }

    // Métodos POST, PUT, DELETE siguen usando entidades
    
    @PostMapping("/")
    public ResponseEntity<?> addNoticia(@RequestBody Noticia noticia) {
        return ResponseEntity.status(201).body(noticiaService.insertOne(noticia));
    }

    @PutMapping("/{idNoticia}")
    public ResponseEntity<?> updateNoticia(@PathVariable int idNoticia, @RequestBody Noticia noticia) {
        noticia.setIdNoticia(idNoticia);
        if (noticiaService.updateOne(noticia) != null)
            return ResponseEntity.status(200).body(noticia);
        else
            return ResponseEntity.status(404).body("Noticia no existe");
    }

    @DeleteMapping("/{idNoticia}")
    public ResponseEntity<String> deleteNoticia(@PathVariable int idNoticia) {
        switch (noticiaService.deleteOne(idNoticia)) {
            case 1: return ResponseEntity.status(200).body("Noticia Eliminada");
            case 0: return ResponseEntity.status(404).body("Noticia no existe");
            default: return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}