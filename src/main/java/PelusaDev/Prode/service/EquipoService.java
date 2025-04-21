package PelusaDev.Prode.service;

import PelusaDev.Prode.dto.EquipoDTO;
import PelusaDev.Prode.mapper.EquipoMapper;
import PelusaDev.Prode.model.Equipo;
import PelusaDev.Prode.repository.EquipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EquipoService implements IEquipoService {

    @Autowired
    private EquipoRepository equipoRepository;

    @Override
    public List<EquipoDTO> getAllEquipos() {
        return equipoRepository.findAll().stream()
                .map(EquipoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<EquipoDTO> getEquipoById(Long id) {
        return equipoRepository.findById(id).map(EquipoMapper::toDTO);
    }

    @Override
    public EquipoDTO saveEquipo(EquipoDTO equipoDTO) {
        Equipo equipo = EquipoMapper.toEntity(equipoDTO);
        return EquipoMapper.toDTO(equipoRepository.save(equipo));
    }

    @Override
    public EquipoDTO updateEquipo(EquipoDTO equipoDTO, Long id) {
        return equipoRepository.findById(id)
                .map(existingEquipo -> {
                    Equipo equipo = EquipoMapper.toEntity(equipoDTO);
                    equipo.setId(id); // Mantener el ID
                    return EquipoMapper.toDTO(equipoRepository.save(equipo));
                })
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado"));
    }

    @Override
    public void deleteEquipo(Long id) {
        equipoRepository.deleteById(id);
    }
}
