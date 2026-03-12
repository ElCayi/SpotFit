package spotfit.modelo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spotfit.modelo.dto.SalaDto;
import spotfit.modelo.entities.Sala;
import spotfit.modelo.repository.SalaRepository;

@Service
public class SalaServiceImpl implements SalaService {

    @Autowired
    private SalaRepository salaRepository;

    @Override
    public Sala findById(Integer atributoId) {
        return salaRepository.findById(atributoId).orElse(null);
    }

    @Override
    public List<Sala> findAll() {
        return salaRepository.findAll();
    }

    @Override
    public Sala insertOne(Sala entidad) {
        return salaRepository.save(entidad);
    }

    @Override
    public Sala updateOne(Sala entidad) {
        if (salaRepository.existsById(entidad.getIdSala()))
            return salaRepository.save(entidad);
        else
            return null;
    }

    @Override
    public int deleteOne(Integer atributoId) {
        if (salaRepository.existsById(atributoId)) {
            try {
                salaRepository.deleteById(atributoId);
                return 1;
            } catch (Exception e) {
                return -1;
            }
        } else {
            return 0;
        }
    }
    

    // MÉTODOS CON DTOs

    
    @Override
    public SalaDto findDtoById(Integer id) {
        Sala sala = salaRepository.findById(id).orElse(null);
        return sala != null ? SalaDto.convertirADto(sala) : null;
    }
    
    @Override
    public List<SalaDto> findAllDtos() {
        return salaRepository.findAll()
                .stream()
                .map(sala -> SalaDto.convertirADto(sala))
                .toList();
    }
}