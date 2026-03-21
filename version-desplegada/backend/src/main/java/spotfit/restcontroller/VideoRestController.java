package spotfit.restcontroller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spotfit.modelo.dto.VideoDto;
import spotfit.modelo.entities.Video;
import spotfit.modelo.service.VideoService;

@RestController
@RequestMapping("/videos")
public class VideoRestController {

    @Autowired
    private VideoService videoService;

    // Métodos GET usan DTOs
    
    @GetMapping("")
    public List<VideoDto> getAllVideos() {
        return videoService.findAllDtos();
    }

    @GetMapping("/{idVideo}")
    public ResponseEntity<VideoDto> getOneVideo(@PathVariable int idVideo) {
        VideoDto video = videoService.findDtoById(idVideo);
        if (video != null)
            return ResponseEntity.status(200).body(video);
        else
            return ResponseEntity.status(404).body(null);
    }

    // Métodos POST, PUT, DELETE siguen usando entidades
    
    @PostMapping("")
    public ResponseEntity<?> addVideo(@RequestBody Video video) {
        return ResponseEntity.status(201).body(videoService.insertOne(video));
    }

    @PutMapping("/{idVideo}")
    public ResponseEntity<?> updateVideo(@PathVariable int idVideo, @RequestBody Video video) {
        video.setIdVideo(idVideo);
        if (videoService.updateOne(video) != null)
            return ResponseEntity.status(200).body(video);
        else
            return ResponseEntity.status(404).body("Video no existe");
    }

    @DeleteMapping("/{idVideo}")
    public ResponseEntity<String> deleteVideo(@PathVariable int idVideo) {
        switch (videoService.deleteOne(idVideo)) {
            case 1: return ResponseEntity.status(200).body("Video Eliminado");
            case 0: return ResponseEntity.status(404).body("Video no existe");
            default: return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}