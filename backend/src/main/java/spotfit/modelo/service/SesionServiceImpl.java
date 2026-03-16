package spotfit.modelo.service;

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


  //Metodos que nos devuelven la entidad sesion entera

    @Override
    public Sesion findById(Integer atributoId) {
        return sesionRepository.findById(atributoId).orElse(null);
    }

    @Override
    public List<Sesion> findAll() {
        return sesionRepository.findAll();
    }
    
    // INSERTAR SESIÓN CON AFORO AUTOMÁTICO
    
//    ¿Cómo funciona?
//    	     * 1. El ADMIN crea una sesión desde el frontend
//    	     * 2. Selecciona un servicio (ej: "Fisioterapia")
//    	     * 3. El backend consulta la categoría de ese servicio ("SALUD")
//    	     * 4. Según la categoría, asigna el aforo:
//    	     *    - SALUD → 1 plaza (citas individuales)
//    	     *    - CLASE → 15 plazas (clases grupales)
//    	     * 5. Guarda la sesión con el aforo calculado

    @Override
    public Sesion insertOne(Sesion entidad) {
    	
    	if (entidad.getAforoMaximo() <= 0) {            
        // Obtenemos la categoría del servicio asociado  
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
    //Guardamos la sesión en la base de datos
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
    
  //Metodos que nos devuelven DTOs

    @Override
    public List<SesionDto> findAllDtos() {
        return sesionRepository.findAll()
                .stream()
                .map(sesion -> {
                    SesionDto dto = SesionDto.convertirADto(sesion);
                    dto.setReservasActuales(sesionRepository.countReservasConfirmadas(sesion.getIdSesion()));
                    return dto;
                })
                .toList();
    }

    @Override
    public SesionDto findDtoById(Integer id) {
        Sesion sesion = sesionRepository.findById(id).orElse(null);
        if (sesion == null) return null;
        SesionDto dto = SesionDto.convertirADto(sesion);
        dto.setReservasActuales(sesionRepository.countReservasConfirmadas(sesion.getIdSesion()));
        return dto;
    }
	
    @Override
    public List<SesionDto> findSesionesHoy() {
        return sesionRepository.findSesionesHoy()
                .stream()
                .map(sesion -> {
                    SesionDto dto = SesionDto.convertirADto(sesion);
                    dto.setReservasActuales(sesionRepository.countReservasConfirmadas(sesion.getIdSesion()));
                    return dto;
                })
                .toList();
    }
}