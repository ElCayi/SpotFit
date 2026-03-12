package spotfit.modelo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spotfit.modelo.dto.ServicioDto;
import spotfit.modelo.entities.Servicio;
import spotfit.modelo.repository.ServicioRepository;

@Service
public class ServicioServiceImpl implements ServicioService {

    @Autowired
    private ServicioRepository servicioRepository;

    @Override
    public Servicio findById(Integer atributoId) {
        return servicioRepository.findById(atributoId).orElse(null);
    }

    @Override
    public List<Servicio> findAll() {
        return servicioRepository.findAll();
    }

    @Override
    public Servicio insertOne(Servicio entidad) {
        return servicioRepository.save(entidad);
    }

    @Override
    public Servicio updateOne(Servicio entidad) {
        if (servicioRepository.existsById(entidad.getIdServicio()))
            return servicioRepository.save(entidad);
        else
            return null;
    }

    @Override
    public int deleteOne(Integer atributoId) {
        if (servicioRepository.existsById(atributoId)) {
            try {
                servicioRepository.deleteById(atributoId);
                return 1;
            } catch (Exception e) {
                return -1;
            }
        } else {
            return 0;
        }
    }
    

    // MÉTODOS CON DTOs (NUEVOS)
  
    
    @Override
    public ServicioDto findDtoById(Integer id) {
        Servicio servicio = servicioRepository.findById(id).orElse(null);
        return servicio != null ? ServicioDto.convertirADto(servicio) : null;
    }
    
    @Override
    public List<ServicioDto> findAllDtos() {
        return servicioRepository.findAll()
                .stream()
                .map(servicio -> ServicioDto.convertirADto(servicio))
                .toList();
    }
}