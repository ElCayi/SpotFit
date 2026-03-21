package spotfit.modelo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import spotfit.modelo.dto.SesionDto;
import spotfit.modelo.entities.Sesion;
import spotfit.modelo.repository.SesionRepository;

@Service
public class SesionServiceImpl implements SesionService {

    @Autowired
    private SesionRepository sesionRepository;

    @Override
    public Sesion findById(Integer atributoId) {
        return sesionRepository.findById(atributoId).orElse(null);
    }

    @Override
    public List<Sesion> findAll() {
        return sesionRepository.findAll();
    }

    @Override
    public Sesion insertOne(Sesion entidad) {
        if (entidad.getAforoMaximo() <= 0) {
            String categoria = entidad.getServicio().getCategoria();
            int aforoCalculado;
            if ("SALUD".equals(categoria)) {
                aforoCalculado = 1;
            } else if ("CLASE".equals(categoria)) {
                aforoCalculado = 15;
            } else {
                aforoCalculado = 10;
            }
            entidad.setAforoMaximo(aforoCalculado);
        }
        return sesionRepository.save(entidad);
    }

    @Override
    public Sesion updateOne(Sesion entidad) {
        if (sesionRepository.existsById(entidad.getIdSesion()))
            return sesionRepository.save(entidad);
        else
            return null;
    }

    @Override
    public int deleteOne(Integer atributoId) {
        if (sesionRepository.existsById(atributoId)) {
            try {
                sesionRepository.deleteById(atributoId);
                return 1;
            } catch (Exception e) {
                return -1;
            }
        } else {
            return 0;
        }
    }

    @Override
    public List<SesionDto> findAllDtos() {
        return sesionRepository.findAll()
                .stream()
                .map(SesionDto::convertirADto)
                .toList();
    }

    @Override
    public SesionDto findDtoById(Integer id) {
        Sesion sesion = sesionRepository.findById(id).orElse(null);
        return sesion != null ? SesionDto.convertirADto(sesion) : null;
    }

    @Override
    public List<SesionDto> findSesionesHoy() {
        LocalDate hoy = LocalDate.now();
        LocalDateTime inicioHoy = hoy.atStartOfDay();
        LocalDateTime finHoy = hoy.atTime(23, 59, 59);
        
        return sesionRepository.findByFechaInicioBetween(inicioHoy, finHoy)
                .stream()
                .map(SesionDto::convertirADto)
                .toList();
    }
}